package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.ChapterEntity
import com.example.data.model.CommentEntity
import com.example.data.model.StoryEntity
import com.example.data.repository.StoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ReaderTheme {
    LIGHT, PAPER, DARK
}

class StoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StoryRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = StoryRepository(database.storyDao())

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Auth State
    val isLoggedIn = MutableStateFlow(false)
    val loginError = MutableStateFlow<String?>(null)

    fun login(email: String, pass: String): Boolean {
        if (email.trim() == "admin@golpoghor.com" && pass.trim() == "123456") {
            isLoggedIn.value = true
            loginError.value = null
            return true
        } else {
            loginError.value = "ইমেইল অথবা পাসওয়ার্ড ভুল।"
            return false
        }
    }

    fun logout() {
        isLoggedIn.value = false
    }

    // Filter & Search
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("সকল") // "সকল" means All

    val publishedStories: StateFlow<List<StoryEntity>> = repository.publishedStories
        .combine(searchQuery) { stories, query ->
            if (query.isBlank()) stories
            else stories.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.tags.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
        .combine(selectedCategory) { stories, category ->
            if (category == "সকল" || category.isBlank()) stories
            else stories.filter { it.category.equals(category, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStoriesForAdmin: StateFlow<List<StoryEntity>> = repository.allStories
        .combine(searchQuery) { stories, query ->
            if (query.isBlank()) stories
            else stories.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val comments: StateFlow<List<CommentEntity>> = repository.allComments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dashboard Stats
    val publishedStoriesCount = repository.publishedStoriesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalViews = repository.totalViews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalLikes = repository.totalLikes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalCommentsCount = repository.totalCommentsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Reader Preferences
    val readerFontSize = MutableStateFlow(18f) // sp
    val readerTheme = MutableStateFlow(ReaderTheme.PAPER)

    fun updateFontSize(delta: Float) {
        val newSize = (readerFontSize.value + delta).coerceIn(14f, 30f)
        readerFontSize.value = newSize
    }

    fun setReaderTheme(theme: ReaderTheme) {
        readerTheme.value = theme
    }

    // Story Details & Actions
    fun getStoryById(id: Long) = repository.getStoryById(id)
    fun getChaptersForStory(storyId: Long) = repository.getChaptersForStory(storyId)
    fun getCommentsForStory(storyId: Long) = repository.getCommentsForStory(storyId)
    fun isBookmarked(storyId: Long) = repository.isBookmarked(storyId)

    fun incrementViews(storyId: Long) {
        viewModelScope.launch {
            repository.incrementViews(storyId)
        }
    }

    fun incrementLikes(storyId: Long) {
        viewModelScope.launch {
            repository.incrementLikes(storyId)
        }
    }

    fun toggleBookmark(storyId: Long, currentBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(storyId, currentBookmarked)
        }
    }

    fun addComment(storyId: Long, userName: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(storyId, if (userName.isBlank()) "পাঠক" else userName, text)
        }
    }

    fun deleteComment(id: Long) {
        viewModelScope.launch {
            repository.deleteComment(id)
        }
    }

    fun deleteStory(id: Long) {
        viewModelScope.launch {
            repository.deleteStory(id)
        }
    }

    fun addCategory(name: String, emoji: String, desc: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addCategory(name, if (emoji.isBlank()) "📚" else emoji, desc)
        }
    }

    // Save or Publish Story from Add/Edit screen
    fun saveStory(
        existingId: Long = 0L,
        title: String,
        category: String,
        tags: String,
        summary: String,
        chapters: List<ChapterEntity>,
        isDraft: Boolean,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val dateStr = "12 Aug 2026"
            val story = StoryEntity(
                id = existingId,
                title = title.ifBlank { "শিরোনামহীন গল্প" },
                category = category.ifBlank { "অন্যান্য" },
                tags = tags,
                summary = summary,
                datePublished = dateStr,
                isDraft = isDraft
            )
            val id = repository.saveStoryWithChapters(story, chapters)
            onComplete(id)
        }
    }
}
