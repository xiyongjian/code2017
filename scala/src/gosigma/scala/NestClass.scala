package gosigma.scala

class A {
  class B

  def f(b: B) = println("Got my B!")

  def g(b: A#B) = println("Got a B.")

}

object NestClass {
  def main(args: Array[String]) {
    val a1 = new A;
    val a2 = new A;
    // a2.f(new a1.B);
    a2.g(new a1.B);
  }

}