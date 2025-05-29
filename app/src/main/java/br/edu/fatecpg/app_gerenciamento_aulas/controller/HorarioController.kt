package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.firestore.FirebaseFirestore

object HorarioController {

    fun criarHorarioDisponivel(
    data: String,
    hora: String,
    disciplina: String,
    professorId: String,
    professorNome: String,
    materiais: List<String>, // Novo parâmetro
    callback: (Boolean, String) -> Unit
    ) {
        val horario = Horario(
            id = "", // Será preenchido pelo DAO
            data = data,
            hora = hora,
            disciplina = disciplina,
            professorId = professorId,
            professorNome = professorNome,
            disponivel = true,
            materiais = materiais  // Setar os materiais aqui
        )
        HorarioDao.adicionar(horario) { sucesso ->
            if (sucesso) callback(true, "")
            else callback(false, "Falha ao salvar horário")
        }
    }


    fun atualizarHorario(horario: Horario, callback: (Boolean, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("horarios")
            .document(horario.id)
            .update(
                mapOf(
                    "data" to horario.data,
                    "hora" to horario.hora,
                    "disciplina" to horario.disciplina,
                    "professorId" to horario.professorId,
                    "professorNome" to horario.professorNome,
                    "disponivel" to horario.disponivel,
                    "materiais" to horario.materiais  // Atualizando materiais também
                )
            )
            .addOnSuccessListener {
                callback(true, "Atualizado com sucesso")
            }
            .addOnFailureListener {
                callback(false, "Erro ao atualizar: ${it.message}")
            }
    }


    fun adicionarMateriaisAoHorario(horario: Horario, materiais: List<String>, onComplete: (Boolean) -> Unit) {
        val horarioAtualizado = horario.copy(materiais = materiais)
        HorarioDao.atualizar(horarioAtualizado, onComplete)
    }

}
