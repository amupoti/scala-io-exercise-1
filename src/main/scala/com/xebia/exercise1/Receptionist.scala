package com.xebia
package exercise1

import akka.actor.{Actor, ActorRef, Props}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import akka.util.Timeout
import com.xebia.exercise1.ReverseActor.ReverseResult
import spray.routing._
import spray.httpx.SprayJsonSupport._


class Receptionist extends HttpServiceActor
                      with ReverseRoute {
  implicit def executionContext = context.dispatcher

  //DONETODO add a createChild method which creates a child actor from a specified Props and name
  def createChild(props: Props, name: String): ActorRef = {
    context.actorOf(props, name)
  }

  def receive = runRoute(reverseRoute)
}

trait ReverseRoute extends HttpService {
  // we need this so we can use Futures and Timeout
  implicit def executionContext: ExecutionContext

  def createChild(props: Props, name: String): ActorRef

  //DONETODO define a val that returns the one ActorRef to the reverse actor using the createChild method

  val reverseActor = createChild(ReverseActor.props, ReverseActor.name)

  def reverseRoute: Route = path("reverse") {
    post {
      entity(as[ReverseRequest]) { request =>
        implicit val timeout = Timeout(20 seconds)

        import akka.pattern.ask

        //TODODONE replace the next line by asking the actor to Reverse
        //and converting (hint: mapping) the resulting Future[ReverseResult] to a Future[ReverseResponse]
        val futureResponse = (reverseActor ? ReverseActor.Reverse(request.value)).mapTo[ReverseResult] map (r => ReverseResponse(r.reversedText))

        complete(futureResponse)
      }
    }
  }
}

