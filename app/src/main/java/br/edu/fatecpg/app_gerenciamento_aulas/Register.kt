package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.RegisterController
import br.edu.fatecpg.app_gerenciamento_aulas.model.User

class Register : AppCompatActivity() {

    private lateinit var controller: RegisterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        controller = RegisterController(this)

        val buttonReg = findViewById<Button>(R.id.btnCadastrar)
        buttonReg.setOnClickListener {
            val nome= findViewById<EditText>(R.id.nome).text.toString().trim()
            val email = findViewById<EditText>(R.id.email).text.toString().trim()
            val password = findViewById<EditText>(R.id.senha).text.toString().trim()  // Ajuste aqui conforme seu XML

            val tipo = when {
                findViewById<RadioButton>(R.id.radioAluno).isChecked -> "aluno"
                findViewById<RadioButton>(R.id.radioProfessor).isChecked -> "professor"
                else -> ""
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Informe o e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Informe a senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tipo.isEmpty()) {
                Toast.makeText(this, "Selecione o tipo de usuário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(email, tipo,nome)
            controller.registrar(user, password) { success, errorMsg ->
                if (success) {
                    Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, errorMsg ?: "Erro desconhecido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
