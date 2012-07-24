package scala.impatient

import scala.runtime._

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises17._

@RunWith(classOf[JUnitRunner])
class Exercises17Spec extends KoanSpec("Specs for Chapter 17"){
  "(17.1-3)" should {
    "be immutable and swap" in {
      val first = new ImmutablePair("Any", 12)
      val second = first.swap
      (first eq second) should not be (true)
      first.first should be (second.second)
      first.second should be (second.first)
    }
    "be mutable and swap" in {
      val pair = new MutablePair("Black", "White")
      pair.first should be ("Black")
      pair.second should be ("White")
      pair.swap()
      pair.first should be ("White")
      pair.second should be ("Black")
    }
    "be a visitor and swap" in {
      val first = new Pair("Any", 12)
      val second = first.swap(first)
      (first eq second) should not be (true)
      first.first should be (second.second)
      first.second should be (second.first)
    }
  }
  "(17.4)" should {
    "not need a lower bound if we replace a Pair[Worker] with an Employee" in {
      import org.functionalkoans.forscala._
      val worker1 = new Worker("alan", "wong")
      val worker2 = new Worker("conan", "obrien")
      val emp1 = new Employee("alan", "wong", 123)
      emp1.isInstanceOf[Worker] should be (true)
      worker1.isInstanceOf[Employee] should be (false)
      
      val initialPair = new MutablePair(worker1, worker2)
      initialPair.first.isInstanceOf[Worker] should be (true)
      initialPair.second.isInstanceOf[Worker] should be (true)
      
      val workerPair = initialPair.replaceFirst(emp1)
      workerPair.first.isInstanceOf[Worker] should be (true)
      workerPair.second.isInstanceOf[Worker] should be (true)
      workerPair.first.isInstanceOf[Employee] should be (true)
      workerPair.second.isInstanceOf[Employee] should be (false)
      // this next replacement occurs due to type erasure
      val anyPair = initialPair.replaceFirstWithoutLB(emp1)
      anyPair.first.isInstanceOf[Employee] should be (true)
      anyPair.second.isInstanceOf[Worker] should be (true)
      
    }
  }
  "(17.5)" should {
    "examine why RichInt implements Comparable[Int] instead of Comparable[RichInt]" in {
      val int = 20
      val richInt: RichInt = int
      val comparableOfInt: Comparable[Int] = richInt
      // next line below won't compile
      // val comparableOfRichInt: Comparable[RichInt] = richInt
      println("""RichInt is a conversion through an implicit from Int at runtime. One could easily
                |have a Seq[Int], some of which have been coerced to RichInt, and need to use 
                |the Comparable interfaces over the entire collection. Comparable[Int] is the common
                |type in this mixed scenario""") 
    }
    "(17.6-10)" should {
      "find middle for (17.6)" in {
        Exercises17.middle("world").get should be ('r')
        Exercises17.middle("") should be (None)
      }
      "explain why Iterable[+A] uses covariant A for (17.7)" in {
        val iterable: Iterable[String] = List("hello", "world")
        
        iterable.foldLeft(List[String]()) { (s,c) => c :: s }.size should be(iterable.size)
        println("because A defines the upper bound of methods which must consume A")
      }
      "explain in (17.8)" in {
        val pair = new Pair(1, "awong")      // Pair[Int,String]
        val next = pair.replaceFirst(Some("where"))   // Pair[Any,String]
        println("the lower bound restriction is needed for the product of the method")
      }
    }
  }

}