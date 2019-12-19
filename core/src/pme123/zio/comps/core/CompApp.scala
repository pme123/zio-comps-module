package pme123.zio.comps.core

import pme123.zio.comps.core.Components.>.{load, render}
import pme123.zio.comps.core.Components.ComponentsEnv
import zio.{App, UIO, ZIO, console}

trait CompApp extends App {

  import CompApp._

  def program: ZIO[AppEnv, Nothing, Int] =
    flow
      .map(_ => 0)
      .catchAll { x =>
        console.putStrLn(s"Exception: $x") *>
          UIO.effectTotal(1)
      }
}

object CompApp {

  val dbLookupName = "postcodeLookup"
  val messageBundleName = "messageBundle.en"

  type AppEnv = Components with ComponentsEnv

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
