package util.QueryBuilder

import akka.http.scaladsl.model.Uri

object QueryBuilder {

  def uriFor(params: Map[String, Option[String]]): Option[Uri.Query] = {
    val m = params.collect {
      case (k, Some(v)) => (k, v)
    }

    if (m.isEmpty)
      None
    else
      Some(Uri.Query(m))
  }

}
