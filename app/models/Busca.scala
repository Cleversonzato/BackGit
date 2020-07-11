package models

import java.time.LocalDateTime

case class Busca(
                id:Long,
                linguagem: String,
                numero:Int,
                data: LocalDateTime
                )
