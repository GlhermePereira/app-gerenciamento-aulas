package br.edu.fatecpg.app_gerenciamento_aulas.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import br.edu.fatecpg.app_gerenciamento_aulas.notificacao.NotificacaoReceiver

object NotificacaoUtil {
    @SuppressLint("ScheduleExactAlarm")
    fun agendarNotificacao(
        context: Context,
        disciplina: String,
        nomeAluno: String,
        nomeProfessor: String,
        horarioNotificacaoAula: Long,
    ) {
        val intent = Intent(context, NotificacaoReceiver::class.java).apply {
            putExtra("titulo", disciplina)
            putExtra("aluno", nomeAluno)
            putExtra("professor", nomeProfessor)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            disciplina.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Calcular horário de disparo
        val triggerTime = horarioNotificacaoAula

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}
