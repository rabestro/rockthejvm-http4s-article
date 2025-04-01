package lv.id.jc.http4s.matcher

import cats.implicits.toBifunctorOps
import org.http4s.dsl.impl.OptionalValidatingQueryParamDecoderMatcher
import org.http4s.{ParseFailure, QueryParamDecoder}

import java.time.Year
import scala.util.Try

object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")(
  QueryParamDecoder[Int].emap { year =>
    Try(Year.of(year))
      .toEither
      .leftMap { throwable =>
        ParseFailure("Invalid year", throwable.getMessage)
      }
  })
