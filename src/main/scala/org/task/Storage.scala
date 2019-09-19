package org.task

import java.util.concurrent.TimeUnit

import org.mongodb.scala.{Completed, Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer, SingleObservable}
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Filters._
import org.task.entity.User

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Storage {
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("users")
  private val collection: MongoCollection[Document] = database.getCollection("users")

  def save(user: User): String = {
    val document = makeDocument(user, user._id)
    val insertObservable: Observable[Completed] = Storage.collection.insertOne(document)
    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("Inserted")

      override def onError(e: Throwable): Unit = println(e)

      override def onComplete(): Unit = println("Completed")
    })
    document("_id").asString().getValue
  }

  def getAllUsers(sortingType: String): String = {
    var col = ""
    var allUsers: SingleObservable[Seq[Document]] = null
    if (sortingType != null && sortingType.nonEmpty && sortingType.equals("DESC")) {
      allUsers = collection.find().sort(descending("name")).collect()
    } else {
      allUsers = collection.find().collect()
    }
    val results = Await.result(allUsers.toFuture, Duration(10, TimeUnit.SECONDS))
    for (document <- results)
      col = col + document.toJson().toString
    col
  }

  def getUserById(id: String): String = {
    val user = collection.find().filter(equal("_id", id)).first()
    val result = Await.result(user.toFuture, Duration(2, TimeUnit.SECONDS))
    result.toJson().toString
  }

  private def makeDocument(user: User, id: String): Document = {
    var userId = id
    if (id == null || id.isEmpty) {
      userId = getNextSequence
    }
    Document(
      "_id" -> userId,
      "name" -> user.name,
      "email" -> user.email,
      "age" -> user.age)
  }

  private def getNextSequence: String = {
    val maxId = collection.find().projection(include("_id")).sort(descending("_id")).first()
    val result = Await.result(maxId.toFuture, Duration(2, TimeUnit.SECONDS))

    if (result != null && result.nonEmpty) {
      return (result("_id").asString().getValue.toInt + 1).toString
    }
    "0"
  }
}
