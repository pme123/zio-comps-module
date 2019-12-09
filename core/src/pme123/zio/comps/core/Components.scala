package pme123.zio.comps.core

import pme123.zio.comps.core.Components.ComponentsEnv
import zio.console.Console
import zio.{RIO, ZIO}

trait Components extends Serializable {
  val components: Components.Service[ComponentsEnv]
}

object Components {

  type ComponentsEnv = Console
  type ComponentsTask[A] = RIO[ComponentsEnv, A]

  trait Service[R <: ComponentsEnv] {
    def load[T <: Component](ref: CompRef): RIO[R, T]

    def render(component: Component): RIO[R, String]
  }

  object > extends Service[Components with ComponentsEnv] {
    final def load[T <: Component](ref: CompRef): RIO[Components with ComponentsEnv, T] =
      ZIO.accessM(_.components.load(ref))

    final def render(component: Component): RIO[Components with ComponentsEnv, String] =
      ZIO.accessM(_.components.render(component))
  }

}