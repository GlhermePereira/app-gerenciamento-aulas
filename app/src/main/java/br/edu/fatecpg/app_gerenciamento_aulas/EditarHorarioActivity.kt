package br.edu.fatecpg.app_gerenciamento_aulas.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditarHorarioActivity : AppCompatActivity() {

    private lateinit var etData: EditText
    private lateinit var etHora: EditText
    private lateinit var etDisciplina: EditText
    private lateinit var btnSalvar: Button

    private lateinit var linksContainer: LinearLayout
    private lateinit var btnAdicionarLink: Button

    private var horario: Horario = Horario() // objeto vazio para depois popular
    private val editTextLinks = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_horario)

        etData = findViewById(R.id.etData)
        etHora = findViewById(R.id.etHora)
        etDisciplina = findViewById(R.id.etDisciplina)
        btnSalvar = findViewById(R.id.btnSalvar)

        linksContainer = findViewById(R.id.linksContainer)
        btnAdicionarLink = findViewById(R.id.btnAdicionarLink)

        val horarioId = intent.getStringExtra("horario_id")
        if (horarioId == null) {
            Toast.makeText(this, "ID do horário não fornecido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Popula campos se vierem pela intent
        intent.getSerializableExtra("horario")?.let {
            horario = it as Horario
            etData.setText(horario.data)
            etHora.setText(horario.hora)
            etDisciplina.setText(horario.disciplina)
            carregarLinksDinamicos(horario.materiais)
        }

        // Atualiza do Firestore para garantir dados atualizados
        val db = FirebaseFirestore.getInstance()
        db.collection("horarios").document(horarioId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    horario = Horario(
                        id = document.id,
                        data = document.getString("data") ?: "",
                        hora = document.getString("hora") ?: "",
                        disciplina = document.getString("disciplina") ?: "",
                        professorId = document.getString("professorId") ?: "",
                        professorNome = document.getString("professorNome") ?: "",
                        materiais = document.get("materiais") as? List<String> ?: emptyList()
                    )
                    etData.setText(horario.data)
                    etHora.setText(horario.hora)
                    etDisciplina.setText(horario.disciplina)
                    carregarLinksDinamicos(horario.materiais)
                } else {
                    Toast.makeText(this, "Horário não encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar horário", Toast.LENGTH_SHORT).show()
                finish()
            }

        val calendar = Calendar.getInstance()
        etData.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    etData.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etHora.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    etHora.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        btnAdicionarLink.setOnClickListener {
            adicionarEditTextLink("")
        }

        btnSalvar.setOnClickListener {
            val novaData = etData.text.toString().trim()
            val novaHora = etHora.text.toString().trim()
            val novaDisciplina = etDisciplina.text.toString().trim()

            if (novaData.isEmpty() || novaHora.isEmpty() || novaDisciplina.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val linksAtualizados = editTextLinks.map { it.text.toString().trim() }.filter { it.isNotEmpty() }

            val horarioAtualizado = horario.copy(
                data = novaData,
                hora = novaHora,
                disciplina = novaDisciplina,
                materiais = linksAtualizados
            )

            HorarioController.atualizarHorario(horarioAtualizado) { sucesso, msg ->
                runOnUiThread {
                    if (sucesso) {
                        Toast.makeText(this, "Horário atualizado!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun carregarLinksDinamicos(links: List<String>) {
        linksContainer.removeAllViews()
        editTextLinks.clear()
        for (link in links) {
            adicionarEditTextLink(link)
        }
        if (links.isEmpty()) {
            adicionarEditTextLink("") // pelo menos um campo vazio
        }
    }

    private fun adicionarEditTextLink(texto: String) {
        val et = EditText(this)
        et.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 8, 0, 8)
        }
        et.hint = "Link do material"
        et.setText(texto)
        linksContainer.addView(et)
        editTextLinks.add(et)
    }
}
