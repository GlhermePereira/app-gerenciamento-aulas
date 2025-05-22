package br.edu.fatecpg.app_gerenciamento_aulas.model

import com.google.firebase.Timestamp

data class Horario(
    val id: String = "",
    val professorId: String = "",
    val dataHora: String = "", // formato tipo "2025-05-22 14:00"
    val disponivel: Boolean = true
)


