package gosigma.scala

object lambda {
  def main(args: Array[String]): Unit = {
    val l01 = (x: Int) => { println("l01 - " + x); x + 1; }
    val l02 = (x: Int) => { println("l02 - " + x); x + 2; }
    //    val l03 = (x: Int) => { println("l02 - " + x); return x + 3; }
    //    val l04 = (x: Int) => { println("l02 - " + x); return x + 4 }

    println(l01(4));
    println(l02(4));
    //    println(l03(4));
    //    println(l04(4));

    println("foo : " + foo())
    println("foo2 : " + foo2())

    // https://tpolecat.github.io/2014/05/09/return.html
    // Don't Use Return in Scala
    nonLocalReturn
    nonLocalReturn02
  }

  def foo(): Int = {
    val sumR: List[Int] => Int = _.foldLeft(0)((n, m) => return n + m)
    sumR(List(1, 2, 3)) + sumR(List(4, 5, 6))
  }

  def foo2(): Int = {
    val sumR: List[Int] => Int = _.foldLeft(0)((n, m) => n + m)
    sumR(List(1, 2, 3)) + sumR(List(4, 5, 6))
  }

  def lazily(s: => String): String =
    try s catch { case t: Throwable => t.toString }

  def nonLocalReturn(): Unit = {
    println("\nnonLocalReturn() non local return")
    def foo: String = lazily("foo")
    def bar: String = lazily(return "bar")

    println("foo : " + foo);
    println("bar : " + bar);
  }

  def nonLocalReturn02(): Unit = {
    println("\nnonLocalReturn02() non local return")
    def x: Int = {
      val a: Int = return 2
      println("inside x, a : " + a);
      1
    } // result is 2
    println("x : " + x);

    def y: Int = {
      val a: String = return 2
      println("inside y, a : " + a);
      1
    }
    println("y : " + y);

    def z: Int = {
      val a: Nothing = return 2
      println("inside y, a : " + a);
      1
    }
    println("z : " + z);

    def p: Int = {
      val a = return 2
      println("inside y, a : " + a);
      1
    }
    println("p : " + p);
  }
}