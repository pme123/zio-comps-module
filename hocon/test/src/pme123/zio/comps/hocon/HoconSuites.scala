package pme123.zio.comps.hocon

import pme123.zio.comps.core.Components
import pme123.zio.comps.core.Components.ComponentsEnv
import pme123.zio.comps.core.test.ComponentsTests
import zio.console.Console
import zio.test.{DefaultRunnableSpec, suite}

object HoconSuites
  extends DefaultRunnableSpec(
    suite("HoconSuites")(
      ComponentsTests.testSuites(
        new Console.Live with Components.Live {
          def compsService: Components.Service[ComponentsEnv] = new HoconComps
        })
    ))
