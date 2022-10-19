import sbt._

object ver {
  val cats = "2.0.0"
  val circe = "0.12.1"
  val circeYaml = "0.12.0"
  val macwire = "2.3.3"
  val pureconfig = "0.12.1"
  val zio = "1.0.0-RC18"
  val zioCats = "2.0.0.0-RC7"
}

object libs {
  val cats = "org.typelevel" %% "cats-core" % ver.cats
  val circeCore = "io.circe" %% "circe-core" % ver.circe
  val circeGeneric = "io.circe" %% "circe-generic" % ver.circe
  val circeYaml = "io.circe" %% "circe-yaml" % ver.circeYaml
  val macwireUtil = "com.softwaremill.macwire" %% "util" % ver.macwire
  val macwireMacros = "com.softwaremill.macwire" %% "macros" % ver.macwire
  val pureconfig =
    "com.github.pureconfig" %% "pureconfig" % ver.pureconfig
  val zio = "dev.zio" %% "zio" % ver.zio
  //  val zioMacrosMockable = "dev.zio" %% "zio-macros-mock" %  ver.zioMacros
  val zioStream = "dev.zio" %% "zio-streams" % ver.zio
  val zioCats = "dev.zio" %% "zio-interop-cats" % ver.zioCats
  val zioTest = "dev.zio" %% "zio-test" % ver.zio
  val zioTestSbt = "dev.zio" %% "zio-test-sbt" % ver.zio
}
