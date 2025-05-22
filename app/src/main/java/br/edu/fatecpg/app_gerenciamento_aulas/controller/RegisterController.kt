package br.edu.fatecpg.app_gerenciamento_aulas.controller

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import br.edu.fatecpg.app_gerenciamento_aulas.model.User

class RegisterController(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registrar(user: User, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        onComplete(false, "Erro inesperado: usuário nulo após registro.")
                        return@addOnCompleteListener
                    }

                    // Salva dados adicionais no Firestore
                    firestore.collection("usuarios").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            onComplete(true, null)
                        }
                        .addOnFailureListener { e ->
                            onComplete(false, "Erro ao salvar dados: ${e.message}")
                        }

                } else {
                    onComplete(false, "Erro: ${task.exception?.message}")
                }
            }
    }
}
