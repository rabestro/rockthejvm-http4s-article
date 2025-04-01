package lv.id.jc.http4s.matcher

import org.http4s.dsl.impl.QueryParamDecoderMatcher

object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")
