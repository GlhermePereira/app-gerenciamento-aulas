package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.adapter.HorarioAdapter
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class ListaHorariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorarioAdapter
    private val horarios = mutableListOf<Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_horarios)

        recyclerView = findViewById(R.id.rvAgendamentos)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarProfessor)

        adapter = HorarioAdapter(horarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        HorarioDao.listarHorarios { lista ->
            horarios.clear()
            horarios.addAll(lista)
            adapter.notifyDataSetChanged()
        }

        btnVoltar.setOnClickListener {
            finish() // Fecha a tela e volta à anterior
        }
    }
}
