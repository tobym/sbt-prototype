//BASIC PROJECT INFO
name := "myproj"

organization := "com.yuvimasory"

version := "alpha"

//SCALA VERSIONS AND OPTIONS
scalaVersion := "2.9.0-1"

crossScalaVersions := Seq("2.9.0-1", "2.9.0", "2.8.1", "2.8.0")

scalacOptions ++= Seq("-deprecation", "-unchecked")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

//ENTRY POINT
mainClass in (Compile, packageBin) := Some("com.yuvimasory.myproj.Main")

mainClass in (Compile, run) := Some("com.yuvimasory.myproj.Main")

//SCALA DEPENDENCIES
libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
  val scalaCheckVersionMap = Map("2.8.0"   -> ("scalacheck_2.8.0",   "1.7"),
                                 "2.8.1"   -> ("scalacheck_2.8.1",   "1.8"),
                                 "2.9.0"   -> ("scalacheck_2.9.0",   "1.8"),
                                 "2.9.0-1" -> ("scalacheck_2.9.0-1", "1.9"))
  val (scalaCheckArtifact, scalaCheckVersion) = scalaCheckVersionMap.getOrElse(
    sv, error("Unsupported Scala version: " + scalaVersion)
  )
  deps :+ ("org.scala-tools.testing" % scalaCheckArtifact %
           scalaCheckVersion % "test")
}

//only uncomment if you need dependencies from the snapshots repo
//resolvers += ScalaToolsSnapshots

//JAVA DEPENDENCIES
libraryDependencies ++= Seq (
  //"com.martiansoftware" % "jsap" % "2.1"
)

//SBT BEHAVIOR
fork in Test := true

fork in Compile := true

logLevel := Level.Info //higher than Info suppresses your own printlns

traceLevel := 5

//PROGUARD
seq(ProguardPlugin.proguardSettings :_*)

proguardOptions ++= Seq (
    "-dontshrink -dontoptimize -dontobfuscate -dontpreverify -dontnote " +
    "-ignorewarnings",
    keepAllScala
)


//PUBLISHING

//this results in warnings if the listed file doesn't exist
// credentials += Credentials(Path.userHome / ".scala-tools")

publishTo <<= version {(v: String) =>
    val nexus = "http://nexus.scala-tools.org/content/repositories/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "snapshots/") 
    else                             Some("releases"  at nexus + "releases/")
}

publishMavenStyle := true

publishArtifact in (Test, packageBin) := false

publishArtifact in (Compile, packageDoc):= false

publishArtifact in (Compile, packageSrc):= false


//UNPORTED FROM 0.7.x prototype
// extraResources = "README.rst" +++ "LICENSE"
// mainResources = super.mainResources +++ extraResources

// override def documentOptions =
//   LinkSource ::
//   documentTitle(name + " " + version + " API") ::
//   windowTitle(name + " " + version + " API") ::
//   Nil