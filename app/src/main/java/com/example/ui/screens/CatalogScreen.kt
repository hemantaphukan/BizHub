package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.data.model.Product
import com.example.data.model.UserProfile
import com.example.ui.BizViewModel
import com.example.ui.theme.BizPrimary
import com.example.ui.theme.BizSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    products: List<Product>,
    cartItems: Map<String, Int>,
    userProfile: UserProfile,
    viewModel: BizViewModel,
    isCartOpen: Boolean,
    onCloseCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember(products) {
        listOf("All") + products.map { it.category }.distinct()
    }

    val filteredProducts = products.filter { prod ->
        (selectedCategory == "All" || prod.category == selectedCategory) &&
                (searchQuery.isBlank() || prod.name.contains(searchQuery, ignoreCase = true) || prod.sku.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_search_catalog"),
                placeholder = { Text("Search products, SKU, components...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BizPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("chip_category_$category")
                    )
                }
            }
        }

        // Product Catalog List
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Empty",
                        modifier = Modifier.size(56.dp),
                        tint = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Try adjusting your search query or category filter",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        cartCount = cartItems[product.id] ?: 0,
                        onAddToCart = { viewModel.addToCart(product.id) }
                    )
                }
            }
        }
    }

    // Cart / Checkout Bottom Sheet
    if (isCartOpen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = onCloseCart,
            sheetState = sheetState
        ) {
            CartCheckoutSheet(
                products = products,
                cartItems = cartItems,
                userProfile = userProfile,
                onUpdateQuantity = { id, qty -> viewModel.updateCartItemQuantity(id, qty) },
                onPlaceOrder = { notes, address ->
                    viewModel.placeOrder(notes, address)
                    onCloseCart()
                },
                onClose = onCloseCart
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    cartCount: Int,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = product.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BizPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SKU: ${product.sku}",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${product.rating}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = String.format(Locale.US, "$%.2f", product.price),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = BizPrimary
                    )
                    // Stock indicator
                    when {
                        product.isOutOfStock -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Out of Stock",
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Out of Stock", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        product.isLowStock -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Low Stock",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Only ${product.stockQuantity} left", fontSize = 11.sp, color = Color(0xFFD97706), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        else -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "In Stock",
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${product.stockQuantity} ${product.unit} in stock", fontSize = 11.sp, color = Color(0xFF059669))
                            }
                        }
                    }
                }

                Button(
                    onClick = onAddToCart,
                    enabled = !product.isOutOfStock,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                    modifier = Modifier.testTag("btn_add_to_cart_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (cartCount > 0) "Add More ($cartCount)" else "Add to Order",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun CartCheckoutSheet(
    products: List<Product>,
    cartItems: Map<String, Int>,
    userProfile: UserProfile,
    onUpdateQuantity: (String, Int) -> Unit,
    onPlaceOrder: (notes: String, shippingAddress: String) -> Unit,
    onClose: () -> Unit
) {
    val productsMap = remember(products) { products.associateBy { it.id } }
    var orderNotes by remember { mutableStateOf("") }
    var shippingAddress by remember { mutableStateOf(userProfile.shippingAddress) }

    val cartList = cartItems.mapNotNull { (prodId, qty) ->
        productsMap[prodId]?.let { prod -> prod to qty }
    }

    val totalAmount = cartList.sumOf { (prod, qty) -> prod.price * qty }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .testTag("cart_checkout_sheet")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Commercial Order Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${cartList.size} item types selected",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedButton(onClick = onClose) {
                Text("Close", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (cartList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty. Add products from the catalog.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartList) { (product, qty) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = String.format(Locale.US, "$%.2f each • SKU: %s", product.price, product.sku),
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onUpdateQuantity(product.id, qty - 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (qty == 1) Icons.Default.Delete else Icons.Default.Remove,
                                        contentDescription = "Reduce",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "$qty",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                IconButton(
                                    onClick = { onUpdateQuantity(product.id, qty + 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = shippingAddress,
                onValueChange = { shippingAddress = it },
                label = { Text("Delivery / Shipping Destination") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_order_address"),
                singleLine = false,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = orderNotes,
                onValueChange = { orderNotes = it },
                label = { Text("Client Delivery Notes / Instructions (Optional)") },
                placeholder = { Text("e.g. Include calibration seal, dock delivery") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_order_notes"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estimated Total:",
                    fontSize = 14.sp,
                    color = Color(0xFF475569)
                )
                Text(
                    text = String.format(Locale.US, "$%.2f", totalAmount),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = BizPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onPlaceOrder(orderNotes, shippingAddress) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_confirm_place_order"),
                colors = ButtonDefaults.buttonColors(containerColor = BizPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Confirm & Place Commercial Order",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
