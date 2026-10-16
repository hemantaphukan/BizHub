package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BizTopAppBar
import com.example.ui.components.DocumentViewerDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary

@Composable
fun MainScreen(viewModel: BizViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val products by viewModel.products.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val serviceRequests by viewModel.serviceRequests.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val documentViewerState by viewModel.documentViewerState.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var isCartOpen by remember { mutableStateOf(false) }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            BizTopAppBar(
                currentRole = currentRole,
                cartItemCount = cartItems.values.sum(),
                onToggleRole = {
                    viewModel.switchRole(
                        if (currentRole == UserRole.CUSTOMER) UserRole.ADMIN else UserRole.CUSTOMER
                    )
                },
                onOpenCart = { isCartOpen = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BizPrimary
            ) {
                if (currentRole == UserRole.CUSTOMER) {
                    NavigationBarItem(
                        selected = selectedTab == MainTab.CATALOG,
                        onClick = { viewModel.selectTab(MainTab.CATALOG) },
                        icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Catalog") },
                        label = { Text(stringResource(R.string.nav_shop), fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BizPrimary,
                            indicatorColor = BizPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_catalog")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.SERVICES,
                        onClick = { viewModel.selectTab(MainTab.SERVICES) },
                        icon = { Icon(Icons.Default.Build, contentDescription = "Services") },
                        label = { Text(stringResource(R.string.nav_services), fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BizPrimary,
                            indicatorColor = BizPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_services")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.TRACKING,
                        onClick = { viewModel.selectTab(MainTab.TRACKING) },
                        icon = { Icon(Icons.Default.Timeline, contentDescription = "Tracking") },
                        label = { Text(stringResource(R.string.nav_orders), fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BizPrimary,
                            indicatorColor = BizPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_tracking")
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainTab.PROFILE,
                        onClick = { viewModel.selectTab(MainTab.PROFILE) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text(stringResource(R.string.nav_profile), fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BizPrimary,
                            indicatorColor = BizPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                } else {
                    // Admin bottom bar options
                    NavigationBarItem(
                        selected = selectedTab == MainTab.ADMIN_DASHBOARD,
                        onClick = { viewModel.selectTab(MainTab.ADMIN_DASHBOARD) },
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Dashboard") },
                        label = { Text("Admin Console", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFDC2626),
                            indicatorColor = Color(0xFFDC2626).copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_admin_console")
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.switchRole(UserRole.CUSTOMER) },
                        icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Customer Mode") },
                        label = { Text("Back to Store", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_back_to_store")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = if (currentRole == UserRole.ADMIN) MainTab.ADMIN_DASHBOARD else selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    MainTab.CATALOG -> CatalogScreen(
                        products = products,
                        cartItems = cartItems,
                        userProfile = userProfile,
                        viewModel = viewModel,
                        isCartOpen = isCartOpen,
                        onCloseCart = { isCartOpen = false }
                    )
                    MainTab.SERVICES -> ServicesScreen(
                        viewModel = viewModel
                    )
                    MainTab.TRACKING -> OrderTrackingScreen(
                        orders = orders,
                        serviceRequests = serviceRequests,
                        viewModel = viewModel
                    )
                    MainTab.PROFILE -> UserProfileScreen(
                        userProfile = userProfile,
                        orders = orders,
                        serviceRequests = serviceRequests,
                        viewModel = viewModel
                    )
                    MainTab.ADMIN_DASHBOARD -> AdminDashboardScreen(
                        products = products,
                        orders = orders,
                        serviceRequests = serviceRequests,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    // Full screen Document Viewer dialog for attached PDFs & JPGs
    DocumentViewerDialog(
        state = documentViewerState,
        onDismiss = { viewModel.closeDocumentViewer() }
    )
}
