package ca.uwaterloo.flickpick

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseAuthentication {
    private val auth = FirebaseAuth.getInstance()

    fun createAccount(email: String, username: String, password: String, context: Context, navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    val userID = firebaseUser?.uid ?: ""
                    createUserBackend(email, username, userID, context, navController)
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Registration Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun createUserBackend(email: String, username: String, userID: String, context: Context, navController: NavController) {
        val user = User(email = email, username = username, userID = userID)
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseClient.apiService.createUser(user)
            withContext(Dispatchers.Main){
                navController.navigate("authenticated")
            }
        }
    }

    fun signIn(email: String, password: String, context: Context, navController: NavController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("authenticated")
                } else {
                    val errorMessage = task.exception?.message ?: "Sign In Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate("login") {
            popUpTo("authenticated") { inclusive = true }
        }
    }

    fun passwordReset(email: String, context: Context, navController: NavController) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password Reset Link Sent!", Toast.LENGTH_SHORT).show()
                    navController.navigate("authenticated")
                } else {
                    val errorMessage = task.exception?.message ?: "Password Reset Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

}