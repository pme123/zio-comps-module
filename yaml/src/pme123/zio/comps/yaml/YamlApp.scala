package pme123.zio.comps.yaml

import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core._
import zio.ZIO
import zio.console.Console

object YamlApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    program.provide(
      new Console.Live with Components.Live {
        def compsService: Components.Service[ComponentsEnv] = new YamlComps
      }
    )
}
