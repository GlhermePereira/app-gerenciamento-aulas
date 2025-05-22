package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.ProfessorDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

object HorarioController {

    fun criarHorarioDisponivel(
        data: String,
        hora: String,
        disciplina: String,
        professorId: String,
        professorNome: String,
        callback: (Boolean, String) -> Unit
    ) {
        val horario = Horario(
            id = "", // Será preenchido pelo DAO
            data = data,
            hora = hora,
            disciplina = disciplina,
            professorId = professorId,
            professorNome = professorNome,
            disponivel = true
        )
        HorarioDao.adicionar(horario) { sucesso ->
            if (sucesso) callback(true, "")
            else callback(false, "Falha ao salvar horário")
        }
    }

    /**
     * Lista todos os horários de um professor
     */
    fun listarHorarios(
        professorId: String,
        callback: (List<Horario>) -> Unit
    ) {
        HorarioDao.listarDoProfessor(professorId, callback)
    }
}

