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

/**
 * 
 */
object SpecializationAbstractOverride {

  trait A[@specialized(Int) T] {                   def foo(t: T) }
  trait B extends A[Int]       {                   def foo(t: Int) { println("B.foo") } }
  trait M extends B            { abstract override def foo(t: Int) { super.foo(t) ; println ("M.foo") } }
  object C extends B with M 

  object D extends B           {          override def foo(t: Int) { super.foo(t); println("M.foo") } }
  
  def main(args: Array[String]) {
    D.foo(42) // OK, prints B.foo M.foo
    C.foo(42) // StackOverflowError
  }
}  
    /*

Exception in thread "main" java.lang.StackOverflowError
	at SpecializationAbstractOverride$M$class.foo$mcI$sp(SpecializationAbstractOverride.scala:34)
	at SpecializationAbstractOverride$C$.foo$mcI$sp(SpecializationAbstractOverride.scala:37)
	at SpecializationAbstractOverride$B$class.foo(SpecializationAbstractOverride.scala:30)
	at SpecializationAbstractOverride$C$.eu$kneissl$scalabugs$SpecializationAbstractOverride$M$$super$foo(SpecializationAbstractOverride.scala:37)
	at SpecializationAbstractOverride$M$class.foo$mcI$sp(SpecializationAbstractOverride.scala:34)
	at SpecializationAbstractOverride$C$.foo$mcI$sp(SpecializationAbstractOverride.scala:37)
	at SpecializationAbstractOverride$B$class.foo(SpecializationAbstractOverride.scala:30)
	at SpecializationAbstractOverride$C$.eu$kneissl$scalabugs$SpecializationAbstractOverride$M$$super$foo(SpecializationAbstractOverride.scala:37)

     */
