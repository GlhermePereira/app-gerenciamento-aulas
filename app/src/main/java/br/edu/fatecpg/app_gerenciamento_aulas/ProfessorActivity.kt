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
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import com.google.firebase.auth.FirebaseAuth


class ProfessorActivity : AppCompatActivity() {


    private lateinit var cvGerenciarHorarios: CardView
    private lateinit var btnSair: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_professor)



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
            UserController.logout()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }
}

