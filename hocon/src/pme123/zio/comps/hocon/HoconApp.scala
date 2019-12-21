package pme123.zio.comps.hocon

import pme123.zio.comps.core._
import pme123.zio.comps.core.components.Components
import zio.ZIO

object HoconApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {

    program.provideCustomLayer(
      Components.live (new HoconComps)
    )
  }
}
