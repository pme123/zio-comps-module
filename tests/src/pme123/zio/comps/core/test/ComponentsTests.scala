package pme123.zio.comps.core.test

import pme123.zio.comps.core.CompApp._
import pme123.zio.comps.core._
import zio.test._
import zio.test.Assertion._

object ComponentsTests {

  //noinspection TypeAnnotation
  def testSuites(env: AppEnv) =
    suite("Run Program")(
      testM("the Configs are correct") {
        for {
          r <- CompApp.flow.provide(env)
          (lookup, conn, messageB) = r
        } yield assert(lookup, equalTo(dbLookup)) &&
          assert(conn, equalTo(dbConnection)) &&
          assert(messageB, equalTo(messageBundle))
      }
    )

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
