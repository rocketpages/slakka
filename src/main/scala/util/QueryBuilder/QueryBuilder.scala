package util.QueryBuilder

import akka.http.scaladsl.model.Uri

object QueryBuilder {

  def uriFor(params: Map[String, Option[String]]): Option[Uri.Query] = {
    val m = params.collect { case (key, Some(value)) => (key, value) }

    if (m.isEmpty)
      None
    else
      Some(Uri.Query(m))
  }

}
