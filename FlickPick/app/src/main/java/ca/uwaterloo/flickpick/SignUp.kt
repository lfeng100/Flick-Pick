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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavController) {
    var firstName = remember { mutableStateOf("") }
    var lastName = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }
    val firebaseAuthentication = FirebaseAuthentication()
    val context = LocalContext.current
    val fieldSpacing = 12.dp
    val labelSpacing = 10.dp
    var isLoading = remember { mutableStateOf(false) }

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
                val scrollState  = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .padding(start = 48.dp, end = 48.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Sign Up",
                        fontSize = 42.sp,
                        //fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Text(
                        text = "First Name",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(labelSpacing))
                    OutlinedTextField(
                        value = firstName.value,
                        onValueChange = { firstName.value = it },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_abc_24), contentDescription = "", tint = Color.Gray) },
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
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Text(
                        text = "Last Name",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(labelSpacing))
                    OutlinedTextField(
                        value = lastName.value,
                        onValueChange = { lastName.value = it },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_abc_24), contentDescription = "", tint = Color.Gray) },
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
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Text(
                        text = "Email",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(labelSpacing))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier
                            .height(48.dp)
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
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Text(
                        text = "Username",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(labelSpacing))
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = { Icon(imageVector = Icons.Default.AccountCircle , contentDescription = "", tint = Color.Gray) },
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
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Text(
                        text = "Password",
                        fontSize = 17.sp,
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.height(labelSpacing))
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
                            .height(48.dp)
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
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            if (firstName.value.isNotBlank() &&
                                lastName.value.isNotBlank() &&
                                email.value.isNotBlank() &&
                                username.value.isNotBlank() &&
                                password.value.isNotBlank()
                                ) {
                                isLoading.value = true
                                firebaseAuthentication.createAccount(firstName.value, lastName.value, email.value, username.value, password.value, context, navController)
                            } else {
                                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        if (isLoading.value) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "Sign up",
                                fontSize = 20.sp,
                                //fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(58.dp))
                    Text(
                        text = "Already Have an Account?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(3.dp))
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



