package pme123.zio.comps.app

import pme123.zio.comps.core.{CompApp, Components}
import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio._
import zio.console.Console
import com.softwaremill.macwire._
import pme123.zio.comps.core.Components.ComponentsEnv

import scala.reflect.ClassTag

object MyApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    (for {
      myConfig <- config()
      service <- wire[Components.Service[ComponentsEnv]](myConfig.compsImpl)
      run <- program.provide(
        new Console.Live with Components.Live {
          def compsService: Components.Service[ComponentsEnv] = service
        }
      )
    } yield run)
      .catchAll { t =>
        console.putStrLn(s"Problem init Components Module.\n - $t")
          .map(_ => 1)
      }
  }

  case class MyConfig(compsImpl: String)

  private def config(): Task[MyConfig] = ZIO.effect(ConfigSource.default.loadOrThrow[MyConfig])

  private def wire[T](implName: String): Task[T] =
    ZIO.effect(
      Wired(Map.empty)
        .wireClassInstanceByName(implName)
        .asInstanceOf[T]
    )

}
