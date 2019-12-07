package pme123.zio.comps.yaml

import io.circe.{Decoder, Encoder}
import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core.{Component, _}
import zio.console.Console
import zio.{RIO, Task, ZIO, console}
import cats.syntax.functor._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.yaml.parser
import io.circe.yaml.syntax._
import io.circe.{Decoder, Encoder, Json, ParsingFailure}

import scala.io.Source
import scala.reflect.ClassTag

trait YamlComps extends Components {

  implicit val sensitive: Decoder[Sensitive] =
    Decoder[String].map(Sensitive).widen


  implicit val decodeCompRef: Decoder[CompRef] =
    List[Decoder[CompRef]](
      Decoder[LocalRef].widen,
      Decoder[RemoteRef].widen
    ).reduceLeft(_ or _)

  def loadYaml[T <: Component : Decoder](ref: CompRef): RIO[Console, T] = {
    val yamlString = Source.fromResource(s"${ref.url}.yaml").mkString
    val json: Either[ParsingFailure, Json] = parser.parse(yamlString)
    for {
      _ <- console.putStrLn(s"\nJSON:\n$json")
      comp = json.flatMap(_.as[T])
      component <- Task.fromEither(comp)
      _ <- zio.console.putStrLn(s"\nComponent:\n$component")
    } yield component
  }

  def renderYaml[T <: Component : Encoder](
                                            component: T
                                          ): RIO[Console, String] =
    for {
      json <- ZIO.effectTotal(component.asJson)
      configString <- ZIO.effectTotal(json.asYaml.spaces2)
      _ <- console.putStrLn(
        s"\nComponent File ${component.name}.conf :\n$configString"
      )
    } yield configString

  val components: Components.Service[ComponentsEnv] = new Components.Service[ComponentsEnv] {

    implicit val decodeComponent: Decoder[Component] =
      List[Decoder[Component]](
        Decoder[DbConnection].widen,
        Decoder[DbLookup].widen,
        Decoder[MessageBundle].widen
      ).reduceLeft(_ or _)

    def load[T <: Component](ref: CompRef)(implicit classTag: ClassTag[T]): RIO[ComponentsEnv, T] = {
      ??? //loadYaml[T] (ref)
    }

    def render[T <: Component](component: T)(implicit classTag: ClassTag[T]): RIO[ComponentsEnv, String] =
      ??? //    renderYaml[T](component)
  }
}


