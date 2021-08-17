package io.github.flyingcowmoomoo.arrowcatsfpguild.scala

import cats.{Applicative, Monad}
import cats.effect.{Async, IO, IOApp}
import cats.effect.unsafe.implicits.global
import cats.implicits._

import java.util.concurrent.atomic.AtomicLong
import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext
import scala.math.BigDecimal


object Effects{

  def main(args: Array[String]): Unit = {
    exec()
  }
  def exec(): Unit = {

    val productRepoIO: RepositoryAlgebra[IO] = new MyRepository[IO](new AtomicLong())

    val programIO = new MyProgram(productRepoIO)



    val finalProgram: IO[(Option[Product], Option[Product], Option[Product])] = for {
      createInvalidPrice <- programIO.createProduct("Test 1", 666)
      createValidPrice <- programIO.createProduct("Test 2", 1337)
      // Dirty below, can throw a NoSuchElementException
      getCreateValidPrice <- programIO.getProduct(createValidPrice.get.id.get)
    } yield (createInvalidPrice, createValidPrice, getCreateValidPrice)

    implicit val context: IO[ExecutionContext] =  Async[IO].executionContext

//    val parallelProgram = (
//      programIO.createProduct("Test 1", 666),
//      programIO.createProduct("Test 2", 1337)
//      ).parMapN((a, b) => (a, b, programIO.getProduct(b.get.id.get)))

    val syncResult = finalProgram.unsafeRunSync()
//    val parallelResult = parallelProgram.unsafeRunSync()
    val parallelResult = None

    val result = Map("syncResult" -> syncResult, "parallelResult" -> parallelResult)

    println(result)
  }

  class MyProgram[F[_]: Monad](repo: RepositoryAlgebra[F]) {
    def renameProduct(id: Long, title: String): F[Option[Product]] = repo
      .get(id)
      .flatMap {
        case Some(product) =>
          val renamed = product.copy(title = title)
          repo.update(renamed) map (_ => Option(renamed))
        case None =>
          Monad[F].pure(None)
      }

    def getProduct(id: Long): F[Option[Product]] = repo.get(id)

    def createProduct(title: String, price: BigDecimal): F[Option[Product]] = {
      if (price.equals(BigDecimal(666))) {
        Monad[F].pure(None)
      }
      else {
        repo.create(Product(None, title, price)) map (it => Option(it))
      }
    }

    def deleteProduct(id: Long): F[Option[Product]] = repo.delete(id)
  }
}

case class Product(id: Option[Long], title: String, price: BigDecimal)

trait RepositoryAlgebra[F[_]] {
  def create(product: Product): F[Product]

  def update(product: Product): F[Option[Product]]

  def get(id: Long): F[Option[Product]]

  def delete(id: Long): F[Option[Product]]
}

class MyRepository[F[_] : Applicative](sequence: AtomicLong) extends RepositoryAlgebra[F] {
  private val cache = TrieMap[Long, Product]()

  def create(product: Product): F[Product] = {
    val id: Long = sequence.getAndIncrement()
    val toSave = product.copy(id = id.some)
    cache += (id -> toSave)
    toSave.pure[F]
  }

  override def update(product: Product): F[Option[Product]] = product.id.traverse { id =>
    cache.update(id, product)
    product.pure[F]
  }

  override def get(id: Long): F[Option[Product]] = cache.get(id).pure[F]

  override def delete(id: Long): F[Option[Product]] = cache.remove(id).pure[F]
}