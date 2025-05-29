package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.app_gerenciamento_aulas.controller.HorarioController
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class AdicionarMaterialActivity : AppCompatActivity() {

    private lateinit var layoutLinks: LinearLayout
    private lateinit var btnAdicionarLink: Button
    private lateinit var btnSalvar: Button
    private var horario: Horario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_material)

        layoutLinks = findViewById(R.id.linearLayoutLinks)
        btnAdicionarLink = findViewById(R.id.btnAdicionarLink)
        btnSalvar = findViewById(R.id.btnSalvar)

        val horarioId = intent.getStringExtra("horario_id")

        if (horarioId.isNullOrEmpty()) {
            Toast.makeText(this, "Horário inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Buscar o horário do Firebase
        HorarioDao.buscarPorId(horarioId) { horarioEncontrado ->
            if (horarioEncontrado != null) {
                horario = horarioEncontrado
            } else {
                Toast.makeText(this, "Horário não encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnAdicionarLink.setOnClickListener {
            adicionarCaixaTexto()
        }

        btnSalvar.setOnClickListener {
            salvarMateriais()
        }
    }

    private fun adicionarCaixaTexto() {
        val editText = EditText(this)
        editText.hint = "Digite o link do material"
        layoutLinks.addView(editText)
    }

    private fun salvarMateriais() {
        val links = mutableListOf<String>()
        for (i in 0 until layoutLinks.childCount) {
            val view = layoutLinks.getChildAt(i)
            if (view is EditText) {
                val link = view.text.toString().trim()
                if (link.isNotEmpty()) {
                    links.add(link)
                }
            }
        }

        val horarioAtual = horario
        if (horarioAtual != null) {
            HorarioController.adicionarMateriaisAoHorario(horarioAtual, links) { sucesso ->
                if (sucesso) {
                    Toast.makeText(this, "Materiais salvos!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Horário não carregado", Toast.LENGTH_SHORT).show()
        }
    }
}
