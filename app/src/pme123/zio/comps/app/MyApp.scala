package pme123.zio.comps.app

import com.softwaremill.macwire._
import pme123.zio.comps.core.CompApp
import pme123.zio.comps.core.components.Components
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import zio._
import zio.console.Console

object MyApp extends CompApp {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    (for {
      myConfig <- config()
      service <- wire[Components.Service](myConfig.compsImpl)
      run <- program.provideLayer(
        Console.live ++ Components.live(service)
      )
    } yield run)
      .catchAll { t =>
        console.putStrLn(s"Problem init Components Module.\n - $t")
          .as(1)
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
