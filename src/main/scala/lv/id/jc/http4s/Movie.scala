package lv.id.jc.http4s

import lv.id.jc.http4s.Movie.Actor

case class Movie(id: String, title: String, year: Int, actors: List[Actor], director: String)

object Movie {
  type Actor = String
}

