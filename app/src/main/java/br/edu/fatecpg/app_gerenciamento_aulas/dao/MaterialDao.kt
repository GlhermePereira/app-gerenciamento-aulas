package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Material
import com.google.firebase.firestore.FirebaseFirestore

object MaterialDao {
    private val db = FirebaseFirestore.getInstance().collection("materiais")

    fun enviar(material: Material, onComplete: (Boolean) -> Unit) {
        val doc = db.document()
        val novo = material.copy(id = doc.id)
        doc.set(novo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun listarPorAgendamento(agendamentoId: String, onResult: (List<Material>) -> Unit) {
        db.whereEqualTo("agendamentoId", agendamentoId)
            .get()
            .addOnSuccessListener { snap ->
                val materiais = snap.toObjects(Material::class.java)
                onResult(materiais)
            }
    }
}
