package br.edu.fatecpg.app_gerenciamento_aulas.dao
import br.edu.fatecpg.app_gerenciamento_aulas.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("usuarios")

    fun saveUser(user: User, onComplete: (Boolean, Exception?) -> Unit) {
        usersCollection.document(user.email)
            .set(user)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful, task.exception)
            }
    }

    fun getUserById(id: String, onComplete: (User?) -> Unit) {
        usersCollection.document(id).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    onComplete(user)
                } else {
                    onComplete(null)
                }
            }
    }

    // Outros métodos como atualizar, deletar, etc.
}
