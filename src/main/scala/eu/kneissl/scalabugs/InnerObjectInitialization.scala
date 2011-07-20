/*
  Copyright 2011 Martin Kneissl

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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