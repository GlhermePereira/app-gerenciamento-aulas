package br.edu.fatecpg.app_gerenciamento_aulas

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TelaProfessorActivity : AppCompatActivity() {

    private lateinit var btnDefinirHorario: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_professor)

        auth = FirebaseAuth.getInstance()

        btnDefinirHorario = findViewById(R.id.btnDefinirHorario)

        btnDefinirHorario.setOnClickListener {
            // Aqui você pode abrir um DatePicker ou outro método para definir o horário
            Toast.makeText(this, "Abrir tela para definir horário...", Toast.LENGTH_SHORT).show()
        }
    }
}
