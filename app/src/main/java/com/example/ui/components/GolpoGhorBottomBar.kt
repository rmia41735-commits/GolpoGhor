package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.ui.navigation.NavRoute
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.RoyalNavy

@Composable
fun GolpoGhorBottomBar(
    currentRoute: String?,
    isAdminLoggedIn: Boolean,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF141622),
        contentColor = Color(0xFFF3F4F6),
        windowInsets = WindowInsets.navigationBars
    ) {
        val isHome = currentRoute == NavRoute.Home.route
        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigate(NavRoute.Home.route) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("হোম") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF141622),
                selectedTextColor = Color(0xFFE2B37E),
                indicatorColor = Color(0xFFE2B37E),
                unselectedIconColor = Color(0xFF9CA3AF),
                unselectedTextColor = Color(0xFF9CA3AF)
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        val isLibrary = currentRoute == NavRoute.Bookmarks.route
        NavigationBarItem(
            selected = isLibrary,
            onClick = { onNavigate(NavRoute.Bookmarks.route) },
            icon = {
                Icon(
                    imageVector = if (isLibrary) Icons.Filled.Book else Icons.Outlined.Book,
                    contentDescription = "Library"
                )
            },
            label = { Text("লাইব্রেরি") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF141622),
                selectedTextColor = Color(0xFFE2B37E),
                indicatorColor = Color(0xFFE2B37E),
                unselectedIconColor = Color(0xFF9CA3AF),
                unselectedTextColor = Color(0xFF9CA3AF)
            ),
            modifier = Modifier.testTag("nav_item_library")
        )

        val isCategories = currentRoute == NavRoute.Categories.route
        NavigationBarItem(
            selected = isCategories,
            onClick = { onNavigate(NavRoute.Categories.route) },
            icon = {
                Icon(
                    imageVector = if (isCategories) Icons.Filled.Category else Icons.Outlined.Category,
                    contentDescription = "Categories"
                )
            },
            label = { Text("ক্যাটাগরি") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF141622),
                selectedTextColor = Color(0xFFE2B37E),
                indicatorColor = Color(0xFFE2B37E),
                unselectedIconColor = Color(0xFF9CA3AF),
                unselectedTextColor = Color(0xFF9CA3AF)
            ),
            modifier = Modifier.testTag("nav_item_categories")
        )

        val isAdmin = currentRoute == NavRoute.AdminDashboard.route || currentRoute == NavRoute.AdminLogin.route
        NavigationBarItem(
            selected = isAdmin,
            onClick = {
                if (isAdminLoggedIn) {
                    onNavigate(NavRoute.AdminDashboard.route)
                } else {
                    onNavigate(NavRoute.AdminLogin.route)
                }
            },
            icon = {
                Icon(
                    imageVector = if (isAdmin) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
                    contentDescription = "Admin"
                )
            },
            label = { Text(if (isAdminLoggedIn) "ড্যাশবোর্ড" else "লগইন") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF141622),
                selectedTextColor = Color(0xFFE2B37E),
                indicatorColor = Color(0xFFE2B37E),
                unselectedIconColor = Color(0xFF9CA3AF),
                unselectedTextColor = Color(0xFF9CA3AF)
            ),
            modifier = Modifier.testTag("nav_item_admin")
        )
    }
}
