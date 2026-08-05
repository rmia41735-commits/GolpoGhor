package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.GolpoGhorBottomBar
import com.example.ui.components.GolpoGhorTopBar
import com.example.ui.navigation.NavRoute
import com.example.ui.screens.AddEditStoryScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CommentsManagementScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoryDetailScreen
import com.example.ui.theme.GolpoGhorTheme
import com.example.ui.viewmodel.StoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolpoGhorTheme {
                GolpoGhorApp()
            }
        }
    }
}

@Composable
fun GolpoGhorApp() {
    val navController = rememberNavController()
    val viewModel: StoryViewModel = viewModel()
    val isAdminLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show top bar on main tabs
    val showTopBar = currentRoute in listOf(
        NavRoute.Home.route,
        NavRoute.Bookmarks.route,
        NavRoute.Categories.route
    )

    // Show bottom bar on main tabs & dashboard
    val showBottomBar = currentRoute in listOf(
        NavRoute.Home.route,
        NavRoute.Bookmarks.route,
        NavRoute.Categories.route,
        NavRoute.AdminDashboard.route,
        NavRoute.AdminLogin.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                GolpoGhorTopBar(
                    isAdminLoggedIn = isAdminLoggedIn,
                    onAdminClick = {
                        if (isAdminLoggedIn) {
                            navController.navigate(NavRoute.AdminDashboard.route)
                        } else {
                            navController.navigate(NavRoute.AdminLogin.route)
                        }
                    },
                    onBookmarksClick = {
                        navController.navigate(NavRoute.Bookmarks.route)
                    },
                    onSettingsClick = {
                        navController.navigate(NavRoute.Settings.route)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                GolpoGhorBottomBar(
                    currentRoute = currentRoute,
                    isAdminLoggedIn = isAdminLoggedIn,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(NavRoute.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Screen
            composable(NavRoute.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onStoryClick = { id ->
                        navController.navigate(NavRoute.StoryDetail.createRoute(id))
                    },
                    onCategoryClick = { category ->
                        viewModel.selectedCategory.value = category
                    }
                )
            }

            // Story Detail Screen
            composable(
                route = NavRoute.StoryDetail.route,
                arguments = listOf(navArgument("storyId") { type = NavType.LongType })
            ) { backStackEntry ->
                val storyId = backStackEntry.arguments?.getLong("storyId") ?: 0L
                StoryDetailScreen(
                    storyId = storyId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onReadChapterClick = { chapterIdx ->
                        navController.navigate(NavRoute.Reader.createRoute(storyId, chapterIdx))
                    }
                )
            }

            // Story Reader Screen
            composable(
                route = NavRoute.Reader.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.LongType },
                    navArgument("chapterIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val storyId = backStackEntry.arguments?.getLong("storyId") ?: 0L
                val chapterIndex = backStackEntry.arguments?.getInt("chapterIndex") ?: 0
                ReaderScreen(
                    storyId = storyId,
                    initialChapterIndex = chapterIndex,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Library Screen
            composable(NavRoute.Bookmarks.route) {
                LibraryScreen(
                    viewModel = viewModel,
                    onStoryClick = { id ->
                        navController.navigate(NavRoute.StoryDetail.createRoute(id))
                    }
                )
            }

            // Categories Screen
            composable(NavRoute.Categories.route) {
                CategoriesScreen(
                    viewModel = viewModel,
                    onCategorySelected = { categoryName ->
                        viewModel.selectedCategory.value = categoryName
                        navController.navigate(NavRoute.Home.route)
                    }
                )
            }

            // Admin Login Screen
            composable(NavRoute.AdminLogin.route) {
                AdminLoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(NavRoute.AdminDashboard.route) {
                            popUpTo(NavRoute.AdminLogin.route) { inclusive = true }
                        }
                    }
                )
            }

            // Admin Dashboard Screen
            composable(NavRoute.AdminDashboard.route) {
                if (!isAdminLoggedIn) {
                    AdminLoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            navController.navigate(NavRoute.AdminDashboard.route)
                        }
                    )
                } else {
                    AdminDashboardScreen(
                        viewModel = viewModel,
                        onAddNewStoryClick = {
                            navController.navigate(NavRoute.AddEditStory.createRoute(null))
                        },
                        onEditStoryClick = { storyId ->
                            navController.navigate(NavRoute.AddEditStory.createRoute(storyId))
                        },
                        onManageCommentsClick = {
                            navController.navigate(NavRoute.CommentsManagement.route)
                        },
                        onManageCategoriesClick = {
                            navController.navigate(NavRoute.Categories.route)
                        }
                    )
                }
            }

            // Add/Edit Story Screen
            composable(
                route = NavRoute.AddEditStory.route,
                arguments = listOf(navArgument("storyId") {
                    type = NavType.StringType
                    nullable = true
                })
            ) { backStackEntry ->
                val storyIdStr = backStackEntry.arguments?.getString("storyId")
                val storyId = storyIdStr?.toLongOrNull()
                AddEditStoryScreen(
                    storyIdToEdit = storyId,
                    viewModel = viewModel,
                    onBackToDashboard = { navController.popBackStack() }
                )
            }

            // Comments Management Screen
            composable(NavRoute.CommentsManagement.route) {
                CommentsManagementScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Settings Screen
            composable(NavRoute.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
