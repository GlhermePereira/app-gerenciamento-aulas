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
        callback: (Boolean, String) -> Unit
    ) {
        val horario = Horario(
            id = "", // Será preenchido pelo DAO
            data = data,
            hora = hora,
            disciplina = disciplina,
            professorId = professorId,
            professorNome = professorNome,
            disponivel = true
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
                    "disciplina" to horario.disciplina,
                    "disponivel" to horario.disponivel,
                    "hora" to horario.hora,
                    "professorNome" to horario.professorNome

                )
            )
            .addOnSuccessListener {
                callback(true, "Atualizado com sucesso")
            }
            .addOnFailureListener {
                callback(false, "Erro ao atualizar: ${it.message}")
            }
    }

}
