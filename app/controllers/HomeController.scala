package controllers

import java.time.LocalDateTime

import javax.inject._
import models.{Busca, Repositorio}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import servers.PostgresServer

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(val service:PostgresServer, val controllerComponents: ControllerComponents, implicit val ec: ExecutionContext) extends BaseController {

  implicit def formatRepositorio: OFormat[Repositorio] = Json.format[Repositorio];
  implicit def formatBusca: OFormat[Busca] = Json.format[Busca];

  def buscas() = Action.async { implicit request: Request[AnyContent] =>
    service.buscas().map{resultado =>
      Ok(Json.toJson(resultado))
    }
  }

  def buscar(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    service.buscar(id).map{resultado => Ok(Json.toJson(resultado))}
  }

  def salvar() = Action.async  { implicit request: Request[AnyContent] =>
    request.body.asJson.map { body =>
      val linguagem = body("linguagem").as[String]
      val numero = body("numero").as[String].toInt
      val resultados = body("repositorios").as[Array[Repositorio]]

      service.salvar(linguagem, numero, LocalDateTime.now(), resultados).map(Ok(_))
    }.getOrElse(
      Future(BadRequest)
    )
  }

}
