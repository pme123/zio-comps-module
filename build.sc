import mill._
import mill.scalalib._

trait MyModule extends ScalaModule {
  def scalaVersion = "2.13.1"

  object version {
    val cats = "2.0.0"
    val circe = "0.12.1"
    val circeYaml = "0.12.0"
    val pureconfig = "0.12.1"
    val zio = "1.0.0-RC16"
    val zioCats = "2.0.0.0-RC7"
  }

  object libs {
    val cats = ivy"org.typelevel::cats-core:${version.cats}"
    val circeCore = ivy"io.circe::circe-core:${version.circe}"
    val circeGeneric = ivy"io.circe::circe-generic:${version.circe}"
    val circeYaml = ivy"io.circe::circe-yaml:${version.circeYaml}"
    val pureconfig =
      ivy"com.github.pureconfig::pureconfig:${version.pureconfig}"
    val zio = ivy"dev.zio::zio:${version.zio}"
    //  val zioMacrosMockable = ivy"dev.zio::zio-macros-mock:${version.zioMacros}"
    val zioStream = ivy"dev.zio::zio-streams:${version.zio}"
    val zioCats = ivy"dev.zio::zio-interop-cats:${version.zioCats}"
  }

  object test extends Tests {
    override def ivyDeps = Agg(
      ivy"dev.zio::zio-test:${version.zio}",
      ivy"dev.zio::zio-test-sbt:${version.zio}"
    )

    def testOne(args: String*) = T.command {
      super.runMain("org.scalatest.run", args: _*)
    }

    def testFrameworks =
      Seq("zio.test.sbt.ZTestFramework")
  }

  override def scalacOptions =
    defaultScalaOpts

  val defaultScalaOpts = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "UTF-8", // Specify character encoding used by source files.
    "-language:higherKinds", // Allow higher-kinded types
    "-language:postfixOps", // Allows operator syntax in postfix position (deprecated since Scala 2.10)
    "-feature" // Emit warning and location for usages of features that should be imported explicitly.
    //  "-Ypartial-unification",      // Enable partial unification in type constructor inference
    //  "-Xfatal-warnings"            // Fail the compilation if there are any warnings
  )

}

object core extends MyModule {
  override def ivyDeps = {
    Agg(
      libs.zio
    )
  }
}

object hocon extends MyModule {

  override def moduleDeps = Seq(core)

  override def scalacOptions =
    defaultScalaOpts ++ Seq("-Ymacro-annotations", "-Ymacro-debug-lite")

  override def ivyDeps = {
    Agg(
      libs.pureconfig
    )
  }
}

object yaml extends MyModule {
  override def moduleDeps = Seq(core)

  override def scalacOptions =
    defaultScalaOpts ++ Seq("-Ymacro-annotations", "-Ymacro-debug-lite")

  override def ivyDeps = {
    Agg(
      libs.circeGeneric,
      libs.circeYaml
    )
  }
}

