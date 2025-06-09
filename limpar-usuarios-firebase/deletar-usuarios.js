const admin = require("firebase-admin");
const fs = require("fs");

// Carrega a chave do Firebase Admin
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

async function deletarTodosUsuarios() {
  try {
    let nextPageToken;
    let totalDeletados = 0;

    do {
      const listResult = await admin.auth().listUsers(1000, nextPageToken);

      const deletarPromises = listResult.users.map(user => {
        console.log("Deletando:", user.email || user.uid);
        return admin.auth().deleteUser(user.uid);
      });

      await Promise.all(deletarPromises);
      totalDeletados += deletarPromises.length;

      nextPageToken = listResult.pageToken;
    } while (nextPageToken);

    console.log(`✅ ${totalDeletados} usuários deletados com sucesso.`);
  } catch (error) {
    console.error("❌ Erro ao deletar usuários:", error);
  }
}

deletarTodosUsuarios();

