package br.edu.fatecpg.app_gerenciamento_aulas.dao

import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material
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

    fun criarAgendamento(horario: Horario, alunoId: String, callback: (Boolean) -> Unit) {
        val dataAula = horario.dataHora.substringBefore(" ") // Pega só a parte da data

        val novoAgendamento = Agendamento(
            alunoId = alunoId,
            professorId = horario.professorId,
            dataHora = horario.dataHora,
            horarioId = horario.id,
            dataAula = dataAula
        )

        FirebaseFirestore.getInstance().collection("agendamentos")
            .add(novoAgendamento)
            .addOnSuccessListener {
                // Atualiza o horário como indisponível
                FirebaseFirestore.getInstance().collection("horarios")
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

    fun buscarMateriaisPorData(alunoId: String, data: LocalDate, callback: (List<Material>) -> Unit) {
        db.collection("materiais")
            .whereEqualTo("alunoId", alunoId)
            .whereEqualTo("dataAula", data.toString())
            .get()
            .addOnSuccessListener { result ->
                val materiais = result.mapNotNull { it.toObject(Material::class.java) }
                callback(materiais)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

}
