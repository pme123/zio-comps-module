package pme123.zio.comps.app

import pme123.zio.comps.core.CompApp
import pme123.zio.comps.hocon.HoconComps
import pme123.zio.comps.yaml.YamlComps
import zio.ZIO
import zio.console.Console

object MyApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    program.provide(
      args.headOption match {
        case Some("YAML") =>
          new Console.Live with YamlComps {}
        case _ =>
          new Console.Live with HoconComps {}
      }
    )
}
