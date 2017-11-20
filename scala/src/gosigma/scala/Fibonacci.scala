package gosigma.scala

import scala.math.BigInt
import gosigma.study.system._

// http://www.scala-lang.org/api/2.10.2/index.html#scala.collection.immutable.Stream

object fibs {
  def main(args: Array[String]) {
    println("Hello Scala !!")
    // println(Info.getSystemInfo())
    // println(Info getSystemInfo)

    fibVariable();
    //fibVarNFunc();
    //    fibFuncRecurWrong();
    //    fibFactoryObject();
    //    fibFactoryClass();
  }

  // varibles here, will hook the object in memory all along the iteration!
  val fibsa: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibsa.zip(fibsa.tail).map { n => n._1 + n._2 }

  val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map(n => {
    println("Adding %d and %d".format(n._1, n._2))
    n._1 + n._2
  })

  def fibVariable(): Unit = {
    println("\n-- fibVariable(), using variables");
    fibsa take 5 foreach println

    fibs take 5 foreach println
    fibs take 6 foreach println

  }

  // it's val! immutable object
  val fib01: Stream[Int] = {
    var prev = 0;
    def loop(v: Int): Stream[Int] = {
      var rsl = prev + v;
      prev = v;
      return v #:: loop(rsl);
    }
    loop(1)
  }

  // it's function
  def fib02(): Stream[Int] = {
    var prev = 0;
    def loop(v: Int): Stream[Int] = {
      var rsl = prev + v;
      println("fib02 add " + prev + " and " + v);
      prev = v;
      return v #:: loop(rsl);
    }
    loop(1)
  }

  def fibVarNFunc(): Unit = {
    println("\n-- fibVarNFunc(), use var & func for fiboccina");
    fib01.take(10).foreach(x => println(x));
    println("fib01()");
    println(fib01.getClass().getName());
    println(fib01.toString());

    val fib = fib02();
    fib.take(10).foreach(x => println(x));
    println("fib02()");
    println(fib.getClass().getName());
    println(fib.toString());
  }

  def ffibsa(): Stream[BigInt] = {
    return BigInt(0) #:: BigInt(1) #:: ffibsa().zip(ffibsa().tail).map { n => n._1 + n._2 }
  }

  def ffibs(): Stream[BigInt] = {
    BigInt(0) #:: BigInt(1) #:: ffibs().zip(ffibs().tail).map(n => {
      println("Adding %d and %d".format(n._1, n._2))
      n._1 + n._2
    })
  }

  def fibFuncRecurWrong(): Unit = {
    println("\n-- fibFuncRecurWrong()");
    var fib = ffibsa();
    fib.take(10).foreach(x => println(x));
    println("ffibsa()");
    println(fib.getClass().getName());
    println(fib.toString());

    fib = ffibs();
    fib.take(10).foreach(x => println(x));
    println("ffibs()");
    println(fib.getClass().getName());
    println(fib.toString());

    println("not efficient!, too much recursive call for each ffibsx()");
  }

  // improve version
  def ffib(): Stream[BigInt] = {
    object factory {
      var fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map(n => {
        println("Adding %d and %d".format(n._1, n._2))
        n._1 + n._2
      })
      def get(): Stream[BigInt] = return fibs;
    }
    return factory.get();
  }

  def fibFactoryObject(): Unit = {
    println("\n-- fibFactoryObject(), using factory object");
    var fib = ffib();
    fib.take(5).foreach(x => println(x));
    println("ffib()");
    println(fib.getClass().getName());
    println(fib.toString());

    var fib2 = ffib();
    fib2.take(10).foreach(x => println(x));
    println("ffib2()");
    println(fib2.getClass().getName());
    println("fib2 : " + fib2.toString());
    println("fib  : " + fib.toString());
  }

  class FibFactory {
    var fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map(n => {
      println("Adding %d and %d".format(n._1, n._2))
      n._1 + n._2
    })

    def get(): Stream[BigInt] = return fibs;
  }

  def fibFactoryClass(): Unit = {
    println("\n-- fibFactoryClass(), using factory class");
    var fib = new FibFactory().get();
    fib.take(30).foreach(x => println(x));
    println("ffib()");
    println(fib.getClass().getName());
    println(fib.toString());
  }
}