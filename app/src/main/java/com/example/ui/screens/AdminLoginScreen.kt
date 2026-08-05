package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.CoralRed
import com.example.ui.theme.LightBlueBg
import com.example.ui.viewmodel.StoryViewModel

@Composable
fun AdminLoginScreen(
    viewModel: StoryViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("admin@golpoghor.com") }
    var password by remember { mutableStateOf("123456") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val loginError by viewModel.loginError.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1117))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📚 GolpoGhor",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2B37E)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Admin Dashboard Login",
                    fontSize = 15.sp,
                    color = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Email field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Email", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFFF3F4F6))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_email_input"),
                        placeholder = { Text("admin@example.com", color = Color(0xFF6B7280)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0F1117),
                            unfocusedContainerColor = Color(0xFF0F1117),
                            focusedTextColor = Color(0xFFF3F4F6),
                            unfocusedTextColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFFE2B37E),
                            unfocusedBorderColor = Color(0xFF282C3A)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Password field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFFF3F4F6))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input"),
                        placeholder = { Text("********", color = Color(0xFF6B7280)) },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password",
                                    tint = Color(0xFF9CA3AF)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0F1117),
                            unfocusedContainerColor = Color(0xFF0F1117),
                            focusedTextColor = Color(0xFFF3F4F6),
                            unfocusedTextColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFFE2B37E),
                            unfocusedBorderColor = Color(0xFF282C3A)
                        )
                    )
                }

                if (loginError != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = loginError!!,
                        color = Color(0xFFF43F5E),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = {
                        val success = viewModel.login(email, password)
                        if (success) {
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("admin_login_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF141622)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "© 2026 GolpoGhor Admin",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}
