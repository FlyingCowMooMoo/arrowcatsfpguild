@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.computations.either
import io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild.EffectsSimple.ioProgram
import kotlinx.coroutines.*

fun main(): Unit = runBlocking { // The edge of our world
    println(ioProgram())
}

object EffectsSimple {

    class Id(val id: Long)

    object PersistenceError

    data class User(val email: String, val name: String)

    data class ProcessedUser(val id: Id, val email: String, val name: String)

    suspend fun fetchUser(): Either<PersistenceError, User> =
        Right(User("test@email.com", "Human"))

    suspend fun User.process(): Either<PersistenceError, ProcessedUser> =
        when {
            email.contains(Regex("^(.+)@(.+)$")) -> Right(ProcessedUser(Id(1), email, name))
            else -> Left(PersistenceError)
        }

    suspend fun ioProgram(): Either<PersistenceError, ProcessedUser> =
        either {
            val user = fetchUser().bind()
            val processed = user.process().bind()
            processed
        }
}