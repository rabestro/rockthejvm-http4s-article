package lv.id.jc.http4s

import cats.Monad
import cats.effect.{Concurrent, ExitCode, IO, IOApp}
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import lv.id.jc.http4s.matcher.{DirectorQueryParamMatcher, DirectorVar, YearQueryParamMatcher}
import lv.id.jc.http4s.model.Movie.Actor
import lv.id.jc.http4s.model.{Director, Movie}
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.`Content-Encoding`
import org.http4s.implicits._
import org.typelevel.ci.CIString
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jFactory

import java.util.UUID
import scala.collection.mutable

object Main extends IOApp {
  implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]

  val directors: mutable.Map[Actor, Director] =
    mutable.Map("Zack Snyder" -> Director("Zack", "Snyder"))

  def directorRoutes[F[_]: Concurrent](implicit logger: LoggerFactory[F]): HttpRoutes[F] = {
    val log = logger.getLogger
    val dsl = Http4sDsl[F]
    import dsl._

    implicit val directorDecoder: EntityDecoder[F, Director] = jsonOf[F, Director]

    HttpRoutes.of[F] {
      case GET -> Root / "directors" / DirectorVar(director) =>
          directors.get(director.toString) match {
          case Some(dir) =>
            log.info(s"Found director: ${dir.firstName} ${dir.lastName}") *>
              Ok(dir.asJson, Header.Raw(CIString("My-Custom-Header"), "value"))
          case None =>
            log.warn(s"No director called $director found") *>
              NotFound(s"No director called $director found")
        }

      case req@POST -> Root / "directors" =>
        for {
          director <- req.as[Director]
          _ <- log.info(s"Adding director: ${director.firstName} ${director.lastName}")
          _ = directors.put(director.toString, director)
          res <- Ok.headers(`Content-Encoding`(ContentCoding.gzip))
            .map(_.addCookie(ResponseCookie("My-Cookie", "value")))
        } yield res
    }
  }

  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )

  val movies: Map[String, Movie] = Map(snjl.id -> snjl)

  private def findMovieById(movieId: UUID) =
    movies.get(movieId.toString)

  private def findMoviesByDirector(director: String): List[Movie] =
    movies.values.filter(_.director == director).toList

  def movieRoutes[F[_] : Monad](implicit logger: LoggerFactory[F]): HttpRoutes[F] = {
    val log = logger.getLogger
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val movieByDirector = findMoviesByDirector(director)
        maybeYear match {
          case Some(y) =>
            y.fold(
              _ => BadRequest("The given year is not valid"),
              { year =>
                val moviesByDirAndYear =
                  movieByDirector.filter(_.year == year.getValue)
                Ok(moviesByDirAndYear.asJson)
              }
            )
          case None => Ok(movieByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
          findMovieById(movieId).map(_.actors) match {
          case Some(actors) =>
            log.info(s"Returning actors for movie ID: $movieId") *>
              Ok(actors.asJson)
          case _ =>
            log.warn(s"Movie with ID: $movieId not found") *>
              NotFound(s"No movie with id $movieId found")
        }
    }
  }

  def allRoutes[F[_] : Concurrent](implicit logger: LoggerFactory[F]): HttpRoutes[F] = {
    import cats.syntax.semigroupk._
    movieRoutes[F] <+> directorRoutes[F]
  }

  def allRoutesComplete[F[_] : Concurrent](implicit logger: LoggerFactory[F]): HttpApp[F] = {
    allRoutes.orNotFound
  }

  private val movieApp = allRoutesComplete[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    EmberServerBuilder.default[IO]
      .withHttpApp(movieApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}

