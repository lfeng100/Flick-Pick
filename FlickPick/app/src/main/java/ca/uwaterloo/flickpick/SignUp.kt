package ca.uwaterloo.flickpick


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.theme.Purple40
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavController) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }
    var confirmPassword = remember { mutableStateOf("") }
    var confirmPasswordVisible = remember { mutableStateOf(false) }
    val firebaseAuthentication = FirebaseAuthentication()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(305.dp),
            ) {
                Image(
                    painter = painterResource(id = ca.uwaterloo.flickpick.R.drawable.signup_header_image),
                    contentDescription = "Login Header Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(305.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
            }
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
                    Text(
                        text = "Signup",
                        fontSize = 42.sp,
                        //fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Email",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier
                            .height(46.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "", tint = Color.Gray) },
                        singleLine = true,
                        maxLines = 1,
                        minLines = 1,
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.Gray,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.DarkGray,
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Password",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        visualTransformation =
                        if (passwordVisible.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        modifier = Modifier
                            .height(46.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "", tint = Color.Gray) },
                        trailingIcon = {
                            val iconImage =
                                if (passwordVisible.value) {
                                    Icons.Filled.Visibility
                                } else {
                                    Icons.Filled.VisibilityOff
                                }
                            val description = ""
                            IconButton(
                                onClick = {
                                    passwordVisible.value = !passwordVisible.value
                                }
                            ) {
                                Icon(iconImage, contentDescription = description, tint = Color.Gray)
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        minLines = 1,
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.Gray,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.DarkGray,
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Confirm Password",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = confirmPassword.value,
                        onValueChange = { confirmPassword.value = it },
                        visualTransformation =
                        if (confirmPasswordVisible.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        modifier = Modifier
                            .height(46.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "", tint = Color.Gray) },
                        trailingIcon = {
                            val iconImage =
                                if (confirmPasswordVisible.value) {
                                    Icons.Filled.Visibility
                                } else {
                                    Icons.Filled.VisibilityOff
                                }
                            val description = ""
                            IconButton(
                                onClick = {
                                    confirmPasswordVisible.value = !confirmPasswordVisible.value
                                }
                            ) {
                                Icon(iconImage, contentDescription = description, tint = Color.Gray)
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        minLines = 1,
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.Gray,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.DarkGray,
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            if ((password.value == confirmPassword.value) &&
                                    email.value.isNotBlank() &&
                                    password.value.isNotBlank() &&
                                    confirmPassword.value.isNotBlank()) {
                                firebaseAuthentication.createAccount(email.value, password.value, context, navController)
                            } else {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40)
                    ) {
                        Text(
                            text = "Sign up",
                            fontSize = 20.sp,
                            //fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(65.dp))
                    Text(
                        text = "Already Have an Account?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        color = Purple40,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("login") },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



