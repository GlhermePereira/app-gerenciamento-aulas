package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.firestore.FirebaseFirestore

object HorarioDao {
    private val kolle = FirebaseFirestore.getInstance().collection("horarios")

    /**
     * Adiciona um novo horário, preenchendo o ID antes de salvar
     */
    fun adicionar(horario: Horario, onComplete: (Boolean) -> Unit) {
        val docRef = kolle.document()
        val novoHorario = horario.copy(id = docRef.id)
        docRef.set(novoHorario)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    /**
     * Lista todos os horários cadastrados por um professor
     */
    fun listarDoProfessor(professorId: String, onResult: (List<Horario>) -> Unit) {
        kolle.whereEqualTo("professorId", professorId)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.mapNotNull { it.toObject(Horario::class.java) }
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /**
     * Atualiza um horário existente
     */
    fun atualizar(horario: Horario, onComplete: (Boolean) -> Unit) {
        if (horario.id.isBlank()) {
            onComplete(false)
            return
        }
        kolle.document(horario.id)
            .set(horario)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    /**
     * Exclui um horário pelo seu ID
     */
    fun excluir(horarioId: String, onComplete: (Boolean) -> Unit) {
        if (horarioId.isBlank()) {
            onComplete(false)
            return
        }
        kolle.document(horarioId)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
