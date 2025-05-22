package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.AulaDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.MaterialDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Aula
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material

object ProfessorController {
    fun adicionarHorario(professorId: String, dataHora: String, onComplete: (Boolean) -> Unit) {
        val horario = Horario(
            professorId = professorId,
            hora = dataHora
        )
        HorarioDao.adicionar(horario, onComplete)
    }

    fun listarHorarios(professorId: String, onResult: (List<Horario>) -> Unit) {
        HorarioDao.listarDoProfessor(professorId, onResult)
    }

    fun listarAulas(professorId: String, onResult: (List<Aula>) -> Unit) {
        AulaDao.listarPorProfessor(professorId, onResult)
    }

    fun enviarMaterial(agendamentoId: String, titulo: String, link: String, onComplete: (Boolean) -> Unit) {
        val material = Material(
            agendamentoId = agendamentoId,
            titulo = titulo,
            link = link
        )
        MaterialDao.enviar(material, onComplete)
    }
}
