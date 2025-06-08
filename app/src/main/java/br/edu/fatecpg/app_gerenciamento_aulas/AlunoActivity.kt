package br.edu.fatecpg.app_gerenciamento_aulas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import br.edu.fatecpg.app_gerenciamento_aulas.Login
import br.edu.fatecpg.app_gerenciamento_aulas.MateriaisActivity
import br.edu.fatecpg.app_gerenciamento_aulas.MinhasAulasActivity
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.ListarAgendamentos
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlunoActivity : AppCompatActivity() {

    private lateinit var cardVerHorarios: CardView
    private lateinit var cardMinhasAulas: CardView
    private lateinit var cardMateriais: CardView
    private lateinit var btnSair: Button
    private lateinit var tvBoasVindas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        tvBoasVindas = findViewById(R.id.tvBoasVindas)
        cardVerHorarios = findViewById(R.id.btnVerAgendamentos)
        cardMinhasAulas = findViewById(R.id.btnMinhasAulas)
        cardMateriais = findViewById(R.id.btnMateriais)
        btnSair = findViewById(R.id.btnSair)
        var alunoId = ""

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            alunoId = it.uid

            db.collection("usuarios").document(alunoId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: "Aluno"
                        tvBoasVindas.text = "Olá, $nome!"
                    } else {
                        tvBoasVindas.text = "Olá, Aluno!"
                    }
                }
                .addOnFailureListener {
                    tvBoasVindas.text = "Olá, Aluno!"
                }
        }

        cardVerHorarios.setOnClickListener{
            val intent = Intent(this, ListarAgendamentos::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }

        cardMinhasAulas.setOnClickListener {
            val intent = Intent(this, MinhasAulasActivity::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }

        cardMateriais.setOnClickListener {
            val intent = Intent(this, MateriaisActivity::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            UserController.logout()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}