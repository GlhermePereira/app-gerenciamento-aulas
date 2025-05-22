package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.AlunoDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento
import java.time.LocalDate

object AlunoController {

    fun listarHorariosDisponiveis(callback: (List<Horario>) -> Unit) {
        AlunoDao.buscarHorariosDisponiveis(callback)
    }

    fun agendarAula(horario: Horario, alunoId: String, callback: (Boolean) -> Unit) {
        AlunoDao.criarAgendamento(horario, alunoId, callback)
    }

    fun listarAulasAgendadas(alunoId: String, callback: (List<Agendamento>) -> Unit) {
        AlunoDao.buscarAulasAgendadas(alunoId, callback)
    }

    fun listarMateriaisHoje(alunoId: String, data: LocalDate, callback: (List<Material>) -> Unit) {
        AlunoDao.buscarMateriaisPorData(alunoId, data, callback)
    }
}
