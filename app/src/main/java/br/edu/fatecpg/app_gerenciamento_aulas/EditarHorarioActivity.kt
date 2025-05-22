package br.edu.fatecpg.app_gerenciamento_aulas.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class EditarHorarioActivity : AppCompatActivity() {

    private lateinit var etData: EditText
    private lateinit var etHora: EditText
    private lateinit var etDisciplina: EditText
    private lateinit var btnSalvar: Button

    private var horario: Horario = Horario() // usa o construtor vazio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_horario)

        etData = findViewById(R.id.etData)
        etHora = findViewById(R.id.etHora)
        etDisciplina = findViewById(R.id.etDisciplina)
        btnSalvar = findViewById(R.id.btnSalvar)

        // Recebe o objeto enviado por outra activity (se existir)
        intent.getSerializableExtra("horario")?.let {
            horario = it as Horario

            // Preenche os campos com os dados do horário atual
            etData.setText(horario.data)
            etHora.setText(horario.hora)
            etDisciplina.setText(horario.disciplina)
        }

        btnSalvar.setOnClickListener {
            val novaData = etData.text.toString()
            val novaHora = etHora.text.toString()
            val novaDisciplina = etDisciplina.text.toString()

            val horarioAtualizado = horario.copy(
                data = novaData,
                hora = novaHora,
                disciplina = novaDisciplina
            )

            HorarioController.atualizarHorario(horarioAtualizado) { sucesso, msg ->
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
