package br.edu.fatecpg.app_gerenciamento_aulas.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UsuarioDao {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Faz o login do usuário usando FirebaseAuth e busca o tipo no Firestore.
     * callback(success, errorMessage, tipoUsuario, uid)
     */
    fun logar(
        email: String,
        senha: String,
        callback: (Boolean, String?, String?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        callback(false, "Usuário não encontrado", null, null)
                        return@addOnCompleteListener
                    }
                    // Busca tipo do usuário
                    firestore.collection("usuarios")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            val tipo = doc.getString("tipo")
                            if (tipo == null) {
                                callback(false, "Tipo de usuário não definido", null, null)
                            } else {
                                callback(true, null, tipo, uid)
                            }
                        }
                        .addOnFailureListener {
                            callback(false, "Falha ao buscar dados do usuário", null, null)
                        }
                } else {
                    callback(false, task.exception?.message ?: "Falha no login", null, null)
                }
            }
    }
}
