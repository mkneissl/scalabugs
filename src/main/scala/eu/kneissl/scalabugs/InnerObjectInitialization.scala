package eu.kneissl.scalabugs

import java.util.concurrent.atomic.AtomicInteger

/**
 * Test program to reproduce https://issues.scala-lang.org/browse/SI-1591 .
 */
object InnerObjectInitialization {
  val ai = new AtomicInteger

  class Holder {
    object Foo {
    /*
      Foo should be initialized only once per instance of Holder.
     */
      ai.incrementAndGet()
      def dummy() {}
    }
  }

  def main(args: Array[String]) {
    val holder = new Holder
    val threads = (1 to 100) map { i =>
      new Thread {
        override def run() { holder.Foo.dummy() }
      }
    }
    threads foreach { _.start() }
    threads foreach { _.join() }

    val scalaVersion = util.Properties.versionString
    if (ai.get() != 1) {
      println("Object initialization still broken in " + scalaVersion)
    } else {
      println("Object initialization bug not triggered in " + scalaVersion)
    }

  }
}