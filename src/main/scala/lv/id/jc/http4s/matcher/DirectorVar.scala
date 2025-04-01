package lv.id.jc.http4s.matcher

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import lv.id.jc.http4s.model.Director
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.util.Try

/**
 * Object for matching director names in URL paths.
 *
 * This object provides an `unapply` method to extract a `Director` instance from a string.
 * The string should contain the first and last name of the director, separated by a single space.
 *
 * ### Example Usage
 * {{{
 * GET {{host}}/movies?director=Zack%20Snyder&year=2021
 * }}}
 */
object DirectorVar {
  private val logger: Logger[IO] = Slf4jLogger.getLogger[IO]
  /**
   * Extracts a `Director` instance from a string.
   *
   * The string should contain the first and last name of the director, separated by a single space.
   * If the string is valid, a `Director` instance is returned; otherwise, `None` is returned.
   *
   * @param str The string to extract the director from.
   * @return An `Option` containing the `Director` instance if the string is valid, otherwise `None`.
   */
  def unapply(str: String): Option[Director] = {
    logger.info(s"Parsing director from input: '$str'").unsafeRunSync()
    val trimmed = str.trim
    if (trimmed.matches("^\\S+\\s+\\S+$")) {
      Try {
        val nameParts = trimmed.split("\\s+", 2)
        Director(nameParts(0), nameParts(1))
      }.toOption
    } else None
  }
}
