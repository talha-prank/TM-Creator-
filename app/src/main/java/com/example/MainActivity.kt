package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.models.ProfileEntity
import com.example.ui.components.openWhatsApp
import com.example.ui.screens.*
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.TalhaPortfolioTheme
import com.example.viewmodel.PortfolioViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: PortfolioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val systemDark = isSystemInDarkTheme()
            var darkThemeEnabled by remember { mutableStateOf(true) } // Default to agency dark theme

            TalhaPortfolioTheme(darkTheme = darkThemeEnabled) {
                MainPortfolioApp(
                    viewModel = viewModel,
                    darkTheme = darkThemeEnabled,
                    onToggleTheme = { darkThemeEnabled = !darkThemeEnabled }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPortfolioApp(
    viewModel: PortfolioViewModel,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val selectedProjectForDetail by viewModel.selectedProjectForDetail.collectAsState()
    val context = LocalContext.current
    val currentProfile = profile ?: ProfileEntity()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissToast()
        }
    }

    // Custom back handling
    BackHandler(enabled = currentScreen != Screen.HOME || selectedProjectForDetail != null) {
        if (selectedProjectForDetail != null) {
            viewModel.closeProjectDetail()
        } else if (currentScreen != Screen.HOME) {
            viewModel.navigateTo(Screen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                // Owner secret shortcut: tapping your profile header opens the secure Admin Dashboard
                                viewModel.navigateTo(Screen.ADMIN)
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyanPrimary.copy(alpha = 0.2f))
                                .border(1.dp, CyanPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(currentProfile.profileImageUrl.ifBlank { R.drawable.talha_avatar })
                                    .crossfade(true)
                                    .error(R.drawable.talha_avatar)
                                    .placeholder(R.drawable.talha_avatar)
                                    .build(),
                                contentDescription = "Talha Mahmood",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Talha Mahmood",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "Digital Creator & Tech Pro",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Dark / Light Theme Toggle
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Quick "Hire Me" CTA in TopBar
                    FilledTonalButton(
                        onClick = { viewModel.navigateTo(Screen.CONTACT) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            "Hire Me",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == Screen.HOME,
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedIconColor = CyanPrimary
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.SERVICES,
                    onClick = { viewModel.navigateTo(Screen.SERVICES) },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Services") },
                    label = { Text("Services") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedIconColor = CyanPrimary
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.PROJECTS,
                    onClick = { viewModel.navigateTo(Screen.PROJECTS) },
                    icon = { Icon(Icons.Default.GridView, contentDescription = "Projects") },
                    label = { Text("Projects") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedIconColor = CyanPrimary
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == Screen.CONTACT,
                    onClick = { viewModel.navigateTo(Screen.CONTACT) },
                    icon = { Icon(Icons.Default.Send, contentDescription = "Contact") },
                    label = { Text("Contact") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedIconColor = CyanPrimary
                    )
                )
            }
        },
        floatingActionButton = {
            // Floating WhatsApp Quick Action on client screens
            if (currentScreen != Screen.ADMIN) {
                FloatingActionButton(
                    onClick = {
                        openWhatsApp(context, currentProfile.whatsapp)
                    },
                    containerColor = EmeraldSuccess,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Chat on WhatsApp",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                Screen.HOME -> HomeScreen(viewModel = viewModel)
                Screen.SERVICES -> ServicesScreen(viewModel = viewModel)
                Screen.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                Screen.CONTACT -> ContactScreen(viewModel = viewModel)
                Screen.ADMIN -> AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }
}
