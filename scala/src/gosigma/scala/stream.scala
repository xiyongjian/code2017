//package gosigma.scala
//
//import gosigma.Util
//
//object stream {
//  def main(args: Array[String]) {
//    //    def foo(x: Int): Stream[Int] = { println("came inside"); (2 * x) } #:: foo(x + 1)
//    //    foo(1).takeWhile(_ < 6).toList.foreach(x => println(s"+++ $x"))
//    //
//    //    foo(1).takeWhile(_ < 6).foreach(x => println(s"+++ $x"))
//    //
//    //    def river(x: Int): Stream[Int] = {
//    //      println("Inside function.")
//    //      (2 * x) #:: river(x + 1)
//    //    }
//    //
//    //    lazy val creek = river(1)
//    //    creek.takeWhile(_ < 6).foreach(x => println(s"+++ $x"))
//    //
//    //    val prop = System.getProperties()
//    //    prop.entrySet().forEach(x => println(x.getKey() + " - " + x.getValue()))
//    //
//    //    val stream = 1 #:: 2 #:: 3 #:: Stream.empty
//    //    stream.foreach(println)
//
//    try {
//      //    test01()
//      //    test02()
//      // test03()
//      notUseReturn()
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//
//    println("all done.")
//  }
//
//  def test01() {
//    println("\ntest01()")
//    lazy val fib: Stream[Int] = {
//      def loop(h: Int, n: Int): Stream[Int] = h #:: loop(n, h + n)
//      loop(1, 1)
//    }
//
//    fib take 10 foreach println
//    fib take 15 foreach println
//  }
//
//  def test02(): Unit = {
//    println("\ntest02()")
//    var mem = Util.pmem()
//    println("sum : " + Stream.range(1, 1000).reduce((x, y) => x + y))
//    // println("max : " + Stream.range(1, 1000000).reduce(_ max _))
//    // println("min : " + Stream.range(1, 1000000).reduce(_ min _))
//    mem = Util.pmem(mem)
//
//    val it = Stream.range(1, 10000).iterator
//    var m: Int = 0
//    while (it.hasNext) {
//      val i: Int = it.next()
//      if (m < i)
//        m = i
//    }
//    println("max : " + m)
//    mem = Util.pmem(mem)
//  }
//
//  def ints(): Stream[Int] = {
//    def loop(v: Int): Stream[Int] = v #:: loop(v + 1)
//    loop(0)
//  }
//
//  def test03(): Unit = {
//    println("\ntest03()")
//    System.gc()
//    var mem = Util.pmem()
//    println("max : " + ints().take(100000).reduce(_ max _))
//    mem = Util.pmem(mem)
//    println("after gc")
//    System.gc()
//    mem = Util.pmem(mem)
//  }
//
//  def notUseReturn(): Unit = {
//    println("\nnotUseReturn()")
//    System.gc()
//    var mem = Util.pmem()
//    println("after gc")
//    System.gc()
//    mem = Util.pmem(mem)
//    println("max : " + ints().take(100000).reduce(_ max _))
//
//    println("max range : " + Stream.range(1, 10000).filter(x => true).foreach(x => x))
//    Stream.range(1, 100000).foreach(x => {
//      if ((x % 10000) == 0) {
//        System.gc()
//        print(x + " - ")
//        Util.pmem()
//      }
//    })
//
//    println("try to reduce rsl : ")
//    val rsl = ints().take(5).toList.filter((x) => {
//      println("hello")
//      x > 0
//    })
//      .foreach(x => {
//        println(x)
//      })
//    println("rsl : " + rsl)
//
//    val r2 = ints().take(1000000).filter(x => {
//      if ((x % 100000) == 0) {
//        System.gc()
//        print(x + " filter - ")
//        Util.pmem()
//      }
//      x > 0
//    // }).reduce(_ max _)
//    }).reduce((x,y) => {
//      if ((x % 100000) == 0) {
//        System.gc()
//        print(x + " reduce - ")
//        Util.pmem()
//      }
//      if (x > y) x else y
//    });
//    println("r2 : " + r2)
//
//    val r3 = ints().take(1000000).filter(func_filter).reduce(_ max _)
//    println("r3 : " + r3)
//
//    mem = Util.pmem(mem)
//    println("after gc")
//    System.gc()
//    mem = Util.pmem(mem)
//  }
//
//  def func_filter(x: Int): Boolean = {
//    if ((x % 100000) == 0) {
//      System.gc()
//      print(x + " - ")
//      Util.pmem()
//    }
//    return x > 0
//  }
//}