package pme123.zio.comps.zioconf

import pme123.zio.comps.core.test.ComponentsTests
import zio.stream.Sink
import zio.test.{DefaultRunnableSpec, suite}
import zio.{DefaultRuntime, UIO, ZIO}

object ZioconfSuites
  extends DefaultRunnableSpec(
    suite("ZioconfSuites")(
      ComponentsTests.testSuites(new ZioconfComps)
    ))

object ss extends App {
  println(new DefaultRuntime {}
    .unsafeRun(
      sumEvenNumbers(List(2, 4, 6))
    ))


  def sumEvenNumbers(nums: Iterable[Int]): UIO[Option[Int]] = {
    zio.stream.Stream.fromIterable(nums)
      .run(Sink.foldLeftM(0) { (acc: Int, b: Int) =>
        if (b % 2 == 0)
          ZIO.succeed(acc + b)
        else
          ZIO.fail(acc)
      }).fold(
      _ => None,
      v => Some(v)
    )
  }
}

import zio.ZIO
import zio.test.Assertion.equalTo
import zio.test._

object Ex {
  def sumEvenNumbers(as: Iterable[Int]): UIO[Option[Int]] =
    ZIO
      .foldLeft(as)(0)((s, a) => if (a % 2 == 0) ZIO.succeed(s + a) else ZIO.fail(s))
      .fold(
        _ => None,
        v => Some(v)
      )
}

object ExampleSpec extends DefaultRunnableSpec(

  testM("abort early in a fold") {
    assertM(Ex.sumEvenNumbers(List(2, 4, 6, 3, 5, 6)), equalTo(None))
    assertM(Ex.sumEvenNumbers(List(2, 4, 6,2)), equalTo(Some(14)))

  }
)