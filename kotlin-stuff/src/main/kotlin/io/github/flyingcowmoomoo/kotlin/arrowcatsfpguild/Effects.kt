package io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import arrow.core.toOption
import arrow.fx.coroutines.parZip
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


fun main() {
    val repo = MyRepository(AtomicLong())

    val program = MyProgram(repo)


     val finalProgram: suspend () -> Triple<Option<Product>, Option<Product>, Option<Product>> = suspend {
         val createInvalidPrice: Option<Product> = program.createProduct("Test 1", BigDecimal(666))
         val createValidPrice = program.createProduct("Test 2", BigDecimal(1337))
         val getCreateValidPrice = program.getProduct(createValidPrice.fold({-1}, {it.id.fold({-1}, { that -> that})}))
         Triple(createInvalidPrice,createValidPrice, getCreateValidPrice)
     }

    val parallelProgram: suspend () -> Triple<Option<Product>, Option<Product>, Option<Product>> = suspend {
        parZip(
            { program.createProduct("Test 1", BigDecimal(666)) },
            { program.createProduct("Test 2", BigDecimal(1337))  },
        ) { a, b ->
            Triple(a, b, program.getProduct(b.fold({-1}, {it.id.fold({-1}, { that -> that})})))
        }
    }

    val result = runBlocking {
        runBlocking {
            val syncResult = finalProgram.invoke()
            val parallelResult = parallelProgram.invoke()
            mapOf(Pair("syncResult", syncResult), Pair("parallelResult", parallelResult))
        }
    }

     println(result)
}

object Effects {

}
data class Product(val id: Option<Long>, val title: String, val price: BigDecimal)


class MyProgram(private val repo: Repository) {
    suspend fun renameProduct(id: Long, title: String): Option<Product> {
        return repo
            .get(id)
            .flatMap {
                val renamed = it.copy(title = title)
                repo.update(renamed)
            }
    }

    fun getProduct(id: Long): Option<Product> = repo.get(id)

    suspend fun createProduct(title: String, price: BigDecimal): Option<Product> {
        return when (price) {
            BigDecimal(666) -> none()
            else -> repo.create(Product(none(), title, price)).toOption()
        }
    }

    suspend fun deleteProduct(id: Long): Option<Product> = repo.delete(id)
}

class MyRepository(private val sequence: AtomicLong): Repository {

    private val cache = ConcurrentHashMap<Long, Product>()

    override fun create(product: Product): Product {
        val id = sequence.getAndIncrement()
        val toSave = product.copy(id = id.some())
        cache[id] = toSave
        return toSave
    }

    override fun update(product: Product): Option<Product> {
        return product.id.fold({ none() }, { cache.replace(it, product).toOption() })
    }

    override fun get(id: Long): Option<Product> {
        return cache[id].toOption()
    }

    override fun delete(id: Long): Option<Product> {
        return cache.remove(id).toOption()
    }

}
interface Repository {
    fun create(product: Product): Product
    fun update(product: Product): Option<Product>
    fun get(id: Long): Option<Product>
    fun delete(id: Long): Option<Product>
}