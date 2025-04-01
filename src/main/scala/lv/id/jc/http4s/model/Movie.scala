package lv.id.jc.http4s.model

import lv.id.jc.http4s.model.Movie.Actor

case class Movie(id: String, title: String, year: Int, actors: List[Actor], director: String)

object Movie {
  type Actor = String
}

