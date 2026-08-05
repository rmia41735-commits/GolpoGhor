package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.CoralRed
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    storyId: Long,
    viewModel: StoryViewModel,
    onBackClick: () -> Unit,
    onReadChapterClick: (Int) -> Unit
) {
    val story by viewModel.getStoryById(storyId).collectAsStateWithLifecycle(initialValue = null)
    val chapters by viewModel.getChaptersForStory(storyId).collectAsStateWithLifecycle(initialValue = emptyList())
    val comments by viewModel.getCommentsForStory(storyId).collectAsStateWithLifecycle(initialValue = emptyList())
    val isBookmarked by viewModel.isBookmarked(storyId).collectAsStateWithLifecycle(initialValue = false)

    var commentText by remember { mutableStateOf("") }
    var userNameText by remember { mutableStateOf("") }

    LaunchedEffect(storyId) {
        viewModel.incrementViews(storyId)
    }

    Scaffold(
                topBar = {
            TopAppBar(
                title = { Text(story?.title ?: "গল্পের বিবরণ", color = Color(0xFFF3F4F6)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFF3F4F6)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.toggleBookmark(storyId, isBookmarked)
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color(0xFFE2B37E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141622))
            )
        }
    ) { innerPadding ->
        val currentStory = story
        if (currentStory == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF0F1117)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "গল্প লোড হচ্ছে...", color = Color(0xFF9CA3AF))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF0F1117)),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Header Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF141622), Color(0xFF1A1D2A))
                                )
                            )
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Cover Image
                            Box(
                                modifier = Modifier
                                    .size(width = 130.dp, height = 180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF26211B)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (currentStory.coverResId != null) {
                                    Image(
                                        painter = painterResource(id = currentStory.coverResId),
                                        contentDescription = currentStory.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize()
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "Book",
                                        tint = Color(0xFFE2B37E),
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentStory.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF3F4F6)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF26211B))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentStory.category,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE2B37E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Quick Action Bar
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Stats Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Visibility, contentDescription = "Views", tint = Color(0xFF818CF8), modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "${currentStory.views}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFF3F4F6))
                                    }
                                    Text(text = "ভিউ", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Likes", tint = Color(0xFFF43F5E), modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "${currentStory.likes}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFF3F4F6))
                                    }
                                    Text(text = "লাইক", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "📖 ${chapters.size}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFF3F4F6))
                                    }
                                    Text(text = "অধ্যায়", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Start Reading Main CTA Button
                            Button(
                                onClick = { onReadChapterClick(0) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("start_reading_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color(0xFF141622))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🚀 পড়া শুরু করুন",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF141622)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Like Button
                            Button(
                                onClick = { viewModel.incrementLikes(storyId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("like_story_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261D22)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like", tint = Color(0xFFF43F5E))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "গল্পে লাইক দিন", color = Color(0xFFF43F5E), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Story Summary Section
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "সংক্ষিপ্ত বিবরণ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF3F4F6)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentStory.summary,
                                fontSize = 15.sp,
                                color = Color(0xFF9CA3AF),
                                lineHeight = 22.sp
                            )

                            if (currentStory.tags.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "ট্যাগ: ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                                    Text(text = currentStory.tags, fontSize = 13.sp, color = Color(0xFFE2B37E))
                                }
                            }
                        }
                    }
                }

                // Chapters Section
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "📚 অধ্যায় তালিকা (${chapters.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                items(chapters.indices.toList()) { index ->
                    val chapter = chapters[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable { onReadChapterClick(index) }
                            .testTag("chapter_item_$index"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF26211B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${chapter.chapterNumber}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE2B37E)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = chapter.chapterTitle,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFF3F4F6)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Read",
                                tint = Color(0xFFE2B37E)
                            )
                        }
                    }
                }

                // Comments Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "💬 মন্তব্যসমূহ (${comments.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "আপনার মতামত লিখুন", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFF3F4F6))
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = userNameText,
                                onValueChange = { userNameText = it },
                                placeholder = { Text("আপনার নাম (ঐচ্ছিক)", color = Color(0xFF6B7280)) },
                                modifier = Modifier.fillMaxWidth().testTag("comment_name_input"),
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

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text("আপনার মূল্যবান মন্তব্যটি লিখুন...", color = Color(0xFF6B7280)) },
                                modifier = Modifier.fillMaxWidth().testTag("comment_text_input"),
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0F1117),
                                    unfocusedContainerColor = Color(0xFF0F1117),
                                    focusedTextColor = Color(0xFFF3F4F6),
                                    unfocusedTextColor = Color(0xFFF3F4F6),
                                    focusedBorderColor = Color(0xFFE2B37E),
                                    unfocusedBorderColor = Color(0xFF282C3A)
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    if (commentText.isNotBlank()) {
                                        viewModel.addComment(storyId, userNameText, commentText)
                                        commentText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2B37E)),
                                modifier = Modifier.align(Alignment.End).testTag("submit_comment_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = "Post", tint = Color(0xFF141622))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("মন্তব্য পোস্ট করুন", color = Color(0xFF141622), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Comment List Items
                items(comments) { com ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "👤 ${com.userName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFFE2B37E)
                                )
                                Text(
                                    text = com.date,
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = com.commentText,
                                fontSize = 14.sp,
                                color = Color(0xFFF3F4F6)
                            )
                        }
                    }
                }
            }
        }
    }
}
