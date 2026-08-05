package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StoryEntity
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: StoryViewModel,
    onAddNewStoryClick: () -> Unit,
    onEditStoryClick: (Long) -> Unit,
    onManageCommentsClick: () -> Unit,
    onManageCategoriesClick: () -> Unit
) {
    val stories by viewModel.allStoriesForAdmin.collectAsStateWithLifecycle()
    val totalViews by viewModel.totalViews.collectAsStateWithLifecycle()
    val totalLikes by viewModel.totalLikes.collectAsStateWithLifecycle()
    val totalComments by viewModel.totalCommentsCount.collectAsStateWithLifecycle()

    var storyToDelete by remember { mutableStateOf<StoryEntity?>(null) }
    var adminSearch by remember { mutableStateOf("") }

    val filteredStories = if (adminSearch.isBlank()) stories else stories.filter {
        it.title.contains(adminSearch, ignoreCase = true) || it.category.contains(adminSearch, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1117)),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Topbar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dashboard",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3F4F6)
                    )
                    Text(
                        text = "স্বাগতম, Admin 👋",
                        fontSize = 15.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                Button(
                    onClick = { viewModel.logout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF43F5E)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }

        // Metrics Cards Grid
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Stories
                    DashboardStatCard(
                        title = "মোট গল্প",
                        value = "${stories.size}",
                        valueColor = Color(0xFFE2B37E),
                        modifier = Modifier.weight(1f)
                    )

                    // Total Views
                    val formattedViews = if ((totalViews ?: 0) >= 1000) "${(totalViews ?: 0) / 1000}K" else "${totalViews ?: 0}"
                    DashboardStatCard(
                        title = "মোট ভিউ",
                        value = formattedViews,
                        valueColor = Color(0xFF818CF8),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Likes
                    val formattedLikes = if ((totalLikes ?: 0) >= 1000) "${(totalLikes ?: 0) / 1000}K" else "${totalLikes ?: 0}"
                    DashboardStatCard(
                        title = "মোট লাইক",
                        value = formattedLikes,
                        valueColor = Color(0xFFF43F5E),
                        modifier = Modifier.weight(1f)
                    )

                    // Total Comments
                    DashboardStatCard(
                        title = "মোট মন্তব্য",
                        value = "$totalComments",
                        valueColor = Color(0xFFE2B37E),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Quick Actions
        item {
            Column {
                Text(
                    text = "দ্রুত অ্যাকশন",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3F4F6),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAddNewStoryClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("quick_action_add_story")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF141622))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "নতুন গল্প", fontSize = 13.sp, color = Color(0xFF141622), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onManageCommentsClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("quick_action_comments")
                    ) {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = "Comments", tint = Color(0xFF141622))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "মন্তব্য", fontSize = 13.sp, color = Color(0xFF141622), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onManageCategoriesClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("quick_action_categories")
                    ) {
                        Icon(imageVector = Icons.Default.Category, contentDescription = "Categories", tint = Color(0xFF141622))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "ক্যাটাগরি", fontSize = 13.sp, color = Color(0xFF141622), fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Stories Table Title & Search
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সর্বশেষ প্রকাশিত গল্প",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF3F4F6)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = adminSearch,
                        onValueChange = { adminSearch = it },
                        placeholder = { Text("গল্প ফিল্টার করুন...", color = Color(0xFF6B7280)) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_dashboard_search"),
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF202330))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "শিরোনাম", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFF3F4F6), modifier = Modifier.weight(2.5f))
                        Text(text = "ক্যাটাগরি", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFF3F4F6), modifier = Modifier.weight(1.5f))
                        Text(text = "ভিউ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFF3F4F6), modifier = Modifier.weight(1f))
                        Text(text = "অ্যাকশন", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFF3F4F6), modifier = Modifier.weight(1.5f))
                    }

                    Divider(color = Color(0xFF282C3A))

                    if (filteredStories.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "কোনো গল্প পাওয়া যায়নি", color = Color(0xFF9CA3AF))
                        }
                    } else {
                        filteredStories.forEach { story ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(2.5f)) {
                                    Text(
                                        text = story.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFFF3F4F6)
                                    )
                                    Text(
                                        text = if (story.isDraft) "💾 খসড়া (Draft)" else story.datePublished,
                                        fontSize = 11.sp,
                                        color = if (story.isDraft) Color(0xFFF43F5E) else Color(0xFF9CA3AF)
                                    )
                                }

                                Text(
                                    text = story.category,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE2B37E),
                                    modifier = Modifier.weight(1.5f)
                                )

                                Text(
                                    text = "${story.views}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.weight(1f)
                                )

                                Row(modifier = Modifier.weight(1.5f)) {
                                    IconButton(
                                        onClick = { onEditStoryClick(story.id) },
                                        modifier = Modifier.size(28.dp).testTag("edit_story_${story.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color(0xFF10B981)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    IconButton(
                                        onClick = { storyToDelete = story },
                                        modifier = Modifier.size(28.dp).testTag("delete_story_${story.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFF43F5E)
                                        )
                                    }
                                }
                            }
                            Divider(color = Color(0xFF282C3A))
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    storyToDelete?.let { story ->
        AlertDialog(
            onDismissRequest = { storyToDelete = null },
            title = { Text("গল্পটি মুছে ফেলতে চান?") },
            text = { Text("’${story.title}’ গল্পটি মুছে ফেলা হলে তা আর ফেরত পাওয়া যাবে না।") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteStory(story.id)
                        storyToDelete = null
                    }
                ) {
                    Text("মুছে ফেলুন", color = CoralRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { storyToDelete = null }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
