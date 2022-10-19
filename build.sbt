

ThisBuild / organization := "io.github.pme123"
ThisBuild / scalaVersion := "2.13.9"
ThisBuild / version      := "0.2.0-SNAPSHOT"
ThisBuild / onLoadMessage := loadingMessage
ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / evictionErrorLevel := Level.Warn

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-comps-module",
    // Ammonite IDE support
    // does not work because of Scala 3 / 2.13 issues
    // libraryDependencies += "com.lihaoyi" % s"ammonite_2.13.6" % ammoniteV
  )
  .aggregate(core)

lazy val core = project
  .in(file("./core"))
  .settings(
    scalacOptions ++= defaultScalaOpts,
    libraryDependencies += libs.zio
  )
lazy val hocon = project
  .in(file("./hocon"))
  .settings(
    scalacOptions ++= extendedScalaOpts,
    libraryDependencies += libs.pureconfig
  )
  .dependsOn(core)

lazy val yaml = project
  .in(file("./yaml"))
  .settings(
    scalacOptions ++= extendedScalaOpts,
    libraryDependencies ++=
      Seq(libs.circeGeneric, libs.circeYaml)
  )
  .dependsOn(core)

lazy val tests = project
  .in(file("./tests"))
  .settings(
    scalacOptions ++= defaultScalaOpts,
    libraryDependencies ++= Seq(libs.zio, libs.zioTest),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
  .dependsOn(core)

lazy val app = project
  .in(file("./app"))
  .settings(
    scalacOptions ++= defaultScalaOpts,
    libraryDependencies ++= Seq(libs.macwireUtil, libs.macwireMacros)
  )
  .dependsOn(hocon, yaml)

lazy val loadingMessage = "ZIO Comps Modules loaded"

lazy val defaultScalaOpts = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "UTF-8", // Specify character encoding used by source files.
  "-language:higherKinds", // Allow higher-kinded types
  "-language:postfixOps", // Allows operator syntax in postfix position (deprecated since Scala 2.10)
  "-feature" // Emit warning and location for usages of features that should be imported explicitly.
  //  "-Ypartial-unification",      // Enable partial unification in type constructor inference
  //  "-Xfatal-warnings"            // Fail the compilation if there are any warnings
)
lazy val extendedScalaOpts =
  defaultScalaOpts ++ Seq("-Ymacro-annotations", "-Ymacro-debug-lite")
