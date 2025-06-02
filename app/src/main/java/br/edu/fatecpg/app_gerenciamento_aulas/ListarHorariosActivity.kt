package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
    private lateinit var professorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_horarios)

        professorId = intent.getStringExtra("professorId") ?: ""

        if (professorId.isEmpty()) {
            Toast.makeText(this, "Professor ID não fornecido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.rvAgendamentos)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarProfessor)
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionarHorario)

        adapter = HorarioAdapter(
            horarios,
            onEditar = { horario ->
                val intent = Intent(this, EditarHorarioActivity::class.java)
                intent.putExtra("horario_id", horario.id)
                startActivity(intent)
            },
            onExcluir = { horario ->
                // Se quiser, pode implementar a exclusão aqui
            },
            onAdicionarMaterial = { horario ->
                val intent = Intent(this, AdicionarMaterialActivity::class.java)
                intent.putExtra("horario_id", horario.id)
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnVoltar.setOnClickListener {
            finish()
        }

        btnAdicionar.setOnClickListener {
            val intent = Intent(this, CriarHorarioActivity::class.java)
            intent.putExtra("professorId", professorId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        HorarioDao.listarHorariosProfessor(professorId) { lista ->
            runOnUiThread {
                horarios.clear()
                horarios.addAll(lista)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
