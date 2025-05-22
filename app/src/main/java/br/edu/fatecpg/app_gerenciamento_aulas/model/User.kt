package br.edu.fatecpg.app_gerenciamento_aulas.model

data class User(
    val email: String = "",
    val tipo: String = ""  // "aluno" ou "professor"
)
