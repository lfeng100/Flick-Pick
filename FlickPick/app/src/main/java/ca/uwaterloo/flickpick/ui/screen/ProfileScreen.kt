package ca.uwaterloo.flickpick.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import ca.uwaterloo.flickpick.FirebaseAuthentication
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.UserUpdate
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(mainNavController: NavController, loginNavController: NavController) {
    val firebaseAuthentication = FirebaseAuthentication()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    var originalFirstName by remember { mutableStateOf("") }
    var originalLastName by remember { mutableStateOf("") }
    var originalUsername by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf<String>("") }
    var lastName by remember { mutableStateOf<String>("") }
    var username by remember { mutableStateOf<String>("") }
    var email by remember { mutableStateOf<String>("") }
//    var password = remember { mutableStateOf("mypassword") }

    val userId = PrimaryUserRepository.getPrimaryUserID()
    val coroutineScope = rememberCoroutineScope()

    if (userId == null) {
        LaunchedEffect(Unit) {
            loginNavController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
        return
    }

    var updateMessage by remember { mutableStateOf<String?>(null) }
    var passwordMessage by remember { mutableStateOf<String?>(null) }
    var deleteConfirmText by remember { mutableStateOf("") }
    var showDeleteButton by remember { mutableStateOf(false) }
    var deleteClickedOnce by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                Log.d("ProfileScreen", "user id: $userId")
                val user = DatabaseClient.apiService.getUserById(userId)
                originalFirstName = user.firstName
                originalLastName = user.lastName
                originalUsername = user.username
                firstName = user.firstName
                lastName = user.lastName
                username = user.username
                email = user.email
                Log.d("ProfileScreen", "User name: $username")
            } catch (e: Exception) {
                Log.e("JoinGroupScreen", "Error fetching user", e)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 48.dp, end = 48.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Profile",
                            fontSize = 42.sp,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                        )
                        IconButton(
                            onClick =
                            {
                                firebaseAuthentication.signOut(mainNavController, loginNavController)
                            },
                            modifier = Modifier
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }

                    UserInfoCard(
                        firstName = originalFirstName,
                        lastName = originalLastName,
                        username = originalUsername,
                        email = email
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(text = "Update Info", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    DatabaseClient.apiService.updateUser(
                                        userId,
                                        UserUpdate(
                                            firstName = firstName,
                                            lastName = lastName,
                                            username = username)
                                    )
                                    updateMessage = "Changes saved successfully"

                                    originalFirstName = firstName
                                    originalLastName = lastName
                                    originalUsername = username
                                } catch (e: Exception) {
                                    updateMessage = "Failed to update profile, username may already exist"

                                    firstName = originalFirstName
                                    lastName = originalLastName
                                    username = originalUsername
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }

                    updateMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = if (it.contains("Failed")) Color.Red else Color.Green)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Change Password")
                    }
                    if (showDialog) {
                        ChangePasswordDialog(
                            showDialog = showDialog,
                            onDismissRequest = { showDialog = false },
                            onChangePassword = { password ->
                                if (password.isNotBlank()) {
                                    firebaseAuthentication.updatePassword(password, context)
                                    showDialog = false
                                } else {
                                    Toast.makeText(context, "Password is missing", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                    passwordMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = if (it.contains("Failed")) Color.Red else Color.Green)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "Delete Account", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = deleteConfirmText,
                        onValueChange = {
                            deleteConfirmText = it
                            showDeleteButton = it == "DELETE"
                        },
                        label = { Text("Type DELETE to confirm") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (showDeleteButton) {
                        Button(
                            onClick = {
                                if (!deleteClickedOnce) {
                                    deleteClickedOnce = true

                                    coroutineScope.launch {
                                        kotlinx.coroutines.delay(5000)
                                        deleteClickedOnce = false
                                    }
                                } else {
                                    coroutineScope.launch {
                                        try {
                                            firebaseAuthentication.deleteAccount(loginNavController, context)
                                            DatabaseClient.apiService.deleteUser(userId)
                                        } catch (e: Exception) {
                                            Log.e("ProfileScreen", "Delete failed", e)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (deleteClickedOnce) Color(0xFFB71C1C) else Color.Red
                            )
                        ) {
                            if (deleteClickedOnce) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = Color.White,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("WARNING: Confirm Account Deletion", color = Color.White)
                            } else {
                                Text("Permanently Delete Account", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun UserInfoCard(
    firstName: String,
    lastName: String,
    username: String,
    email: String
) {
    val infiniteTransition = rememberInfiniteTransition()
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "gradient"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF3F51B5), Color(0xFF2196F3)),
        start = Offset(0f, 0f),
        end = Offset(gradientOffset * 1000f, gradientOffset * 1000f)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "My FlickPick ID",
        fontSize = 17.sp,
        color = Color.Gray,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(text = "$firstName $lastName", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Username: $username", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ChangePasswordDialog(showDialog: Boolean, onDismissRequest: () -> Unit, onChangePassword: (String) -> Unit) {
    var password = remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Update Your Password",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enter a new, unique password below, different from any used before",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Enter New Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onChangePassword(password.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40)
                    ) {
                        Text(
                            text = "Update Password",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}





























