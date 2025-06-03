package br.edu.fatecpg.app_gerenciamento_aulas.controller

import br.edu.fatecpg.app_gerenciamento_aulas.dao.UsuarioDao

object UserController {

    private var usuarioIdAtual: String? = null
    private var tipoUsuarioAtual: String? = null


    fun logar(email: String, senha: String, callback: (Boolean, String?, String?, String?) -> Unit) {
        UsuarioDao.logar(email, senha) { sucesso, erro, tipoUsuario, uid ->
            if (sucesso && uid != null && tipoUsuario != null) {
                usuarioIdAtual = uid
                tipoUsuarioAtual = tipoUsuario
            }
            callback(sucesso, erro, tipoUsuario, uid)
        }
    }

    fun getUsuarioIdAtual(): String? = usuarioIdAtual

    fun getTipoUsuarioAtual(): String? = tipoUsuarioAtual

    fun logout() {
        usuarioIdAtual = null
        tipoUsuarioAtual = null
    }
}
