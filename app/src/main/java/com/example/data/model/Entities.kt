package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val tags: String,
    val summary: String,
    val coverResId: Int? = null,
    val coverUri: String? = null,
    val views: Long = 0,
    val likes: Long = 0,
    val datePublished: String,
    val isDraft: Boolean = false,
    val authorName: String = "Admin"
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storyId: Long,
    val chapterNumber: Int,
    val chapterTitle: String,
    val contentHtml: String
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storyId: Long,
    val userName: String,
    val userAvatar: String = "",
    val commentText: String,
    val date: String,
    val isApproved: Boolean = true
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val storyId: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconEmoji: String = "📚",
    val description: String = ""
)

data class StoryWithDetails(
    val story: StoryEntity,
    val chaptersCount: Int,
    val commentsCount: Int,
    val isBookmarked: Boolean = false
)
