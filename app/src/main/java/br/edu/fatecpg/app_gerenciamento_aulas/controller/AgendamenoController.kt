package br.edu.fatecpg.app_gerenciamento_aulas.controller
import android.os.Build
import androidx.annotation.RequiresApi
import br.edu.fatecpg.app_gerenciamento_aulas.dao.AlunoDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material
import java.time.LocalDate

object AgendamentoController {

    // Criar um novo agendamento
    fun agendarAula(horario: Horario, alunoId: String, callback: (Boolean) -> Unit) {
        AlunoDao.criarAgendamento(horario, alunoId) { sucesso ->
            callback(sucesso)
        }
    }

    // Buscar aulas agendadas do aluno
    fun buscarAulasAgendadas(alunoId: String, callback: (List<Agendamento>) -> Unit) {
        AlunoDao.buscarAulasAgendadas(alunoId) { agendamentos ->
            callback(agendamentos)
        }
    }

    // Buscar materiais da aula de hoje
    @RequiresApi(Build.VERSION_CODES.O)
    fun buscarMateriaisDeHoje(alunoId: String, callback: (List<Material>) -> Unit) {
        val hoje = LocalDate.now()
        AlunoDao.buscarMateriaisPorData(alunoId, hoje) { materiais ->
            callback(materiais)
        }
    }
}
