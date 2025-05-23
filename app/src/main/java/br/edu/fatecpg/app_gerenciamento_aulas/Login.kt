package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.login_button)
        val textRegister = findViewById<TextView>(R.id.textRegister)
        textRegister.setOnClickListener {
            // ação a ser executada
            Toast.makeText(this, "Ir para cadastro", Toast.LENGTH_SHORT).show()

            // exemplo: abrir outra activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Informe o email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Informe a senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            // Busca o tipo do usuário no Firestore:
                            FirebaseFirestore.getInstance().collection("usuarios")
                                .document(uid)
                                .get()
                                .addOnSuccessListener { doc ->
                                    val tipo = doc.getString("tipo")
                                    when (tipo) {
                                        "aluno" -> startActivity(
                                            Intent(
                                                this,
                                                AlunoActivity::class.java
                                            )
                                        )

                                        "professor" -> {
                                            val intent = Intent(this, ProfessorActivity::class.java)
                                            intent.putExtra("professorId", uid)  // envia o uid do professor logado
                                            startActivity(intent)
                                        }


                                        else -> Toast.makeText(
                                            this,
                                            "Tipo inválido",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show()
                    }
                }



        }
    }
}
