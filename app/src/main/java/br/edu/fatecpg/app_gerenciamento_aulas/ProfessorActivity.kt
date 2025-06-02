package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import br.edu.fatecpg.app_gerenciamento_aulas.R.id.btnSairProfessor
import br.edu.fatecpg.app_gerenciamento_aulas.R.id.cdListarAgendamentos
import com.google.firebase.auth.FirebaseAuth


class ProfessorActivity : AppCompatActivity() {


    private lateinit var cvGerenciarHorarios: CardView
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
        cvGerenciarHorarios    = findViewById<CardView>(cdListarAgendamentos)
        btnSair               = findViewById<Button>(btnSairProfessor)


        cvGerenciarHorarios.setOnClickListener{
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

