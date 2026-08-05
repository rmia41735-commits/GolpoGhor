package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.ChapterEntity
import com.example.data.model.CommentEntity
import com.example.data.model.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    // Stories
    @Query("SELECT * FROM stories ORDER BY id DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isDraft = 0 ORDER BY id DESC")
    fun getPublishedStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun getStoryById(id: Long): Flow<StoryEntity?>

    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStoryByIdDirect(id: Long): StoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Update
    suspend fun updateStory(story: StoryEntity)

    @Query("DELETE FROM stories WHERE id = :id")
    suspend fun deleteStoryById(id: Long)

    @Query("UPDATE stories SET views = views + 1 WHERE id = :id")
    suspend fun incrementViews(id: Long)

    @Query("UPDATE stories SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLikes(id: Long)

    // Chapters
    @Query("SELECT * FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC")
    fun getChaptersForStory(storyId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC")
    suspend fun getChaptersForStoryDirect(storyId: Long): List<ChapterEntity>

    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: Long): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity): Long

    @Query("DELETE FROM chapters WHERE storyId = :storyId")
    suspend fun deleteChaptersForStory(storyId: Long)

    // Comments
    @Query("SELECT * FROM comments WHERE storyId = :storyId ORDER BY id DESC")
    fun getCommentsForStory(storyId: Long): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments ORDER BY id DESC")
    fun getAllComments(): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteComment(id: Long)

    // Bookmarks
    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE storyId = :storyId)")
    fun isBookmarked(storyId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE storyId = :storyId")
    suspend fun removeBookmark(storyId: Long)

    // Categories
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    // Counts for stats
    @Query("SELECT COUNT(*) FROM stories WHERE isDraft = 0")
    fun getPublishedStoriesCount(): Flow<Int>

    @Query("SELECT SUM(views) FROM stories")
    fun getTotalViews(): Flow<Long?>

    @Query("SELECT SUM(likes) FROM stories")
    fun getTotalLikes(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM comments")
    fun getTotalCommentsCount(): Flow<Int>
}
