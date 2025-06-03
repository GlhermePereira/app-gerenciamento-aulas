package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.adapter.MinhasAulasAdapter
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import br.edu.fatecpg.app_gerenciamento_aulas.dao.AgendamentoDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento

class MinhasAulasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MinhasAulasAdapter
    private val meusAgendamentos = mutableListOf<Agendamento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minhas_aulas)

        val uid = UserController.getUsuarioIdAtual()

        recyclerView = findViewById(R.id.rvAgendamentos)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarAluno)
        btnVoltar.setOnClickListener { finish() }

        adapter = MinhasAulasAdapter(meusAgendamentos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (uid != null) {
            carregarMeusAgendamentos(uid)
        }


    }

    private fun carregarMeusAgendamentos(uid: String) {
        AgendamentoDao.listarPorAluno(uid) { lista ->
            runOnUiThread {
                meusAgendamentos.clear()
                meusAgendamentos.addAll(lista)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
