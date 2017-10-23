package gosigma.scala

object syntax {
  def main(args: Array[String]): Unit = {
    println("hello")
    val a = { 2 > 8 }
    println("a : " + a + " , type " + a.getClass().getName())
    val b = { println("inside val b"); 2 > 8; }
    println("b : " + b + " , type " + b.getClass().getName())
    
    println("func01 : " + func01())
    println("func02 : " + func02())
  }
  
  def func01() : String = {
    return "func01()"
  }

  def func02() : String = {
    "func02()"
  }

}