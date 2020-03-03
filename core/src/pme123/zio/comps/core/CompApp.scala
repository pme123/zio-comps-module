package pme123.zio.comps.core

import pme123.zio.comps.core.components.{Components, ComponentsEnv}
import pme123.zio.comps.core.components.Components.{load, render}
import zio.console.Console
import zio.{App, UIO, ZIO, console}

trait CompApp extends App {

  import CompApp._

  def program: ZIO[Console with Components, Nothing, Int] =
    flow.as(0)
      .catchAll { x =>
        console.putStrLn(s"Exception: $x") *>
          UIO.effectTotal(1)
      }
}

object CompApp {

  val dbLookupName = "postcodeLookup"
  val messageBundleName = "messageBundle.en"

  def flow: ZIO[Components with ComponentsEnv, Throwable, (DbLookup, DbConnection, MessageBundle)] = {
    for {
      dbLookup <- load[DbLookup](LocalRef(dbLookupName))
      dbConnection <- load[DbConnection](dbLookup.dbConRef)
      messageBundle <- load[MessageBundle](LocalRef(messageBundleName))
      _ <- render(dbLookup)
      _ <- render(dbConnection)
      _ <- render(messageBundle)
    } yield (dbLookup, dbConnection, messageBundle)
  }
}
