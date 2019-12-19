package pme123.zio.comps.yaml

import pme123.zio.comps.core.CompApp.AppEnv
import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core._
import zio.ZIO
import zio.console.Console

object YamlApp extends CompApp {

  lazy val environment: AppEnv = new Console.Live with Components.Live {
    def compsService: Components.Service[ComponentsEnv] = new YamlComps
  }

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    program.provide(
      environment
    )
  }
}
