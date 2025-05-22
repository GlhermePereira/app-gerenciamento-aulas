package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.fatecpg.app_gerenciamento_aulas.controller.ProfessorController
import com.google.firebase.auth.FirebaseAuth

class ProfessorActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var professorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor)

        auth = FirebaseAuth.getInstance()
        professorId = auth.currentUser?.uid ?: ""

        // Exemplo de chamada:
        ProfessorController.listarHorarios(professorId) { horarios ->
            // atualizar UI
        }
    }
}
