package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.firestore.FirebaseFirestore

object ProfessorDao {
    private val db = FirebaseFirestore.getInstance()

    // Adiciona um novo horário disponível
    fun adicionarHorario(horario: Horario, callback: (Boolean) -> Unit) {
        db.collection("horarios")
            .add(horario)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // Lista os horários cadastrados de um professor
    fun buscarHorarios(professorId: String, callback: (List<Horario>) -> Unit) {
        db.collection("horarios")
            .whereEqualTo("professorId", professorId)
            .get()
            .addOnSuccessListener { result ->
                val horarios = result.mapNotNull {
                    it.toObject(Horario::class.java).copy(id = it.id)
                }
                callback(horarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }


}
