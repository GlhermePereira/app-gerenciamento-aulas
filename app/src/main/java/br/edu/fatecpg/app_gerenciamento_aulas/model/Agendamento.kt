package br.edu.fatecpg.app_gerenciamento_aulas.model

data class Agendamento(
    var id: String? = null,            // id do agendamento (opcional, atribuído pelo Firestore)
    val alunoId: String = "",          // id do aluno que agendou
    val professorId: String = "",
    val hora: String = "", // formato tipo "2025-05-22 14:00"
    val horarioId: String = "",        // id do horário agendado
    val data: String = "",
    val disciplina: String = "" 
)
