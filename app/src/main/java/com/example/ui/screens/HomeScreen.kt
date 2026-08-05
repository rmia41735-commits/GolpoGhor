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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.StoryCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@Composable
fun HomeScreen(
    viewModel: StoryViewModel,
    onStoryClick: (Long) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val stories by viewModel.publishedStories.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val totalViews by viewModel.totalViews.collectAsStateWithLifecycle()
    val publishedCount by viewModel.publishedStoriesCount.collectAsStateWithLifecycle()

    val bookmarkedIds = bookmarks.map { it.storyId }.toSet()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1117)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF141622), Color(0xFF1A1D2A))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "স্বাগতম গল্পঘরে 👋",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF3F4F6)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "বাংলা সাহিত্যের সেরা গল্পের ভাণ্ডার",
                                fontSize = 14.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Logo",
                            tint = Color(0xFFE2B37E),
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_input"),
                        placeholder = { Text("গল্পের নাম বা লেখক দিয়ে খুঁজুন...", color = Color(0xFF6B7280)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFE2B37E))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF9CA3AF))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF181B24),
                            unfocusedContainerColor = Color(0xFF181B24),
                            focusedTextColor = Color(0xFFF3F4F6),
                            unfocusedTextColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFFE2B37E),
                            unfocusedBorderColor = Color(0xFF282C3A)
                        )
                    )
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "মোট গল্প", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        Text(text = "$publishedCount টি", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE2B37E))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181B24)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C3A)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "মোট ভিউ", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        Text(
                            text = "${(totalViews ?: 0) / 1000}K+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF818CF8)
                        )
                    }
                }
            }
        }

        // Category Filter Pills
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "ক্যাটাগরি সমূহ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3F4F6),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isSelected = selectedCategory == "সকল"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFFE2B37E) else Color(0xFF181B24))
                                .clickable { viewModel.selectedCategory.value = "সকল" }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("category_pill_all")
                        ) {
                            Text(
                                text = "📚 সকল",
                                color = if (isSelected) Color(0xFF141622) else Color(0xFF9CA3AF),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }

                    items(categories) { cat ->
                        val isSelected = selectedCategory.equals(cat.name, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFFE2B37E) else Color(0xFF181B24))
                                .clickable { viewModel.selectedCategory.value = cat.name }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("category_pill_${cat.name}")
                        ) {
                            Text(
                                text = "${cat.iconEmoji} ${cat.name}",
                                color = if (isSelected) Color(0xFF141622) else Color(0xFF9CA3AF),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Stories Header
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory == "সকল") "সর্বশেষ গল্পসমূহ 📖" else "$selectedCategory এর গল্প",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3F4F6)
                )

                Text(
                    text = "${stories.size} টি গল্প পাওয়া গেছে",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }

        // Stories List
        if (stories.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "কোনো গল্প পাওয়া যায়নি",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "অন্য কোনো শিরোনাম বা ক্যাটাগরি বেছে চেষ্টা করুন",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(stories, key = { it.id }) { story ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    StoryCard(
                        story = story,
                        isBookmarked = bookmarkedIds.contains(story.id),
                        onClick = { onStoryClick(story.id) },
                        onBookmarkToggle = {
                            viewModel.toggleBookmark(story.id, bookmarkedIds.contains(story.id))
                        }
                    )
                }
            }
        }
    }
}
