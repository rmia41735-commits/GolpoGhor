package com.example.ui.navigation

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object StoryDetail : NavRoute("story_detail/{storyId}") {
        fun createRoute(storyId: Long) = "story_detail/$storyId"
    }
    object Reader : NavRoute("reader/{storyId}/{chapterIndex}") {
        fun createRoute(storyId: Long, chapterIndex: Int = 0) = "reader/$storyId/$chapterIndex"
    }
    object Bookmarks : NavRoute("bookmarks")
    object Categories : NavRoute("categories")
    
    // Admin routes
    object AdminLogin : NavRoute("admin_login")
    object AdminDashboard : NavRoute("admin_dashboard")
    object AddEditStory : NavRoute("add_edit_story?storyId={storyId}") {
        fun createRoute(storyId: Long? = null) = if (storyId != null) "add_edit_story?storyId=$storyId" else "add_edit_story"
    }
    object CommentsManagement : NavRoute("comments_management")
    object CategoriesManagement : NavRoute("categories_management")
    object Settings : NavRoute("settings")
}
