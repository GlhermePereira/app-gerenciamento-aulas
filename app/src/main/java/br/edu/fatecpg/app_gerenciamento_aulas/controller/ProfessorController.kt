package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.AulaDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.MaterialDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Aula
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material

object ProfessorController {
    fun adicionarHorario(
        id: String = "", // o DAO vai gerar isso
        data: String,
        hora: String,
        disciplina: String,
        professorId: String,
        professorNome: String,
        disponivel: Boolean = true,
        onComplete: (Boolean) -> Unit
    ) {
        val horario = Horario(
            id = id, // normalmente será "" e o DAO gera o ID real
            data = data,
            hora = hora,
            disciplina = disciplina,
            professorId = professorId,
            professorNome = professorNome,
            disponivel = disponivel
        )
        HorarioDao.adicionar(horario, onComplete)
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
