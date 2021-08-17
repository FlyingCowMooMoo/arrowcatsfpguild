package io.github.flyingcowmoomoo.arrowcatsfpguild.scala

object PatternMatching {

  sealed trait Thing

  case class Action(value: String, no: Int) extends Thing

  case class Button(value: String, no: Int) extends Thing

  type Error = String

  sealed trait Color
  case object Red extends Color
  case object Green extends Color
  case object Yellow extends Color


  def main(args: Array[String]): Unit = {
    run()
  }

  def run(): Unit = {

    // Option
    println("*****************OPTION START*************************")
    matchOptionString(Option("Hello"))
    matchOptionString(Option.empty)
    println("*****************OPTION END*************************")

    //Either
    println("*****************EITHER START*************************")
    matchEither(Right("Hello"))
    matchEither(Left("errorMsg"))
    println("*****************EITHER END*************************")

    //Pattern Match
    println("*****************PATTERN MATCH START*************************")
    patternMatchSimple(Action("a", 2))
    patternMatchSimple(Button("a", 3))
    patternMatchSimple(Action("b", 2))
    println("*****************PATTERN MATCH END*************************")

    //Pattern Match Multi
    println("*****************PATTERN MULTI START*************************")
    patternMatchMulti(Action("a", 2))
    patternMatchMulti(Button("a", 3))
    patternMatchMulti(Button("a", 2))
    println("*****************PATTERN MULTI END*************************")

    //Pattern Match Enum
    println("*****************PATTERN MULTI START*************************")
    patternMatchEnum(Red)
    patternMatchEnum(Yellow)
    patternMatchEnum(Green)
    println("*****************PATTERN MULTI END*************************")

  }

  private def matchOptionString(value: Option[String]): Unit = {
    value match {
      case Some(s) => println(s"matchOptionString: $s")
      case None => println("matchOptionString: empty")
    }
  }

  private def matchEither(value: Either[Error, String]): Unit = {
    value match {
      case Left(s) => println(s"matchEither: oh noes an error ($s)")
      case Right(s) => println(s"matchEither: $s")
    }
  }

  private def patternMatchSimple(value: Thing): Unit = {
    value match {
      case Action("a", 2) => println("its a2")
      case _ => println("it's not a2")
    }
  }

  private def patternMatchMulti(value: Thing): Unit = {
    value match {
      case Action(_, _) => patternMatchSimple(value)
      case Button(_, 3) => println("its button 3")
      case _ => println("it's a dud")
    }
  }

  case class Name(name: String)
  case class Age(age: Int)
  case class Person(name: Name, age: Age)


  private def patternMatchEnum(color: Color): Unit = {
    color match {
      case Red => println("It's red")
      case Green => println("It's green")
      case Yellow => println("It's yellow")
    }
  }




}
