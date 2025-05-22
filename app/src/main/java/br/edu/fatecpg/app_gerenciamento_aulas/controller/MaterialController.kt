package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.ProfessorDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Material

object MaterialController {

    // Enviar material para uma aula
    fun enviarMaterial(material: Material, callback: (Boolean) -> Unit) {
        ProfessorDao.enviarMaterial(material) { sucesso ->
            callback(sucesso)
        }
    }
}
