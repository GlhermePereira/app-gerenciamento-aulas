package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.UsuarioDao

object UserController {

    /**
     * Faz login e retorna resultado para a Activity
     */
    fun logar(email: String, senha: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        UsuarioDao.logar(email, senha) { sucesso, erro, tipoUsuario, uid ->
            // Pode incluir regras adicionais aqui, se quiser
            callback(sucesso, erro, tipoUsuario, uid)
        }
    }
}
