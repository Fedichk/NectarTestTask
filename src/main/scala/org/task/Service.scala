package org.task

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object SortingTypeQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("sort")

class Service[F[_] : Sync]() extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "users" :? SortingTypeQueryParamMatcher(sortingType) =>
      sortingType match {
        case None =>
          Ok(Storage.getAllUsers(null))
        case Some(sort) =>
          Ok(Storage.getAllUsers(sort))
      }
    case GET -> Root / "users" / id =>
      try {
        Ok(Storage.getUserById(id))
      } catch {
        case e: Exception => BadRequest()
      }
    case req@POST -> Root / "users" =>
      Ok()
  }
}
