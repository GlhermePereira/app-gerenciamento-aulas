package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.adapter.AgendamentoAdapter
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import br.edu.fatecpg.app_gerenciamento_aulas.dao.AgendamentoDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.util.NotificacaoUtil
import java.text.SimpleDateFormat
import java.util.Locale

class ListarAgendamentos : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AgendamentoAdapter
    private val horariosDisponiveis = mutableListOf<Horario>()
    private lateinit var alunoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_horarios)

        val uid = UserController.getUsuarioIdAtual()


        recyclerView = findViewById(R.id.rvAgendamentos)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarAluno)
        btnVoltar.setOnClickListener { finish() }

        adapter = AgendamentoAdapter(horariosDisponiveis) { horario ->
            val agendamento = Agendamento(
                alunoId = uid.toString(),
                professorId = horario.professorId,
                hora = horario.hora,
                horarioId = horario.id,
                data = horario.data,
                disciplina = horario.disciplina
            )

            // 1. Salvar agendamento
            AgendamentoDao.salvar(agendamento) { sucesso ->
                if (sucesso) {
                    // 2. Atualizar disponibilidade do horário
                    HorarioDao.atualizarDisponibilidade(horario.id, false) { atualizou ->
                        if (atualizou) {
                            Toast.makeText(
                                this,
                                "Agendamento realizado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            carregarHorarios() // Atualiza lista
                        } else {
                            Toast.makeText(this, "Erro ao atualizar horário", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Erro ao agendar", Toast.LENGTH_SHORT).show()
                }
                UserController.getNomeUsuarioAtual { nomeAluno ->
                    if (nomeAluno == null) return@getNomeUsuarioAtual

                    UserController.getNomeUsuarioPorId(horario.professorId) { nomeProfessor ->
                        if (nomeProfessor == null) return@getNomeUsuarioPorId

                        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val dataHora = formato.parse("${agendamento.data} ${agendamento.hora}")
                        val momentoNotificacaoAula =
                            dataHora?.time?.minus(60 * 60 * 1000) ?: return@getNomeUsuarioPorId

                        NotificacaoUtil.agendarNotificacao(
                            context = this,
                            disciplina = agendamento.disciplina,
                            nomeAluno = nomeAluno,
                            nomeProfessor = nomeProfessor,
                            horarioNotificacaoAula = momentoNotificacaoAula
                        )
                    }
                }
            }
            }


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        carregarHorarios()
    }

    private fun carregarHorarios() {
        HorarioDao.listarHorariosDisponiveis { lista ->
            runOnUiThread {
                horariosDisponiveis.clear()
                horariosDisponiveis.addAll(lista.filter { it.disponivel })
                adapter.notifyDataSetChanged()
            }
        }
    }
}
