package gosigma.scala

import scala.reflect.runtime.{ universe => ru }

object generic_app extends App {
  class Stack[T] {
    var elems: List[T] = Nil
    def push(x: T) { elems = x :: elems }
    def top: T = elems.head
    def pop() { elems = elems.tail }
  }

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
  //relfect to
  // println("Stack type : " + getTypeTag(Stack)).tpe.toString());
  // println("Stack[_] type : " + getTypeTag(Stack[_])).tpe.toString())
  // println("Stack[Int] type : " + getTypeTag(Stack[Int])).tpe.toString()))
  
  object StockSymbols extends Enumeration {
    type StockSymbols = Value
    val SUN, CSCO, INTL, GOGL, IBM = Value
  }
  override def main(args: Array[String]) = {
    println("SUN : " + StockSymbols.SUN.getClass + ", " + StockSymbols.SUN.hashCode());
    println("INTL : " + StockSymbols.INTL.getClass + ", " + StockSymbols.INTL.hashCode());
    println("IBM : " + StockSymbols.IBM.getClass + ", " + StockSymbols.IBM.hashCode());

  }
}