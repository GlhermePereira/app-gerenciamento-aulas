package br.edu.fatecpg.app_gerenciamento_aulas.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import br.edu.fatecpg.app_gerenciamento_aulas.Login
import br.edu.fatecpg.app_gerenciamento_aulas.MinhasAulasActivity
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.ListarAgendamentos
import br.edu.fatecpg.app_gerenciamento_aulas.controller.UserController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.app.AlarmManager
import android.content.Context
import android.provider.Settings


class AlunoActivity : AppCompatActivity() {

    private lateinit var cardVerHorarios: CardView
    private lateinit var cardMinhasAulas: CardView
    private lateinit var cardMateriais: CardView
    private lateinit var btnSair: Button
    private lateinit var tvBoasVindas: TextView
    private val NOTIFICATION_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)
        verificarPermissaoExatoAlarme()
        tvBoasVindas = findViewById(R.id.tvBoasVindas)
        cardVerHorarios = findViewById(R.id.btnVerAgendamentos)
        cardMinhasAulas = findViewById(R.id.btnMinhasAulas)
        btnSair = findViewById(R.id.btnSair)
        var alunoId = ""

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseFirestore.getInstance()
            alunoId = it.uid

            db.collection("usuarios").document(alunoId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: "Aluno"
                        tvBoasVindas.text = "Olá, $nome!"
                    } else {
                        tvBoasVindas.text = "Olá, Aluno!"
                    }
                }
                .addOnFailureListener {
                    tvBoasVindas.text = "Olá, Aluno!"
                }
        }

        cardVerHorarios.setOnClickListener{
            val intent = Intent(this, ListarAgendamentos::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }

        cardMinhasAulas.setOnClickListener {
            val intent = Intent(this, MinhasAulasActivity::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }




        btnSair.setOnClickListener {
            UserController.logout()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    private fun verificarPermissaoExatoAlarme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão de notificação concedida", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Permissão de notificação negada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}