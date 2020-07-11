package servers

import java.time.LocalDateTime

import javax.inject.Singleton
import javax.inject.Inject
import anorm._
import models.{Busca, Repositorio}
import anorm.{Macro, SQL, ToParameterList}
import anorm.NamedParameter

import scala.concurrent.Future
import play.api.db.Database
import play.api.libs.json._
import anorm.SqlParser.str
import play.api.libs.json.{Json, OFormat}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostgresServer @Inject()(implicit val ec: ExecutionContext, db: Database, databaseExecutionContext: DatabaseExecutionContext) {
  val parserBusca: RowParser[Busca] = Macro.namedParser[Busca]
  val parserRepositorio: RowParser[Repositorio] = Macro.namedParser[Repositorio]

  def buscas(): Future[List[Busca]]  = {
    Future {
      db.withConnection { implicit con =>
        SQL("SELECT * FROM busca")
          .executeQuery()
          .as(parserBusca.*)
      }
    }(databaseExecutionContext)
  }

  def buscar(id:Int): Future[List[Repositorio]]  ={
    Future {
      db.withConnection { implicit con =>
        SQL"SELECT * FROM repositorio WHERE busca_id = $id"
          .executeQuery()
          .as(parserRepositorio.*)
      }
    }(databaseExecutionContext)
  }

  def salvar(linguagem: String, numero: Int, data: LocalDateTime, resultados: Array[Repositorio]):Future[JsObject] ={

    Future {
      db.withConnection { implicit con =>

        SQL"INSERT INTO busca(linguagem, numero, data) VALUES ($linguagem, $numero, $data)"
          .executeInsert()

        match {
          case None => Json.obj("erro"->"falha ao inserir a busca")
          case Some(id) => {

            var query = "INSERT INTO repositorio(id, nome, posicao, url, estrelas, busca_id) VALUES"
            for(r<-resultados){
              query += "('" + r.id +"', '" + r.nome + "', '" + r.posicao + "', '" + r.url + "', '" + r.estrelas + "', '" + id + "'), "
            }
            query = query.dropRight(2)
            if(SQL(query).execute()){
              Json.obj("sucesso"->"tarefa completa")
            }else{
              Json.obj("erro"->"falha ao inserir os repositórios")
            }
          }
        }
      }
    }(databaseExecutionContext)
  }
}
