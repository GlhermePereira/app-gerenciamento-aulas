package br.edu.fatecpg.app_gerenciamento_aulas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CriarHorarioActivity : AppCompatActivity() {

    private lateinit var edtData: EditText
    private lateinit var edtHora: EditText
    private lateinit var edtDisciplina: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnAdicionarLink: Button
    private lateinit var linearLayoutLinks: LinearLayout

    private lateinit var professorId: String
    private var professorNome: String = "Professor" // valor padrão

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_horario)

        edtData = findViewById(R.id.etData)
        edtHora = findViewById(R.id.etHora)
        edtDisciplina = findViewById(R.id.etDisciplina)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnAdicionarLink = findViewById(R.id.btnAdicionarLink)
        linearLayoutLinks = findViewById(R.id.linearLayoutLinks)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        professorId = user.uid

        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(professorId).get()
            .addOnSuccessListener { document ->
                professorNome = document.getString("nome") ?: "Professor"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar dados do professor", Toast.LENGTH_LONG).show()
            }

        // Date picker
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

        // Time picker
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

        // Botão para adicionar um novo campo de link dinamicamente
        btnAdicionarLink.setOnClickListener {
            val novoLink = EditText(this).apply {
                hint = "Link do material"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 12
                }
            }
            linearLayoutLinks.addView(novoLink)
        }

        btnSalvar.setOnClickListener {
            val data = edtData.text.toString().trim()
            val hora = edtHora.text.toString().trim()
            val disciplina = edtDisciplina.text.toString().trim()

            if (data.isEmpty() || hora.isEmpty() || disciplina.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pega todos os links que foram adicionados dinamicamente e que não estejam vazios
            val links = mutableListOf<String>()
            for (i in 0 until linearLayoutLinks.childCount) {
                val view = linearLayoutLinks.getChildAt(i)
                if (view is EditText) {
                    val textoLink = view.text.toString().trim()
                    if (textoLink.isNotEmpty()) {
                        links.add(textoLink)
                    }
                }
            }

            // Aqui você precisa adaptar o método do controller para aceitar os links também
            HorarioController.criarHorarioDisponivel(
                data, hora, disciplina, professorId, professorNome, links
            ) { sucesso, msg ->
                runOnUiThread {
                    if (sucesso) {
                        Toast.makeText(this, "Horário salvo com sucesso", Toast.LENGTH_SHORT).show()
                        val intent = intent
                        intent.putExtra("novo_horario_criado", true)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Erro ao salvar horário: $msg", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
