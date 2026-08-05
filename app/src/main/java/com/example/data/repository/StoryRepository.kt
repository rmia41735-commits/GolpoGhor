package com.example.data.repository

import com.example.data.local.StoryDao
import com.example.data.model.BookmarkEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.ChapterEntity
import com.example.data.model.CommentEntity
import com.example.data.model.StoryEntity
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class StoryRepository(private val dao: StoryDao) {

    val allStories: Flow<List<StoryEntity>> = dao.getAllStories()
    val publishedStories: Flow<List<StoryEntity>> = dao.getPublishedStories()
    val allComments: Flow<List<CommentEntity>> = dao.getAllComments()
    val allBookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val allCategories: Flow<List<CategoryEntity>> = dao.getAllCategories()

    val publishedStoriesCount: Flow<Int> = dao.getPublishedStoriesCount()
    val totalViews: Flow<Long?> = dao.getTotalViews()
    val totalLikes: Flow<Long?> = dao.getTotalLikes()
    val totalCommentsCount: Flow<Int> = dao.getTotalCommentsCount()

    fun getStoryById(id: Long): Flow<StoryEntity?> = dao.getStoryById(id)
    fun getChaptersForStory(storyId: Long): Flow<List<ChapterEntity>> = dao.getChaptersForStory(storyId)
    fun getCommentsForStory(storyId: Long): Flow<List<CommentEntity>> = dao.getCommentsForStory(storyId)
    fun isBookmarked(storyId: Long): Flow<Boolean> = dao.isBookmarked(storyId)

    suspend fun getChapterById(chapterId: Long): ChapterEntity? = dao.getChapterById(chapterId)

    suspend fun saveStoryWithChapters(
        story: StoryEntity,
        chapters: List<ChapterEntity>
    ): Long = withContext(Dispatchers.IO) {
        val storyId = if (story.id == 0L) {
            dao.insertStory(story)
        } else {
            dao.updateStory(story)
            story.id
        }

        // Replace chapters
        dao.deleteChaptersForStory(storyId)
        chapters.forEachIndexed { index, chapter ->
            dao.insertChapter(
                chapter.copy(
                    storyId = storyId,
                    chapterNumber = index + 1
                )
            )
        }

        storyId
    }

    suspend fun deleteStory(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteStoryById(id)
        dao.deleteChaptersForStory(id)
    }

    suspend fun incrementViews(id: Long) = dao.incrementViews(id)
    suspend fun incrementLikes(id: Long) = dao.incrementLikes(id)

    suspend fun toggleBookmark(storyId: Long, currentBookmarked: Boolean) = withContext(Dispatchers.IO) {
        if (currentBookmarked) {
            dao.removeBookmark(storyId)
        } else {
            dao.addBookmark(BookmarkEntity(storyId = storyId))
        }
    }

    suspend fun addComment(storyId: Long, userName: String, commentText: String) = withContext(Dispatchers.IO) {
        dao.insertComment(
            CommentEntity(
                storyId = storyId,
                userName = userName,
                commentText = commentText,
                date = "আজ, ৫:৩০ PM"
            )
        )
    }

    suspend fun deleteComment(id: Long) = dao.deleteComment(id)

    suspend fun addCategory(name: String, emoji: String, desc: String) = withContext(Dispatchers.IO) {
        dao.insertCategory(CategoryEntity(name = name, iconEmoji = emoji, description = desc))
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingStories = publishedStories.first()
        if (existingStories.isNotEmpty()) return@withContext

        // Categories
        val defaultCategories = listOf(
            CategoryEntity(name = "রোমান্টিক", iconEmoji = "💖", description = "হৃদয়ছোঁয়া ভালোবাসার গল্প"),
            CategoryEntity(name = "রহস্য", iconEmoji = "🕵️", description = "রোমাঞ্চকর ও রহস্যঘেরা কাহিনী"),
            CategoryEntity(name = "হরর", iconEmoji = "👻", description = "গা ছমছমে ভৌতিক অভিজ্ঞতা"),
            CategoryEntity(name = "ইসলামিক", iconEmoji = "🌙", description = "ঈমান ও নৈতিকতার শিক্ষণীয় গল্প"),
            CategoryEntity(name = "থ্রিলার", iconEmoji = "⚡", description = "উত্তেজনায় ভরপুর রহস্য থ্রিলার"),
            CategoryEntity(name = "সায়েন্স ফিকশন", iconEmoji = "🚀", description = "ভবিষ্যৎ ও কল্পবিজ্ঞানের গল্প"),
            CategoryEntity(name = "পারিবারিক", iconEmoji = "🏡", description = "পারিবারিক সম্পর্ক ও অনুভূতির গল্প")
        )
        defaultCategories.forEach { dao.insertCategory(it) }

        // Initial Stories matching mockups
        val story1Id = dao.insertStory(
            StoryEntity(
                title = "বৃষ্টির দিনে ভালোবাসা",
                category = "রোমান্টিক",
                tags = "প্রেম, বৃষ্টি, অনুভূতি",
                summary = "এক মুষলধারার বৃষ্টির দুপুরে কফি শপে দু'টি অজানা হৃদয়ের অনাকাঙ্ক্ষিত সাক্ষাৎ আর মিষ্টি সম্পর্কের সূচনা।",
                coverResId = R.drawable.cover_rain_love_1785892481081,
                views = 2540,
                likes = 420,
                datePublished = "12 Aug 2026",
                isDraft = false
            )
        )
        dao.insertChapter(
            ChapterEntity(
                storyId = story1Id,
                chapterNumber = 1,
                chapterTitle = "অধ্যায় ১: কফির কাপ ও বৃষ্টি",
                contentHtml = """
                    <p>শ্রাবণের এক অলস দুপুরে ঝুম বৃষ্টি নামল ধানমন্ডির লেকের পাড়ে। আবীর ছাতা খুলতেও ভুলে গিয়ে ছুটে আশ্রয় নিল এক ছোট্ট কফি শপের বারান্দায়। পানির ছাঁটে ভেজা জামাটা ঝাড়তে ঝাড়তে চোখ গেল কর্নারের সেই টেবিলটার দিকে।</p>
                    <p>সেখানে বসেছিল নীল শাড়ি পরা এক মেয়ে, হাতে গরম কফির কাপ আর চোখে দূর অসীম আকাশ পানে শূন্য দৃষ্টি। জানালার কাঁচে বৃষ্টির ফোঁটা গড়িয়ে পড়ছিল, ঠিক যেমন আবীরের বুকে জমে থাকা স্মৃতিগুলো নাড়িয়ে দিচ্ছিল।</p>
                    <p><b>"কফি খাবেন এক কাপ?"</b> হালকা মিষ্টি গলায় প্রশ্নটা ভেসে এল। আবীর চকিতে তাকাল—মেয়েটি হাসিমুখে তাকিয়ে আছে।</p>
                    <p>সেই বৃষ্টির দিনে শুরু হয়েছিল এক অন্যরকম উপাখ্যান...</p>
                """.trimIndent()
            )
        )
        dao.insertChapter(
            ChapterEntity(
                storyId = story1Id,
                chapterNumber = 2,
                chapterTitle = "অধ্যায় ২: না বলা কথা",
                contentHtml = """
                    <p>দিনগুলো গড়িয়ে যেতে লাগল। নীল শাড়ির মেয়েটির নাম নীলা। কফি শপে তাদের নিয়মিত দেখা হওয়াটা যেন এক নীরব নিয়মে পরিণত হলো।</p>
                    <p>দুজনেই জানত এই বৃষ্টির দিনগুলো চিরকাল থাকবে না, কিন্তু অনুভূতির গভীরতা ছিল অতল। ভালোবাসার গল্পগুলো এভাবেই চুপিচুপি ডালপালা মেলে।</p>
                """.trimIndent()
            )
        )
        dao.insertComment(
            CommentEntity(
                storyId = story1Id,
                userName = "তানজিনা রহমান",
                commentText = "গল্পটা অসম্ভব সুন্দর! পরবর্তী অধ্যায়ের জন্য অপেক্ষায় রইলাম।",
                date = "১২ আগস্ট, ২০২৬"
            )
        )

        val story2Id = dao.insertStory(
            StoryEntity(
                title = "অন্ধকার রাত",
                category = "রহস্য",
                tags = "রহস্য, ভৌতিক, রাত",
                summary = "পুরোনো জমিদার বাড়ির নির্জন কক্ষে প্রতি অমাবস্যার রাতে বেজে ওঠে পিয়ানো। কী রহস্য লুকিয়ে আছে এই ভিটেয়?",
                coverResId = R.drawable.cover_dark_night_1785892494098,
                views = 3150,
                likes = 680,
                datePublished = "11 Aug 2026",
                isDraft = false
            )
        )
        dao.insertChapter(
            ChapterEntity(
                storyId = story2Id,
                chapterNumber = 1,
                chapterTitle = "অধ্যায় ১: জমিদার বাড়ির ছায়া",
                contentHtml = """
                    <p>রাত তখন প্রায় বারোটা। গ্রামের মেঠোপথ ধরে হেঁটে চলছিল রাজীব। চারপাশ নিঝুম, শুধু ঝিঁঝিঁ পোকার ডাক আর মাঝে মাঝে দূর থেকে শেয়ালের হাঁক শুনতে পাওয়া যায়।</p>
                    <p>সামনেই দাঁড়িয়ে আছে শত বছরের পুরোনো রাজবাড়ির কঙ্কালসার অট্টালিকা। হঠাৎ দোতলার বন্ধ জানালা থেকে ভেসে এল মৃদু আলোর দ্যুতি আর পুরোনো সুরের পিয়ানো ধ্বনি...</p>
                    <p>রাজীবের পায়ের রক্ত যেন বরফ হয়ে জমে গেল। পিয়ানো বাজানোর লোক তো এই বাড়িতে বিশ বছর আগে মারা গেছে!</p>
                """.trimIndent()
            )
        )
        dao.insertComment(
            CommentEntity(
                storyId = story2Id,
                userName = "নাহিদ হাসান",
                commentText = "একদম গায়ে কাঁটা দেওয়ার মতো থ্রিল!",
                date = "১১ আগস্ট, ২০২৬"
            )
        )

        val story3Id = dao.insertStory(
            StoryEntity(
                title = "হারানো চিঠি",
                category = "পারিবারিক",
                tags = "পরিবার, অতীত, চিঠি",
                summary = "চিঠির বাক্সে জমে থাকা এক শতাব্দী পুরোনো ধুলোঝাড়া চিঠি উন্মোচন করে দিল পরিবারের হারানো ঐতিহ্য আর গোপন ভালোবাসা।",
                coverResId = R.drawable.cover_lost_letter_1785892505799,
                views = 4870,
                likes = 1200,
                datePublished = "10 Aug 2026",
                isDraft = false
            )
        )
        dao.insertChapter(
            ChapterEntity(
                storyId = story3Id,
                chapterNumber = 1,
                chapterTitle = "অধ্যায় ১: কাঠের সিন্দুক",
                contentHtml = """
                    <p>নানীবাড়ির চিলেকোঠার ঘর পরিষ্কার করার সময় সামিয়া খুঁজে পেল পিতলের তালা লাগানো এক পুরোনো কাঠের সিন্দুক। তালা ভেঙে ভেতরে পাওয়া গেল একগুচ্ছ হলুদ হয়ে যাওয়া চিঠি।</p>
                    <p>প্রথম চিঠিটা খুলতেই সামিয়ার চোখ দিয়ে পানি গড়িয়ে পড়ল। চিঠিটা লিখেছিলেন তার প্রয়াত দাদা, ১৯৭১ সালের যুদ্ধের দিনগুলোতে...</p>
                """.trimIndent()
            )
        )

        // Seed another story for extra richness
        val story4Id = dao.insertStory(
            StoryEntity(
                title = "মহাকাশের শেষ বার্তা",
                category = "সায়েন্স ফিকশন",
                tags = "ভবিষ্যৎ, স্পেস, বিজ্ঞান",
                summary = "২০৮০ সালের মঙ্গল গ্রহের এক গবেষণাগার থেকে পৃথিবীতে আসে এক রহস্যময় সাংকেতিক সিগন্যাল।",
                coverResId = R.drawable.cover_dark_night_1785892494098,
                views = 1890,
                likes = 310,
                datePublished = "09 Aug 2026",
                isDraft = false
            )
        )
        dao.insertChapter(
            ChapterEntity(
                storyId = story4Id,
                chapterNumber = 1,
                chapterTitle = "অধ্যায় ১: সংকেত",
                contentHtml = """
                    <p>মঙ্গল গ্রহের অলিম্পাস মানস স্টেশনে লাল বাতি জ্বলতে শুরু করল। এয়ারলকের প্রধান কম্পিউটার থেকে বিকট আওয়াজে বিপ সিগন্যাল আসছে।</p>
                    <p>ক্যাপ্টেন আরিয়ান রাডার স্ক্রিনে তাকিয়ে চমকে উঠলেন। মিল্কিওয়ে গ্যালাক্সির একদম শেষ সীমা থেকে এক ডিজিটাল ডিজিটাল ফ্রিকোয়েন্সি ভেসে আসছে...</p>
                """.trimIndent()
            )
        )
    }
}
