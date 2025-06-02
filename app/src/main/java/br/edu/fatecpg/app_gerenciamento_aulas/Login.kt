package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import br.edu.fatecpg.app_gerenciamento_aulas.view.AlunoActivity

class Login : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.login_button)
        val textRegister = findViewById<TextView>(R.id.textRegister)

        textRegister.setOnClickListener {
            Toast.makeText(this, "Ir para cadastro", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Register::class.java))
        }

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Informe o email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (senha.isEmpty()) {
                Toast.makeText(this, "Informe a senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chama o controller para fazer login
            UserController.logar(email, senha) { sucesso, erro, tipoUsuario, uid ->
                runOnUiThread {
                    if (sucesso) {
                        when (tipoUsuario) {
                            "aluno" -> {
                                startActivity(Intent(this, AlunoActivity::class.java))
                                intent.putExtra("alunoId", uid)
                                startActivity(intent)
                                finish()
                            }
                            "professor" -> {
                                val intent = Intent(this, ProfessorActivity::class.java)
                                intent.putExtra("professorId", uid)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                Toast.makeText(this, "Tipo inválido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, erro ?: "Erro no login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
