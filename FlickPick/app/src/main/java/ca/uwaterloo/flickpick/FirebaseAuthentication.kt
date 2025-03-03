package ca.uwaterloo.flickpick

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthentication {
    private val auth = FirebaseAuth.getInstance()

    fun createAccount(email: String, password: String, context: Context, navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                } else {
                    val errorMessage = task.exception?.message ?: "Registration Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun signIn(email: String, password: String, context: Context, navController: NavController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                } else {
                    val errorMessage = task.exception?.message ?: "Sign In Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    fun passwordReset(email: String, context: Context, navController: NavController) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password Reset Link Sent!", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                } else {
                    val errorMessage = task.exception?.message ?: "Password Reset Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

}