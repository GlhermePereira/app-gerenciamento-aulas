package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.adapter.HorarioAdapter
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.view.EditarHorarioActivity

class ListarHorariosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorarioAdapter
    private val horarios = mutableListOf<Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_horarios)

        recyclerView = findViewById(R.id.rvAgendamentos)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarProfessor)

        adapter = HorarioAdapter(horarios,
            onEditar = { horario ->
                val intent = Intent(this, EditarHorarioActivity::class.java)
                intent.putExtra("horarioId", horario.id)
                startActivity(intent)
            },
            onExcluir = { horario ->
                HorarioDao.excluir(horario.id) { sucesso ->
                    if (sucesso) {
                        Toast.makeText(this, "Horário excluído", Toast.LENGTH_SHORT).show()
                        horarios.remove(horario)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val professorId = intent.getStringExtra("professorId") ?: ""

        if(professorId.isEmpty()) {
            Toast.makeText(this, "Professor não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        HorarioDao.listarHorarios(professorId) { lista ->
            horarios.clear()
            horarios.addAll(lista)
            adapter.notifyDataSetChanged()
        }

        btnVoltar.setOnClickListener{
            finish()
        }

    }
}
