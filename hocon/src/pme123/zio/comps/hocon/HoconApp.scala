package pme123.zio.comps.hocon

import pme123.zio.comps.core._
import zio.ZIO
import zio.console.Console

object HoconApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    program.provide(
      new Console.Live with HoconComps {}
    )
}
