package gosigma.scala

import scala.reflect.runtime.{ universe => ru }

// 1 - the imports
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

object syntax {
  def main(args: Array[String]): Unit = {
    println("hello")
    val a = { 2 > 8 }
    println("a : " + a + " , type " + a.getClass().getName())
    val b = { println("inside val b"); 2 > 8; }
    println("b : " + b + " , type " + b.getClass().getName())

    println("func01 : " + func01())
    println("func02 : " + func02())

    test01();
    test02();
    test03();
  }

  def func01(): String = {
    return "func01()"
  }

  def func02(): String = {
    "func02()"
  }

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
  def test01() = {
    val list01 = for (x <- 1 to 7) yield { s"Day $x:" }
    println("list01's type : " + list01.getClass.getName);
    println("list01 scala type : " + getTypeTag(list01).tpe.toString())

    val future = Future { "hello" }
    val list02 = for (x <- future) yield { s"Day $x:" }
    println("list02 scala type : " + getTypeTag(list02).tpe.toString())
    list02 onComplete {
      case Success(e) => println("Success")
      case Failure(e) => println("Failure")
    }
  }

  def test02() = {
    def run(f: Int => Int) = f(10)
    println("run x+10 : " + run(x => x + 10))
    println("run x+10 : " + run { x => x + 10 })
    val a = (x: Int) => x + 10
    println("a scala type : " + getTypeTag(a).tpe.toString())
    println("{x=>x+10} scala type : " + getTypeTag({ x: Int => x + 10 }).tpe.toString())

    // difference between f:=>Int and f:()=>Int
    def run2(f: => Int) = f
    def func(): Int = 10
    println("run2 {} : " + run2(func))
    println("run2 {} : " + run2 { 3 + 10 })
    println("run2 {} : " + run2 { println("do something, then return int : "); 3 + 10 })

    def run3(f: => Int) = { y: Int => y + f }
    val func3 = run3 { println("provide 99"); 99 }
    println("func3() is : " + func3(99))

    // difference between f:=>Int and f:()=>Int
    def run4(f: () => Int) = f
    val func4 = { () => println("func4 anonymous block"); 99 }
    println("func4 scala type : " + getTypeTag(func4).tpe.toString())
    func4()
    println("run4(func4) : " + run4 { func4 })
    println("run4(func4)() : " + run4 { func4 }())
    println("run4{ xxxx } : " + run4 { println("run4{xxx}"); () => 99 })
    println("run4{ xxxx }() : " + run4 { println("run4{xxx}"); () => 99 }())

  }

  def test03() = {
    implicit val v: Int = 42 
    def g(x: Int, y: Int)(implicit base: Int): Int = (x + y) % base
    println("g : " + g(3,3)(23))
    println("g(x,y) scala type : " + getTypeTag(g(_, _)).tpe.toString())

    def h(x: Int, y: Int): Int = (x + y) % 10
    println("h(x,y) scala type : " + getTypeTag(h(_, _)).tpe.toString())

    // function return function, composite
    def i(x: Int, y: Int)(base: Int): Int = (x + y) % base
    println("i(x,y) scala type : " + getTypeTag(i(_, _)).tpe.toString())

    def lift(ls: List[Int]) = ls map { x => Option(x) }
    println("lift(_) type : " + getTypeTag(lift(_)).tpe.toString());
    
    // use context sensitve prompt
    def func01(i:Int)(j:Int) = i * j
    var x2 = func01(2)(_)
    var y2 = func01(_)
    
  }

}