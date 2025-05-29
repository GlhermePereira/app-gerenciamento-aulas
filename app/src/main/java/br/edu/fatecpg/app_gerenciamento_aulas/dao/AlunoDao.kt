package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId

object AlunoDao {
    private val db = FirebaseFirestore.getInstance()

    fun buscarHorariosDisponiveis(callback: (List<Horario>) -> Unit) {
        db.collection("horarios")
            .whereEqualTo("disponivel", true)
            .get()
            .addOnSuccessListener { result ->
                val horarios =
                    result.mapNotNull { it.toObject(Horario::class.java).copy(id = it.id) }
                callback(horarios)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun criarAgendamento(
        horario: Horario,
        alunoId: String,
        callback: (Boolean) -> Unit
    ) {
        // Monta a string completa de data e hora para o agendamento
        val dataHoraCompleta = "${horario.data} ${horario.hora}"
        // A dataAula passa a ser apenas o dia (campo data)
        val dataAula = horario.data

        val novoAgendamento = Agendamento(
            alunoId = alunoId,
            professorId = horario.professorId,
            dataHora = dataHoraCompleta,
            horarioId = horario.id,
            dataAula = dataAula
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("agendamentos")
            .add(novoAgendamento)
            .addOnSuccessListener {
                // Marca o horário como indisponível
                db.collection("horarios")
                    .document(horario.id)
                    .update("disponivel", false)
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }



    fun buscarAulasAgendadas(alunoId: String, callback: (List<Agendamento>) -> Unit) {
        db.collection("agendamentos")
            .whereEqualTo("alunoId", alunoId)
            .get()
            .addOnSuccessListener { result ->
                val aulas = result.mapNotNull { it.toObject(Agendamento::class.java) }
                callback(aulas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }


}
