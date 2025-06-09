package br.edu.fatecpg.app_gerenciamento_aulas.notificacao

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log

class NotificacaoReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val disciplina = intent.getStringExtra("titulo") ?: "Aula"
        val aluno = intent.getStringExtra("aluno") ?: "Aluno"
        val professor = intent.getStringExtra("professor") ?: "Professor"

        val canalId = "aula_channel"

        // ✅ Cria o canal de notificação se necessário (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nome = "Notificações de Aulas"
            val descricao = "Lembretes de aulas agendadas"
            val importancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel(canalId, nome, importancia).apply {
                description = descricao
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }

        // ✅ Monta a notificação
        val builder = NotificationCompat.Builder(context, canalId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔔 Aula em breve!")
            .setContentText("A aula de '$disciplina' começa em 1 hora. Prof: $professor | Aluno: $aluno")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // ✅ Exibe
        with(NotificationManagerCompat.from(context)) {
            try {
                notify(System.currentTimeMillis().toInt(), builder.build())
            } catch (e: SecurityException) {
                Log.e("NotificacaoReceiver", "Erro ao mostrar notificação", e)
            }
        }
    }
}
