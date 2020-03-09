package pme123.zio.comps.core

import zio.console.Console
import zio._

import scala.reflect.ClassTag

object components {
  type Components = Has[Components.Service]
  type ComponentsEnv = Console

  object Components {

    trait Service {

      protected def renderOutput(comp: Component, output: String): ZIO[ComponentsEnv, Nothing, Unit] =
        console.putStrLn(
          s"$renderOutputPrefix ${comp.name}:\n$output"
        )

      def load[T <: Component : ClassTag](ref: CompRef): RIO[ComponentsEnv, T]

      def render(component: Component): RIO[ComponentsEnv, String]
    }

    def live(service: Service): ZLayer.NoDeps[Nothing, Components] = ZLayer.succeed(service)

    final def load[T <: Component : ClassTag](ref: CompRef): RIO[Components with ComponentsEnv, T] =
      ZIO.accessM(_.get.load(ref))

    final def render(component: Component): RIO[Components with ComponentsEnv, String] =
      ZIO.accessM(_.get.render(component))
  }

}
