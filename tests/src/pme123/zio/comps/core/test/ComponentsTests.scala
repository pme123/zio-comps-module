package pme123.zio.comps.core.test

import pme123.zio.comps.core.CompApp._
import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core._
import zio.Ref
import zio.test.Assertion._
import zio.test._
import zio.test.environment.TestConsole
import zio.test.environment.TestConsole.Data

object ComponentsTests {

  def env(service: Components.Service[ComponentsEnv], data: Ref[TestConsole.Data]): AppEnv =
    new TestConsole with Components.Live {
      def compsService: Components.Service[ComponentsEnv] = service

      val console: TestConsole.Service[Any] = TestConsole.Test(data)
    }

  //noinspection TypeAnnotation
  def testSuites(service: Components.Service[ComponentsEnv]) =
    suite("Run Program")(
      testM("the Configs are correct") {
        for {
          consoleData <- Ref.make[TestConsole.Data](Data())
          r <- CompApp.flow.provide(env(service, consoleData))
          (lookup, conn, messageB) = r
          content <- consoleData.get
          consoleOut = content.output.filter(_.startsWith(renderOutputPrefix))
        } yield
          assert(consoleOut.length, equalTo(3)) &&
            testRenderLookup(lookup, consoleOut.head) &&
            testDbConn(conn, consoleOut(1)) &&
            testMessageBundle(messageB, consoleOut.last)
      }
    )

  private def testRenderLookup(lookup: DbLookup, output: String) = {
    assert(lookup, equalTo(dbLookup)) &&
      assert(output,
        containsString(dbLookup.name) &&
          containsString(dbLookup.dbConRef.url)
      )
  }

  private def testDbConn(conn: DbConnection, output: String) = {
    assert(conn, equalTo(dbConnection)) &&
      assert(output,
        containsString(dbConnection.name) &&
          containsString(dbConnection.url) &&
          containsString(dbConnection.user) &&
          containsString(dbConnection.password.value)
      )
  }

  private def testMessageBundle(messageB: MessageBundle, output: String) = {
    assert(messageB, equalTo(messageBundle)) &&
      assert(output,
        containsString(messageBundle.name)
      )
  }

  private val dbLookup = DbLookup(
    dbLookupName,
    LocalRef("odsDb"),
    """|
       |SELECT f_postcode FROM t_places
       |    WHERE f_name == ?
""".stripMargin,
    Map("name" -> "Schwändi")
  )

  private val dbConnection = DbConnection(
    "odsDb",
    "jdbc:h2:mem:testdb",
    "sa",
    Sensitive("This is a Secret and should not be seen!")
  )

  private val messageBundle = MessageBundle(
    "messageBundle",
    Map(
      "language" -> "German",
      "email-subject" -> "Welcome Email from SuperComp AG.",
      "email-body" ->
        """|
           |Dear {{gender}} {{name}}
           |
           |We welcome you to be part of our community!
           |
           |Regards
           |   SuperComp AG""".stripMargin

    )
  )
}
