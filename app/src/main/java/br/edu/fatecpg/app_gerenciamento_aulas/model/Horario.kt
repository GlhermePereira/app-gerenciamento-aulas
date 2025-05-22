package br.edu.fatecpg.app_gerenciamento_aulas.model

import com.google.firebase.Timestamp

data class Horario(
    val id: String = "",
    val data: String = "",
    val hora: String = "",
    val disciplina: String = "",
    val professorId: String = "",
    val professorNome: String = "",
    val disponivel: Boolean = true
)



