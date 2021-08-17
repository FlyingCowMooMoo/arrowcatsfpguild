package io.github.flyingcowmoomoo.arrowcatsfpguild.scala

import cats.data.EitherT
import cats.effect.IO
import cats.effect.unsafe.implicits.global

object EffectsSimple {

  def main(args: Array[String]): Unit = { // The edge of our world
    val result = ioProgram().unsafeRunSync()
    println(result)
  }

  case class Id(id: Long)

  case class PersistenceError()

  case class User(email: String, name: String)

  case class ProcessedUser(id: Id, email: String, name: String)


  def fetchUser(): IO[Either[PersistenceError, User]] = {
    IO(Right(User("test@email.com", "Human")))
  }

  implicit class UserHelpers(val user: User) {
    def process(): IO[Either[PersistenceError, ProcessedUser]] = {
      if (user.email.matches("^(.+)@(.+)$")) {
        IO(Right(ProcessedUser(Id(1), user.email, user.name)))
      }
      else IO(Left(PersistenceError()))
    }
  }


  def ioProgram(): IO[Either[PersistenceError, ProcessedUser]] = {
    val result = for {
      user <- EitherT(fetchUser())
      processed <- EitherT(user.process())
    } yield processed
    result.value
  }
}
