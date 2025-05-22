package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.adapter.HorarioAdapter
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class ListarHorariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorarioAdapter
    private val horarios = mutableListOf<Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_horarios)

        recyclerView = findViewById(R.id.rvAgendamentos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HorarioAdapter(horarios)
        recyclerView.adapter = adapter

        val professorId = intent.getStringExtra("professorId") ?: ""

        if (professorId.isNotBlank()) {
            carregarHorarios(professorId)
        } else {
            Toast.makeText(this, "ID do professor não encontrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarHorarios(professorId: String) {
        HorarioController.listarHorarios(professorId) { lista ->
            horarios.clear()
            horarios.addAll(lista)
            adapter.notifyDataSetChanged()
        }
    }
}
