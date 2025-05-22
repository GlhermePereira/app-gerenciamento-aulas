package br.edu.fatecpg.app_gerenciamento_aulas.model

import java.io.Serializable

data class Horario(
    val id: String,
    val data: String,
    val hora: String,
    val disciplina: String,
    val professorId: String,
    val professorNome: String,
    val disponivel: Boolean
) : Serializable



