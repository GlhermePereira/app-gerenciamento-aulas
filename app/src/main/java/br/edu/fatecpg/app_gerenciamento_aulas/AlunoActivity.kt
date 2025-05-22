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

    private lateinit var btnAgendar: Button
    private lateinit var horariosListView: ListView
    private lateinit var aulasListView: ListView
    private lateinit var materiaisListView: ListView

    private var horarioSelecionado: Horario? = null
    private lateinit var alunoId: String
    private val horariosList = mutableListOf<Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        alunoId = user.uid

        // Inicializar Views
        btnAgendar = findViewById(R.id.btnAgendar)
        horariosListView = findViewById(R.id.horariosListView)
        aulasListView = findViewById(R.id.aulasListView)
        materiaisListView = findViewById(R.id.materiaisListView)

        // Selecionar horário
        horariosListView.setOnItemClickListener { _, _, position, _ ->
            if (position >= 0 && position < horariosList.size) {
                horarioSelecionado = horariosList[position]
            }
        }


    }
}