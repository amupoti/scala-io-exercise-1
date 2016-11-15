package com.xebia
package exercise1

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props}

object ReverseActor {
  //DONE_TODO define messages for reverse actor here (Reverse, ReverseResult)
  case class Reverse(text: String) {}

  case class ReverseResult(reversedText: String) {}

  //DONE_TODO define props and name for ReverseActor here
  val props = Props[ReverseActor]
  val name = "ReverseActor"
}

class ReverseActor extends Actor {
  //DONE_TODO extend from Actor
  import ReverseActor._

  //DONE_TODO write your receive method here, respond with a ReverseResult
  def receive: Receive = {

    case r: Reverse => sender() ! ReverseResult(r.text.reverse)


  }
}
