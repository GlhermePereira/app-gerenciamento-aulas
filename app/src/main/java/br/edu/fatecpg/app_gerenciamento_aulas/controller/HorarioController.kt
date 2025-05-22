package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

object HorarioController {

    /**
     * Cria um novo horário disponível para agendamento
     */
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

    /**
     * Exclui um horário pelo ID
     */
    fun deletarHorario(
        horarioId: String,
        callback: (Boolean, String) -> Unit
    ) {
        HorarioDao.excluir(horarioId) { sucesso ->
            if (sucesso) callback(true, "")
            else callback(false, "Falha ao excluir horário")
        }
    }

    /**
     * Atualiza um horário existente
     */
    fun atualizarHorario(
        horario: Horario,
        callback: (Boolean, String) -> Unit
    ) {
        HorarioDao.atualizar(horario) { sucesso ->
            if (sucesso) callback(true, "")
            else callback(false, "Falha ao atualizar horário")
        }
    }
}
