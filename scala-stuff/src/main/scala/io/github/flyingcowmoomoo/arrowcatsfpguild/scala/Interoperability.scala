package io.github.flyingcowmoomoo.arrowcatsfpguild.scala

import io.github.flyingcowmoomoo.arrowcatsfpguild.java.MyApi


object Interoperability {

  import scala.jdk.CollectionConverters._
  val api = new MyApi()
  api.setNumbers(List(1, 2, 3).map(Integer.valueOf).asJava)

}
