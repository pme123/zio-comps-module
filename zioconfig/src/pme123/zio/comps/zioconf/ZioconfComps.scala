package pme123.zio.comps.zioconf

import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core._
import zio.config._
import zio.config.typesafe.TypeSafeConfigSource._
import zio.{RIO, Task, ZIO, console}

import scala.reflect.ClassTag

class ZioconfComps extends Components.Service[ComponentsEnv] {

  val componentsConfig: ConfigDescriptor[String, String, Component] = ??? //description[Component]

  def load[T <: Component : ClassTag](ref: CompRef): RIO[ComponentsEnv, T] =
    loadConf[Component](ref).map { case c: T => c }

  def render(component: Component): RIO[ComponentsEnv, String] =
    renderConf(component)

  private def loadConf[T <: Component  : ClassTag](
                                                                  ref: CompRef
                                                                ) = {
    (for {
      component <- read(componentsConfig.from(hoccon(Left(new java.io.File(s"${ref.url}.conf")))))
      _ <- console.putStrLn(s"\nComponent:\n$component")
    } yield component)
      .mapError(errors => new IllegalArgumentException(s"Problems reading Component ${ref.url}.conf: ${errors.mkString("\n")}"))
  }

  private def renderConf(
                          component: Component
                        ): Task[String] =
   ZIO.fromEither(write(componentsConfig, component).map(_.flattenString().toString()))
    .mapError(new IllegalArgumentException(_))


}
