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
    fun listarHorariosProfessor(professorId: String, callback: (List<Horario>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("horarios")
            .whereEqualTo("professorId", professorId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lista = querySnapshot.documents.map { doc ->
                    doc.toObject(Horario::class.java)!!
                }
                callback(lista)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun listarHorariosDisponiveis(callback: (List<Horario>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("horarios")
            .whereEqualTo("disponivel", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lista = querySnapshot.documents.map { doc ->
                    doc.toObject(Horario::class.java)!!
                }
                callback(lista)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun atualizarDisponibilidade(horarioId: String, disponivel: Boolean, onComplete: (Boolean) -> Unit) {
        if (horarioId.isBlank()) {
            onComplete(false)
            return
        }

        kolle.document(horarioId)
            .update("disponivel", disponivel)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
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

    fun buscarPorId(horarioId: String, onComplete: (Horario?) -> Unit) {
        kolle.document(horarioId).get()
            .addOnSuccessListener { doc ->
                val horario = doc.toObject(Horario::class.java)
                onComplete(horario)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

}
