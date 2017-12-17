package gosigma.scala

import scala.reflect.runtime.{ universe => ru }

// This is Scala
class Person(val firstName: String, val lastName: String, val age: Int) {
  override def toString = "[Person: firstName=" + firstName + " lastName=" + lastName +
    " age=" + age + "]"
}

class Student(firstName: String, lastName: String, age: Int)
  extends Person(firstName, lastName, age) {
  def doSomething = {
    System.out.println("I'm studying hard, Ma, I swear! (Pass the beer, guys!)")
  }
}

object class_app extends App {
  override def main(args: Array[String]) = {
    println("hello, world");
    val person = new Person("james", "xi", 46)
    println("person class : " + person.getClass.getName)

    val student = new Student("james", "xi", 46)
    println("student class : " + student.getClass.getName)

    type Callback[T] = Function1[T, Unit]
    val func: Callback[Int] = y => println(y + 2)
    println("func class : " + func.getClass.getName)

    val func2 = (x: Int) => x + 1
    println("func2 class : " + func2.getClass.getName)

    val filter = (predicate: Int => Boolean, xs: List[Int]) => {
      for (x <- xs; if predicate(x)) yield x
    }
    println("filter class : " + filter.getClass.getName)
    println("filter class : " + filter.getClass.getSuperclass.getName)

    type Xtuple = (Double, String)
    val x: Xtuple = (1.0, "hello")
    println("x : " + x.toString());

    funcCompose()

    testPartial()
  }

  def funcCompose() = {
    println("---- funcCompose() ----");
    println("option(1)'s type : " + Option(1).getClass().getName);

    def lift(ls: List[Int]) = ls map { x => Option(x) }
    def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
    //relfect to
    println("lift(_) type : " + getTypeTag(lift(_)).tpe.toString());

    println("List type : " + getTypeTag(List).tpe.toString());
    //  println("List[_] type : " + getTypeTag(List[_]).tpe.toString());

  }

    def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
  def testPartial() = {
    val divide: PartialFunction[Int, Int] = {
      case d: Int if d != 0 => 42 / d
    }
    val receive: PartialFunction[Any, Unit] = {
      case i: Int => println("get Int : " + i)
      case s: String => println("get String : " + s)
      // case a: Any => println("unsupported : " + getTypeTag(a).tpe.toString());
      // case _ => println("unsupported")
      case other => println("unsupported : " + other.toString() + ", " + other.getClass().getName)
    }
    
    receive(1)
    receive("hello")
    receive(divide)
    receive(receive)
  }
}