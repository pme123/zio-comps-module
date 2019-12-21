package pme123.zio.comps.hocon

import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core._
import pureconfig.generic.auto._
import pureconfig.{ConfigReader, ConfigSource, ConfigWriter}
import zio.console.Console
import zio.{RIO, Task, ZIO, console}

import scala.reflect.ClassTag

class HoconComps extends Components.Service[ComponentsEnv] {

  def load[T <: Component : ClassTag](ref: CompRef): RIO[ComponentsEnv, T] =
    loadConf[Component](ref).map { case c: T => c }

  def render(component: Component): RIO[ComponentsEnv, String] =
    renderConf(component)

  private def loadConf[T <: Component : ConfigReader : ClassTag](
                                                                  ref: CompRef
                                                                ): RIO[Console, T] = {
    for {
      component <- Task.effect(ConfigSource.resources(s"${ref.url}.conf").loadOrThrow[T])
      _ <- console.putStrLn(s"\nComponent:\n$component")
    } yield component
  }

  private def renderConf(
                          component: Component
                        ): RIO[Console, String] =
    for {
      configValue <- ZIO.effectTotal(ConfigWriter[Component].to(component))
      configString <- ZIO.effectTotal(configValue.render())
      _ <- renderOutput(component, configString)
    } yield configString

}
