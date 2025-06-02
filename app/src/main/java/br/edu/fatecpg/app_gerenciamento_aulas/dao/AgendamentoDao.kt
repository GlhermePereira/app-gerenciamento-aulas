package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import com.google.firebase.firestore.FirebaseFirestore

object AgendamentoDao {
    private val kolle = FirebaseFirestore.getInstance().collection("agendamentos")

    /**
     * Salva um novo agendamento e gera um ID automaticamente
     */
    fun salvar(agendamento: Agendamento, onComplete: (Boolean) -> Unit) {
        val docRef = kolle.document()
        val novoAgendamento = agendamento.copy(id = docRef.id)
        docRef.set(novoAgendamento)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    /**
     * Lista todos os agendamentos de um aluno
     */
    fun listarPorAluno(alunoId: String, callback: (List<Agendamento>) -> Unit) {
        kolle.whereEqualTo("alunoId", alunoId)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { it.toObject(Agendamento::class.java) }
                callback(lista)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Busca um agendamento pelo ID
     */
    fun buscarPorId(agendamentoId: String, onComplete: (Agendamento?) -> Unit) {
        kolle.document(agendamentoId)
            .get()
            .addOnSuccessListener { doc ->
                val agendamento = doc.toObject(Agendamento::class.java)
                onComplete(agendamento)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }
}
