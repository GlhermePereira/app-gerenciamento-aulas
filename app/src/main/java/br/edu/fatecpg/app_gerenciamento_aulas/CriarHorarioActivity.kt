package br.edu.fatecpg.app_gerenciamento_aulas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*class CriarHorarioActivity : AppCompatActivity() {

    private lateinit var edtData: EditText
    private lateinit var edtHora: EditText
    private lateinit var edtDisciplina: EditText
    private lateinit var btnSalvar: Button

    private lateinit var professorId: String
    private var professorNome: String = "Professor" // valor padrão

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_horario)

        edtData       = findViewById(R.id.edtData)
        edtHora       = findViewById(R.id.edtHora)
        edtDisciplina = findViewById(R.id.edtDisciplina)
        btnSalvar     = findViewById(R.id.btnSalvar)

        FirebaseAuth.getInstance().currentUser?.let { user ->
            professorNome = user.displayName ?: "Professor"
        } ?: run {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            professorId = user.uid
            FirebaseFirestore.getInstance().collection("usuarios")
                .document(professorId)
                .get()
                .addOnSuccessListener { document ->
                    professorNome = document.getString("nome") ?: "Professor"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao buscar nome do professor", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        professorId = user.uid
        btnSalvar.isEnabled = false
// Buscar o nome do professor na coleção "usuarios"
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(professorId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    professorNome = document.getString("nome") ?: "Professor"
                } else {
                    Toast.makeText(this, "Usuário não encontrado no banco", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar dados do professor", Toast.LENGTH_LONG).show()
            }
        btnSalvar.isEnabled = true

        // Date picker e time picker (sem mudança)
        val calendar = Calendar.getInstance()
        edtData.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    edtData.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        edtHora.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    edtHora.setText(String.format("%02d:%02d", hour, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        btnSalvar.setOnClickListener {
            val data       = edtData.text.toString().trim()
            val hora       = edtHora.text.toString().trim()
            val disciplina = edtDisciplina.text.toString().trim()

            if (data.isEmpty() || hora.isEmpty() || disciplina.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            HorarioController.criarHorarioDisponivel(
                data, hora, disciplina, professorId, professorNome
            ) { sucesso, msg ->
                runOnUiThread {
                    if (sucesso) {
                        Toast.makeText(this, "Horário salvo com sucesso", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Erro: $msg", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
