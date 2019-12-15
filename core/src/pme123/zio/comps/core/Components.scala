package pme123.zio.comps.core

import pme123.zio.comps.core.Components.ComponentsEnv
import zio.console.Console
import zio.{RIO, ZIO}

import scala.reflect.ClassTag

trait Components extends Serializable {
  val components: Components.Service[ComponentsEnv]
}

object Components {

  type ComponentsEnv = Console
  type ComponentsTask[A] = RIO[ComponentsEnv, A]

  trait Service[R <: ComponentsEnv] {
    def load[T <: Component : ClassTag](ref: CompRef): RIO[R, T]

    def render(component: Component): RIO[R, String]
  }

  trait Live extends Components {

    def compsService: Service[ComponentsEnv]

    val components: Components.Service[ComponentsEnv] = new Components.Service[ComponentsEnv] {

      def load[T <: Component : ClassTag](ref: CompRef): RIO[ComponentsEnv, T] =
        compsService.load[T](ref)

      def render(component: Component): RIO[ComponentsEnv, String] =
        compsService.render(component)
    }
  }

  object > extends Service[Components with ComponentsEnv] {
    final def load[T <: Component : ClassTag](ref: CompRef): RIO[Components with ComponentsEnv, T] =
      ZIO.accessM(_.components.load(ref))

    final def render(component: Component): RIO[Components with ComponentsEnv, String] =
      ZIO.accessM(_.components.render(component))
  }

}