package gosigma.scala

import scala.reflect.runtime.{ universe => ru }

object currying_app {
  def process[A](filter: A => Boolean)(list: List[A]): List[A] = {
    lazy val recurse = process(filter) _

    list match {
      case head :: tail => if (filter(head)) {
        head :: recurse(tail)
      } else {
        recurse(tail)
      }

      case Nil => Nil
    }
  }

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]

  val even = (a: Int) => a % 2 == 0
  val processEven = process(even) _
  println("processEven type(java) : " + processEven.getClass.getName)
  //relfect to
  println("processEven(_) type(scala) : " + getTypeTag(processEven(_)).tpe.toString());

  val numAsc = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
  val numDesc = 5 :: 4 :: 3 :: 2 :: 1 :: Nil

  println("numAsc : " + processEven(numAsc).toString())
  println("numDesc : " + processEven(numDesc).toString())

  def lift(ls:List[Int]) = ls map { x=>Option(x) }
  
  def findElem(e:Int, ls:List[Option[Int]]):Boolean = ls match {
    case Nil => false
    case x::xs => if (e == x.get) true else findElem(e, xs)
  }
  
  val findCurrying = (findElem _).curried;
  println("findCurrying(_) type(scala) : " + getTypeTag(findCurrying(_)).tpe.toString());
  
  val findCompose = findCurrying(3)
  
  def main(args: Array[String]) = {

  }
}