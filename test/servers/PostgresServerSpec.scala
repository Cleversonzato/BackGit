package servers

import java.time.LocalDateTime

import models.Repositorio
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.{GuiceOneAppPerTest, GuiceOneServerPerSuite}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits, Injecting}

class PostgresServerSpec extends PlaySpec with GuiceOneServerPerSuite with Injecting with FutureAwaits with DefaultAwaitTimeout{

  "Teste dos métodos com o banco de dados" should {
    val server = app.injector.instanceOf[PostgresServer]

      "Salvar dados no banco de dados " in {
        val salvar = await(server.salvar("buscaTeste", 0, LocalDateTime.now(), Array(
          Repositorio(id = 0, nome = "rTeste0", posicao = 0, url = "http0", estrelas = 0),
          Repositorio(id = 1, nome = "rTeste1", posicao = 1, url = "http1", estrelas = 1),
        )))

        salvar.toString contains ("sucesso")
      }

    "buscar dados no banco de dados " in {
      val buscas = await(server.buscas())
      println(buscas.toString())

      buscas.toString contains ("buscaTeste,0")
    }

    "buscar repositorio com o base_id 1 no banco de dados " in {
      val buscar = await(server.buscar(1))
      println(buscar.toString())

      buscar.toString contains ("rTeste")
    }

  }
}
