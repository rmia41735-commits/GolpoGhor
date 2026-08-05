package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.ui.components.StoryCard
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: StoryViewModel,
    onStoryClick: (Long) -> Unit
) {
    val stories by viewModel.publishedStories.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    val bookmarkedIds = bookmarks.map { it.storyId }.toSet()
    val bookmarkedStories = stories.filter { bookmarkedIds.contains(it.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📚 আমার লাইব্রেরি", color = Color(0xFFF3F4F6)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141622))
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F1117)),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "সংরক্ষিত গল্পসমূহ (${bookmarkedStories.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3F4F6)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (bookmarkedStories.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🔖", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "কোনো গল্প বুকমার্ক করা হয়নি",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF3F4F6)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "পছন্দের গল্পের বুকমার্ক বাটনে ট্যাপ করে এখানে সেভ করুন",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }
            } else {
                items(bookmarkedStories, key = { it.id }) { story ->
                    Box(modifier = Modifier.padding(vertical = 6.dp)) {
                        StoryCard(
                            story = story,
                            isBookmarked = true,
                            onClick = { onStoryClick(story.id) },
                            onBookmarkToggle = {
                                viewModel.toggleBookmark(story.id, true)
                            }
                        )
                    }
                }
            }
        }
    }
}
