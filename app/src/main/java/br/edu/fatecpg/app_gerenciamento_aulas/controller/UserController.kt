package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.UsuarioDao

object UserController {

    private var usuarioIdAtual: String? = null

    /**
     * Faz login e retorna resultado para a Activity
     */
    fun logar(email: String, senha: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        UsuarioDao.logar(email, senha) { sucesso, erro, tipoUsuario, uid ->
            if (sucesso && uid != null) {
                usuarioIdAtual = uid
            }
            callback(sucesso, erro, tipoUsuario, uid)
        }
    }

    /**
     * Retorna o ID do usuário atualmente logado
     */
    fun getUsuarioIdAtual(): String? {
        return usuarioIdAtual
    }
}
