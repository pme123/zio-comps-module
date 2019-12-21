package pme123.zio.comps.hocon

import pme123.zio.comps.core.test.ComponentsTests
import zio.test._

object HoconSuites
  extends DefaultRunnableSpec(
    suite("HoconSuites")(
      ComponentsTests.testSuites(new HoconComps)
    ))
