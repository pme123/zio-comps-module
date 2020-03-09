package pme123.zio.comps.core

import pme123.zio.comps.core.components.Components.{load, render}
import pme123.zio.comps.core.components.{Components, ComponentsEnv}
import zio.{App, ZIO, console}

trait CompApp extends App {

  import CompApp._

  def program: ZIO[AppEnv, Nothing, Int] =
    flow.as(0)
      .catchAll(x => console.putStrLn(s"Exception: $x").as(1))
}

object CompApp {

  type AppEnv = Components with ComponentsEnv
  val dbLookupName = "postcodeLookup"
  val messageBundleName = "messageBundle.en"

  def flow: ZIO[AppEnv, Throwable, (DbLookup, DbConnection, MessageBundle)] = {
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
