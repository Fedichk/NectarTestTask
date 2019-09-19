package org.task.entity

import cats.effect.IO
import io.circe.Encoder
import io.circe.literal._
import org.http4s.circe.jsonOf

case class User(_id: String, name: String, email: String, age: Int) {

  implicit val UserEncoder: Encoder[User] =
    Encoder.instance { user: User =>
      json"""{
            "_id": ${user._id}
            "name": ${user.name}
            "email": ${user.email}
            "age": ${user.age}
            }"""
    }

  implicit val userDecoder = jsonOf[IO, User]

}
