package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.CoralRed
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: StoryViewModel,
    onBackClick: () -> Unit
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚙️ Settings", color = Color(0xFFF3F4F6)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFF3F4F6))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141622))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F1117))
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "অ্যাপ তথ্য", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFE2B37E))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "GolpoGhor v1.0", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFFF3F4F6))
                    Text(text = "বাংলা গল্প পড়া ও প্রকাশের প্ল্যাটফর্ম", fontSize = 13.sp, color = Color(0xFF9CA3AF))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "© 2026 GolpoGhor Admin", fontSize = 12.sp, color = Color(0xFF6B7280))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoggedIn) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "এডমিন একাউন্ট", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFE2B37E))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "ইমেইল: admin@golpoghor.com", fontSize = 14.sp, color = Color(0xFFF3F4F6))
                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.logout()
                                onBackClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF43F5E)),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("🚪 Admin Logout", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
