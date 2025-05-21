package br.edu.fatecpg.app_gerenciamento_aulas

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    // Variáveis usando lateinit (evita nullable)
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button

    private lateinit var auth: FirebaseAuth // Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Vincula os componentes da interface
        val editTextEmail = findViewById<EditText>(R.id.email)
        val editTextPassword = findViewById<EditText>(R.id.senha)
        val buttonReg = findViewById<Button>(R.id.btnCadastrar)

        // Clique do botão de registro
        buttonReg.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validação dos campos
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Informe o e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Informe a senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro com Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()

                        // Vai para a tela de Login
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
