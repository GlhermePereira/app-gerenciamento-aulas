package br.edu.fatecpg.app_gerenciamento_aulas.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.Login
import br.edu.fatecpg.app_gerenciamento_aulas.MateriaisActivity
import br.edu.fatecpg.app_gerenciamento_aulas.MinhasAulasActivity
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.ListarAgendamentos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlunoActivity : AppCompatActivity() {

    private lateinit var btnVerHorarios: Button
    private lateinit var btnMinhasAulas: Button
    private lateinit var btnMateriais: Button
    private lateinit var btnSair: Button
    private lateinit var tvBoasVindas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        tvBoasVindas = findViewById(R.id.tvBoasVindas)
        btnVerHorarios = findViewById(R.id.btnVerAgendamentos)
        btnMinhasAulas = findViewById(R.id.btnMinhasAulas)
        btnMateriais = findViewById(R.id.btnMateriais)
        btnSair = findViewById(R.id.btnSair)

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            val uid = it.uid

            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: "Aluno"
                        tvBoasVindas.text = "Olá, $nome!"
                    } else {
                        tvBoasVindas.text = "Olá, Aluno!"
                    }
                }
                .addOnFailureListener { e ->
                    tvBoasVindas.text = "Olá, Aluno!"
                    Log.e("Firestore", "Erro ao buscar nome do usuário", e)
                }
        }



        btnVerHorarios.setOnClickListener {
            startActivity(Intent(this, ListarAgendamentos::class.java))
        }

        btnMinhasAulas.setOnClickListener {
            startActivity(Intent(this, MinhasAulasActivity::class.java))
        }

        btnMateriais.setOnClickListener {
            startActivity(Intent(this, MateriaisActivity::class.java))
        }

        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}
