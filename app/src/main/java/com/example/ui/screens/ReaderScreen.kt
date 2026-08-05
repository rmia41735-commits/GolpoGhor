package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.components.RichTextRenderer
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PaperBg
import com.example.ui.theme.PaperText
import com.example.ui.theme.RoyalNavy
import com.example.ui.viewmodel.ReaderTheme
import com.example.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    storyId: Long,
    initialChapterIndex: Int,
    viewModel: StoryViewModel,
    onBackClick: () -> Unit
) {
    val story by viewModel.getStoryById(storyId).collectAsStateWithLifecycle(initialValue = null)
    val chapters by viewModel.getChaptersForStory(storyId).collectAsStateWithLifecycle(initialValue = emptyList())
    val fontSize by viewModel.readerFontSize.collectAsStateWithLifecycle()
    val readerTheme by viewModel.readerTheme.collectAsStateWithLifecycle()

    var currentChapterIndex by remember { mutableStateOf(initialChapterIndex) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var isNarrating by remember { mutableStateOf(false) }

    val currentChapter = chapters.getOrNull(currentChapterIndex)

    // Background & text color derived from ReaderTheme
    val (bgColor, textColor) = when (readerTheme) {
        ReaderTheme.PAPER -> PaperBg to PaperText
        ReaderTheme.DARK -> Color(0xFF121212) to Color(0xFFE0E0E0)
        ReaderTheme.LIGHT -> Color.White to Color(0xFF1E293B)
    }

    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = story?.title ?: "পড়ুন",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (readerTheme == ReaderTheme.DARK) Color.White else Color.Black
                        )
                        if (currentChapter != null) {
                            Text(
                                text = currentChapter.chapterTitle,
                                fontSize = 12.sp,
                                color = if (readerTheme == ReaderTheme.DARK) Color.LightGray else Color.DarkGray
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (readerTheme == ReaderTheme.DARK) Color.White else Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isNarrating = !isNarrating },
                        modifier = Modifier.testTag("narration_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isNarrating) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Audio Narration",
                            tint = if (readerTheme == ReaderTheme.DARK) Color.White else Color.Black
                        )
                    }

                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("reader_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (readerTheme == ReaderTheme.DARK) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = when (readerTheme) {
                        ReaderTheme.PAPER -> Color(0xFFF3EFE6)
                        ReaderTheme.DARK -> Color(0xFF1E1E1E)
                        ReaderTheme.LIGHT -> Color(0xFFF1F5F9)
                    }
                )
            )
        },
        bottomBar = {
            // Chapter Navigation Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        when (readerTheme) {
                            ReaderTheme.PAPER -> Color(0xFFF3EFE6)
                            ReaderTheme.DARK -> Color(0xFF1E1E1E)
                            ReaderTheme.LIGHT -> Color(0xFFF1F5F9)
                        }
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (currentChapterIndex > 0) currentChapterIndex--
                    },
                    enabled = currentChapterIndex > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("পূর্ববর্তী")
                }

                Text(
                    text = "${currentChapterIndex + 1} / ${chapters.size}",
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Button(
                    onClick = {
                        if (currentChapterIndex < chapters.size - 1) currentChapterIndex++
                    },
                    enabled = currentChapterIndex < chapters.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
                ) {
                    Text("পরবর্তী")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(bgColor)
        ) {
            val chapter = currentChapter
            if (chapter == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "অধ্যায় পাওয়া যায়নি", color = textColor)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Audio Narration Banner if active
                    if (isNarrating) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = RoyalNavy)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Playing",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🔊 অডিও বর্ণনা চলছে (শব্দ শুনুন)...",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = chapter.chapterTitle,
                        fontSize = (fontSize + 4).sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RichTextRenderer(
                        htmlContent = chapter.contentHtml,
                        fontSize = fontSize.sp,
                        textColor = textColor,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Settings Bottom Sheet
    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "⚙️ পড়ার সেটিংস",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Font size controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.FormatSize, contentDescription = "Font Size")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "ফন্ট সাইজ: ${fontSize.toInt()}sp", fontWeight = FontWeight.Bold)
                    }

                    Row {
                        IconButton(
                            onClick = { viewModel.updateFontSize(-2f) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Smaller")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { viewModel.updateFontSize(2f) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Larger")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Theme mode selection
                Text(text = "থিম পছন্দ করুন:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Paper
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PaperBg)
                            .clickable { viewModel.setReaderTheme(ReaderTheme.PAPER) }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কাগজ 📜",
                            fontWeight = FontWeight.Bold,
                            color = PaperText
                        )
                    }

                    // Dark
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF121212))
                            .clickable { viewModel.setReaderTheme(ReaderTheme.DARK) }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "অন্ধকার 🌙",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Light
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { viewModel.setReaderTheme(ReaderTheme.LIGHT) }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "সাদা ☀️",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
