package io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild

import arrow.core.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild.PatternMatching.Color.*


fun main() {
    PatternMatching.run()
}

typealias Error = String
object PatternMatching {


    interface Thing
    data class Action(val value: String, val no: Int): Thing
    data class Button(val value: String, val no: Int): Thing

    data class Name(val name: String)
    data class Age(val age: Int)
    data class Person(val name: Name, val age: Age)

    enum class Color {
        Red,
        Green,
        Yellow
    }

    fun run() {
        // Option
        println("*****************OPTION START*************************")
        matchOptionString(Option("Hello"))
        matchOptionString(none())
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

    private fun matchOptionString(value: Option<String>) = when (value) {
        is Some -> println("matchOptionString: ${value.value}")
        None -> println("matchOptionString: empty")
    }

    private fun matchEither(value: Either<Error, String>) = when (value) {
        is Left -> println("matchEither: oh noes an error (${value.value})")
        is Right -> println("matchEither: ${value.value}")
    }

    private fun patternMatchSimple(value: Thing) = when (value) {
        Action("a", 2) -> println("its a2")
        else -> println("it's not a2")
    }

    private fun patternMatchMulti(value: Thing) = when {
        value is Action -> patternMatchSimple(value)
        value is Button && value.no == 3 -> println("its button 3")
        else -> println("it's a dud")
    }

    private fun patternMatchEnum(color: Color) = when(color) {
        Red -> println("It's red")
        Green -> println("It's green")
        Yellow -> println("It's yellow")
    }

//
}
