package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.AgendamentoController
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import br.edu.fatecpg.app_gerenciamento_aulas.controller.MaterialController
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class AlunoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var horariosListView: ListView
    private lateinit var aulasListView: ListView
    private lateinit var materiaisListView: ListView
    private lateinit var btnAgendar: Button
    private var horarioSelecionado: Horario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        auth = FirebaseAuth.getInstance()
        horariosListView = findViewById(R.id.horariosListView)
        aulasListView = findViewById(R.id.aulasListView)
        materiaisListView = findViewById(R.id.materiaisListView)
        btnAgendar = findViewById(R.id.btnAgendar)

        val alunoId = auth.currentUser?.uid ?: return

        carregarHorariosDisponiveis()
        carregarAulasAgendadas(alunoId)
        carregarMateriaisDeHoje(alunoId)

        horariosListView.setOnItemClickListener { _, _, position, _ ->
            horarioSelecionado = horariosListView.adapter.getItem(position) as Horario
            Toast.makeText(this, "Horário selecionado", Toast.LENGTH_SHORT).show()
        }

        btnAgendar.setOnClickListener {
            val horario = horarioSelecionado
            if (horario != null) {
                AgendamentoController.agendarAula(horario.id, alunoId) { sucesso, msg ->
                    if (sucesso) {
                        Toast.makeText(this, "Aula agendada!", Toast.LENGTH_SHORT).show()
                        carregarHorariosDisponiveis()
                        carregarAulasAgendadas(alunoId)
                    } else {
                        Toast.makeText(this, "Erro: $msg", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Selecione um horário!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun carregarHorariosDisponiveis() {
        HorarioController.listarHorariosDisponiveis { horarios ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, horarios.map {
                "${it.data} - ${it.hora} (${it.professorNome})"
            })
            horariosListView.adapter = adapter
        }
    }

    private fun carregarAulasAgendadas(alunoId: String) {
        AgendamentoController.listarAgendamentosDoAluno(alunoId) { agendamentos ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, agendamentos.map {
                "${it.dataAula} - ${it.professorNome}"
            })
            aulasListView.adapter = adapter
        }
    }

    private fun carregarMateriaisDeHoje(alunoId: String) {
        val hoje = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        MaterialController.listarMateriaisPorAlunoEData(alunoId, hoje) { materiais ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, materiais.map {
                "${it.titulo}: ${it.link}"
            })
            materiaisListView.adapter = adapter
        }
    }
}
