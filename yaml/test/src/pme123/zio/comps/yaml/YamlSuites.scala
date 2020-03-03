package pme123.zio.comps.yaml

import pme123.zio.comps.core.test.ComponentsTests
import zio.test._


object YamlSuites
  extends DefaultRunnableSpec {
  def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("YamlSuites")(
      ComponentsTests.testSuites(new YamlComps)
    )
}
