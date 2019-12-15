package pme123.zio.comps.yaml

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.yaml.parser
import io.circe.yaml.syntax._
import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core.{Component, _}
import zio.console.Console
import zio.{RIO, Task, ZIO, console}

import scala.io.{Codec, Source}
import scala.reflect.ClassTag

class YamlComps extends Components.Service[ComponentsEnv] {

  def load[T <: Component: ClassTag](ref: CompRef): RIO[ComponentsEnv, T] = {
    loadYaml[Component](ref).map { case c: T => c }
  }

  def render(component: Component): RIO[ComponentsEnv, String] =
    renderYaml(component)


  implicit val componentDecoder: Decoder[Component] =
    List[Decoder[Component]](
      Decoder[DbConnection].widen,
      Decoder[DbLookup].widen,
      Decoder[MessageBundle].widen
    ).reduceLeft(_ or _)

  implicit val sensitiveDecoder: Decoder[Sensitive] =
    Decoder[String].map(Sensitive).widen

  implicit val compRefDecoder: Decoder[CompRef] =
    List[Decoder[CompRef]](
      Decoder[LocalRef].widen,
      Decoder[RemoteRef].widen
    ).reduceLeft(_ or _)

  private def loadYaml[T <: Component : Decoder](ref: CompRef): RIO[Console, T] = {
    for {
      yamlString <- Task.effect(Source.fromResource(s"${ref.url}.yaml")(Codec.UTF8).mkString)
      json <- Task.fromEither(parser.parse(yamlString))
      _ <- console.putStrLn(s"\nJSON:\n$json")
      comp = json.as[T]
      component <- Task.fromEither(comp)
      _ <- zio.console.putStrLn(s"\nComponent:\n$component")
    } yield component
  }

  private def renderYaml(
                  component: Component
                ): RIO[Console, String] =
    for {
      json <- ZIO.effectTotal(component.asJson)
      configString <- ZIO.effectTotal(json.asYaml.spaces2)
      _ <- console.putStrLn(
        s"\nComponent File ${component.name}.conf :\n$configString"
      )
    } yield configString

}


