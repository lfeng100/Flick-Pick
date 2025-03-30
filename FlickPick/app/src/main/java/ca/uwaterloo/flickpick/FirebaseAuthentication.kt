package ca.uwaterloo.flickpick

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.dataObjects.Database.Models.UserCreate
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseAuthentication {
    private val auth = FirebaseAuth.getInstance()

    fun createAccount(firstName: String, lastName: String, email: String, username: String, password: String, context: Context, navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    val userID = firebaseUser?.uid ?: ""
                    createUserBackend(firstName, lastName, email, username, userID, context, navController)
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Registration Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun createUserBackend(firstName: String, lastName: String, email: String, username: String, userID: String, context: Context, navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseClient.apiService.createUser(
                UserCreate(
                    username = username,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    userID = userID
                )
            )
            withContext(Dispatchers.Main){
                navController.navigate("login")
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

    fun signOut(navController: NavController, loginNavController: NavController) {
        auth.signOut()
        navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
        loginNavController.navigate("login") {
            popUpTo("login") { inclusive = true }
        }
    }

    fun passwordReset(email: String, context: Context, navController: NavController) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password Reset Link Sent!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                } else {
                    val errorMessage = task.exception?.message ?: "Password Reset Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun updatePassword(password: String, context: Context) {
        val user = auth.currentUser
        val newPassword = password

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Your password has been updated!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Password Update Failed"
                    Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }
}