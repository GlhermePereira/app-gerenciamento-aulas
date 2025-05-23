package br.edu.fatecpg.app_gerenciamento_aulas.model

data class Professor(
    val id: String = "", // <- valor padrão
    val nome: String,
    val email: String
)

