package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.fatecpg.app_gerenciamento_aulas.controller.ProfessorController
import com.google.firebase.auth.FirebaseAuth


class ProfessorActivity : AppCompatActivity() {

    private lateinit var btnGerenciarHorarios: Button
    private lateinit var btnAdicionarMaterial: Button
    private lateinit var btnVerAgendamentos: Button
    private lateinit var btnSair: Button

    private lateinit var professorId: String
    private lateinit var professorNome: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val professorId = intent.getIntExtra("professorId", -1)

        setContentView(R.layout.activity_professor)

        // Pega dados do professor logado
        FirebaseAuth.getInstance().currentUser?.let { user ->
            professorNome = user.displayName ?: "Professor"
        } ?: run {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Vincula botões
        btnGerenciarHorarios  = findViewById<Button>(R.id.btnGerenciarHorarios)
        btnVerAgendamentos    = findViewById<Button>(R.id.btnVerAgendamentos)
        btnSair               = findViewById<Button>(R.id.btnSairProfessor)

        // Ao clicar em Gerenciar Horários → abre tela de criação/edição
        btnGerenciarHorarios.setOnClickListener {
            val intent = Intent(this, CriarHorarioActivity::class.java)
            intent.putExtra("professorId", professorId)
            startActivity(intent)

        }
        btnVerAgendamentos.setOnClickListener{
            val professorId = intent.getStringExtra("professorId") ?: ""
            val intent = Intent(this, ListarHorariosActivity::class.java)
            intent.putExtra("professorId", professorId)
            startActivity(intent)
        }


        // Ao clicar em Sair → desloga e volta para Login
        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}

