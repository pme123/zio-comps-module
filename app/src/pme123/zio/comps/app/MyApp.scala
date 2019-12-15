package pme123.zio.comps.app

import pme123.zio.comps.core.{CompApp, Components}
import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio._
import zio.console.Console
import com.softwaremill.macwire._
import pme123.zio.comps.core.Components.ComponentsEnv

object MyApp extends CompApp {

  case class MyConfig(compsService: String)

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val wired = wiredInModule(None)
    val zEnv: ZIO[Any, Throwable, ZIO[zio.ZEnv, Nothing, Int]] = for {
      compClass <- ZIO.effect(ConfigSource.default.loadOrThrow[MyConfig])
      service <- ZIO.effect(wired.wireClassInstanceByName(compClass.compsService).asInstanceOf[Components.Service[ComponentsEnv]])
      run: ZIO[ZEnv, Nothing, Int] = program.provide(
        new Console.Live with Components.Live {
          def configService: Components.Service[ComponentsEnv] = service
        }
      )
    } yield run
    zEnv.flatten.catchAll { t =>
      console.putStrLn(s"Problem init Components Module.\n - $t") *>
        ZIO.effectTotal(1)
    }

  }
}
