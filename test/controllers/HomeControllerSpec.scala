package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HomeController" should {
    "método de bucas (list)" in {
      val controller = inject[HomeController]
      val home = controller.buscas().apply(FakeRequest(GET, "/buscas"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "método para buscar um (show)" in {
      val controller = inject[HomeController]
      val home = controller.buscas().apply(FakeRequest(GET, "/buscar/1"))
      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "método para salvar uma busca (save) - se está validando" in {
      val request = FakeRequest(POST, "/salvar")
      val home = route(app, request).get
      status(home) mustBe BAD_REQUEST
    }

    "método para salvar uma busca (save)" in {
      val request = FakeRequest(POST, "/salvar")
                      .withJsonBody(Json.obj(
                        "linguagem"->"teste", "numero" -> "0", "repositorios" -> Seq(
                          Json.obj("id"->0,"nome" -> "teste", "posicao" ->0, "url" ->"-", "estrelas"->0)
                        ))
                      )

      val home = route(app, request).get
      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }
}
