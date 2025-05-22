package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.firestore.FirebaseFirestore

object HorarioDao {
    private val db = FirebaseFirestore.getInstance().collection("horarios")

    fun adicionar(horario: Horario, onComplete: (Boolean) -> Unit) {
        val doc = db.document()
        val novoHorario = horario.copy(id = doc.id)
        doc.set(novoHorario)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun listarDoProfessor(professorId: String, onResult: (List<Horario>) -> Unit) {
        db.whereEqualTo("professorId", professorId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.toObjects(Horario::class.java)
                onResult(lista)
            }
    }
}
