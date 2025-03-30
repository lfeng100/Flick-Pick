package ca.uwaterloo.flickpick

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController) {
    var email = remember { mutableStateOf("") }
    val firebaseAuthentication = FirebaseAuthentication()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(45.dp))
            Image(
                painter = painterResource(id = ca.uwaterloo.flickpick.R.drawable.lock_icon),
                contentDescription = "Lock Icon Image",
                modifier = Modifier
                    .size(115.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Forgot Password?",
                fontSize = 27.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Please enter a valid email below to receive password reset instructions:",
                fontSize = 17.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(54.dp))
            Text(
                text = "Email",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(9.dp))
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
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
            Spacer(modifier = Modifier.height(38.dp))
            Button(
                onClick = {
                    if (email.value.isNotBlank()) {
                        firebaseAuthentication.passwordReset(email.value, context, navController)
                    } else {
                        Toast.makeText(context, "Email is missing", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .padding(horizontal = 6.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Text(
                    text = "Send Email",
                    fontSize = 20.sp,
                    //fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}