package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Aula
import com.google.firebase.firestore.FirebaseFirestore

object AulaDao {
    private val db = FirebaseFirestore.getInstance().collection("agendamentos")

    fun listarPorProfessor(professorId: String, onResult: (List<Aula>) -> Unit) {
        db.whereEqualTo("professorId", professorId)
            .get()
            .addOnSuccessListener { snapshot ->
                val aulas = snapshot.toObjects(Aula::class.java)
                onResult(aulas)
            }
    }
}
