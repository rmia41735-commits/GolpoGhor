package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.ChapterEntity
import com.example.ui.components.RichTextRenderer
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStoryScreen(
    storyIdToEdit: Long?,
    viewModel: StoryViewModel,
    onBackToDashboard: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("রোমান্টিক") }
    var tags by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var currentChapterTitle by remember { mutableStateOf("অধ্যায় ১: সূচনা") }
    var currentChapterContent by remember { mutableStateOf("") }

    val chapters = remember { mutableStateListOf<ChapterEntity>() }

    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var showPreviewModal by remember { mutableStateOf(false) }

    // If editing existing story, load its contents
    if (storyIdToEdit != null && storyIdToEdit > 0L) {
        val storyToEdit by viewModel.getStoryById(storyIdToEdit).collectAsStateWithLifecycle(initialValue = null)
        val chaptersToEdit by viewModel.getChaptersForStory(storyIdToEdit).collectAsStateWithLifecycle(initialValue = emptyList())

        LaunchedEffect(storyToEdit, chaptersToEdit) {
            storyToEdit?.let { s ->
                title = s.title
                selectedCategory = s.category
                tags = s.tags
                summary = s.summary
            }
            if (chaptersToEdit.isNotEmpty()) {
                chapters.clear()
                chapters.addAll(chaptersToEdit)
            }
        }
    }

    // Default 1 empty chapter if none exist
    LaunchedEffect(Unit) {
        if (chapters.isEmpty()) {
            chapters.add(
                ChapterEntity(
                    storyId = 0L,
                    chapterNumber = 1,
                    chapterTitle = "অধ্যায় ১",
                    contentHtml = "<p>এখানে গল্প লিখুন...</p>"
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (storyIdToEdit != null) "📖 গল্প সম্পাদনা" else "📖 নতুন গল্প প্রকাশ করুন", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackToDashboard) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalNavy)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FB)),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "গল্পের তথ্য বিবরণী",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalNavy
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title Field
                        Text(text = "গল্পের শিরোনাম *", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("গল্পের নাম দিন (যেমন: বৃষ্টির দিনে ভালোবাসা)") },
                            modifier = Modifier.fillMaxWidth().testTag("story_title_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Category Dropdown
                        Text(text = "ক্যাটাগরি *", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCategoryDropdownExpanded = true }
                                    .testTag("story_category_dropdown"),
                                trailingIcon = {
                                    IconButton(onClick = { isCategoryDropdownExpanded = true }) {
                                        Text("▼")
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = isCategoryDropdownExpanded,
                                onDismissRequest = { isCategoryDropdownExpanded = false }
                            ) {
                                val defaultCategoryNames = listOf("রোমান্টিক", "রহস্য", "হরর", "ইসলামিক", "থ্রিলার", "সায়েন্স ফিকশন", "পারিবারিক")
                                val catNames = if (categories.isNotEmpty()) categories.map { it.name } else defaultCategoryNames

                                catNames.forEach { catName ->
                                    DropdownMenuItem(
                                        text = { Text(catName) },
                                        onClick = {
                                            selectedCategory = catName
                                            isCategoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tags
                        Text(text = "ট্যাগ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = tags,
                            onValueChange = { tags = it },
                            placeholder = { Text("যেমন: প্রেম, বৃষ্টি, রহস্য") },
                            modifier = Modifier.fillMaxWidth().testTag("story_tags_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Short Summary
                        Text(text = "সংক্ষিপ্ত বিবরণ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = summary,
                            onValueChange = { summary = it },
                            placeholder = { Text("গল্প সম্পর্কে সংক্ষিপ্ত বিবরণ লিখুন...") },
                            modifier = Modifier.fillMaxWidth().testTag("story_summary_input"),
                            minLines = 3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rich Text Story Writer Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "📖 গল্প লিখুন (Rich Editor)",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalNavy
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Toolbar formatting simulation
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    currentChapterContent += " <b>বোল্ড লেখা</b> "
                                }
                            ) {
                                Icon(imageVector = Icons.Default.FormatBold, contentDescription = "Bold")
                            }

                            IconButton(
                                onClick = {
                                    currentChapterContent += " <i>ইটালিক লেখা</i> "
                                }
                            ) {
                                Icon(imageVector = Icons.Default.FormatItalic, contentDescription = "Italic")
                            }

                            IconButton(
                                onClick = {
                                    currentChapterContent += " <p><b>নতুন প্যারাগ্রাফ...</b></p> "
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Title, contentDescription = "Header")
                            }

                            IconButton(
                                onClick = {
                                    currentChapterContent += " <blockquote>উদ্ধৃতি টেক্সট</blockquote> "
                                }
                            ) {
                                Icon(imageVector = Icons.Default.FormatQuote, contentDescription = "Quote")
                            }

                            IconButton(
                                onClick = {
                                    currentChapterContent += " <ul><li>তালিকা আইটেম ১</li><li>আইটেম ২</li></ul> "
                                }
                            ) {
                                Icon(imageVector = Icons.Default.FormatListNumbered, contentDescription = "List")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = currentChapterContent,
                            onValueChange = { currentChapterContent = it },
                            placeholder = { Text("এখানে মূল গল্প বা অধ্যায়ের বিবরণ লিখুন (HTML বা প্লেইন টেক্সট)...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .testTag("story_editor_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (currentChapterContent.isNotBlank()) {
                                    val nextNumber = chapters.size + 1
                                    chapters.add(
                                        ChapterEntity(
                                            storyId = storyIdToEdit ?: 0L,
                                            chapterNumber = nextNumber,
                                            chapterTitle = "অধ্যায় $nextNumber",
                                            contentHtml = currentChapterContent
                                        )
                                    )
                                    currentChapterContent = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                            modifier = Modifier.align(Alignment.End).testTag("add_chapter_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Chapter")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("অধ্যায় হিসেবে যোগ করুন")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chapter list preview card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📚 তৈরি করা অধ্যায়সমূহ (${chapters.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RoyalNavy
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        chapters.forEachIndexed { index, chapter ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "অধ্যায় ${index + 1}: ${chapter.chapterTitle}",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Action Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Publish Button
                    Button(
                        onClick = {
                            viewModel.saveStory(
                                existingId = storyIdToEdit ?: 0L,
                                title = title,
                                category = selectedCategory,
                                tags = tags,
                                summary = summary,
                                chapters = chapters.toList(),
                                isDraft = false,
                                onComplete = { onBackToDashboard() }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("publish_story_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🚀 Publish করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Draft
                        Button(
                            onClick = {
                                viewModel.saveStory(
                                    existingId = storyIdToEdit ?: 0L,
                                    title = title,
                                    category = selectedCategory,
                                    tags = tags,
                                    summary = summary,
                                    chapters = chapters.toList(),
                                    isDraft = true,
                                    onComplete = { onBackToDashboard() }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280)),
                            modifier = Modifier.weight(1f).testTag("save_draft_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("💾 Save Draft")
                        }

                        // Preview
                        Button(
                            onClick = { showPreviewModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            modifier = Modifier.weight(1f).testTag("preview_story_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Visibility, contentDescription = "Preview")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("👀 Preview")
                        }
                    }

                    // Dashboard back
                    Button(
                        onClick = onBackToDashboard,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        modifier = Modifier.fillMaxWidth().testTag("back_to_dashboard_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("⬅ Dashboard এ ফিরে যান", color = Color.White)
                    }
                }
            }
        }
    }

    // Live Story Preview Modal Dialog
    if (showPreviewModal) {
        AlertDialog(
            onDismissRequest = { showPreviewModal = false },
            title = {
                Text(
                    text = "👀 গল্প প্রিভিউ: $title",
                    fontWeight = FontWeight.Bold,
                    color = RoyalNavy
                )
            },
            text = {
                Column {
                    Text(text = "ক্যাটাগরি: $selectedCategory", color = BrightBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = summary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "অধ্যায় সংখ্যা: ${chapters.size}", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPreviewModal = false }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }
}
