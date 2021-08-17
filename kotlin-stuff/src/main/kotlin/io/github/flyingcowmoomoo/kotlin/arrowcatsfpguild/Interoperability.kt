package io.github.flyingcowmoomoo.kotlin.arrowcatsfpguild

import io.github.flyingcowmoomoo.arrowcatsfpguild.java.MyApi

object Interoperability {


    fun doThings() {

        // Kotlin
        val api = MyApi()
        api.addNumbers(listOf(1, 2, 3))
    }


}