package com.vn.bomnuocv1.presentation.farmers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.presentation.common.AgriButton
import com.vn.bomnuocv1.presentation.farmers.components.DeleteFarmerConfirmDialog
import com.vn.bomnuocv1.presentation.farmers.components.FarmerFormDialog
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriError
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer

@Composable
fun FarmerListScreen(
    onNavigateBack: () -> Unit,
    viewModel: FarmerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AgriBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground)
        ) {
            // 1. Top Header Bar
            FarmerTopBar(
                farmerCount = uiState.farmers.size,
                onNavigateBack = onNavigateBack
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = AgriCardBorder.copy(alpha = 0.6f)
            )

            // 2. Search Box & Add Button Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        placeholder = {
                            Text(
                                text = "Tìm kiếm theo tên nông dân...",
                                fontSize = 14.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = AgriGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = viewModel::onClearSearch) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Xóa tìm kiếm",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = AgriCardBorder,
                            cursorColor = AgriGreenPrimary
                        )
                    )

                    // Add Button
                    Surface(
                        modifier = Modifier
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(onClick = viewModel::onOpenAddDialog),
                        color = AgriGreenPrimary,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Thêm",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 3. Farmer List Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading && uiState.farmers.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AgriGreenPrimary,
                                strokeWidth = 3.dp
                            )
                        }
                    }

                    uiState.farmers.isEmpty() -> {
                        EmptyFarmerList(
                            isSearching = uiState.searchQuery.isNotBlank(),
                            onAddFarmer = viewModel::onOpenAddDialog,
                            onClearSearch = viewModel::onClearSearch
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = uiState.farmers,
                                key = { it.id }
                            ) { farmer ->
                                FarmerItemCard(
                                    farmer = farmer,
                                    onEdit = { viewModel.onOpenEditDialog(farmer) },
                                    onDelete = { viewModel.onOpenDeleteDialog(farmer) }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Add / Edit Dialog
        if (uiState.showFormDialog) {
            FarmerFormDialog(
                editingFarmer = uiState.editingFarmer,
                fullName = uiState.formFullName,
                phoneNumber = uiState.formPhoneNumber,
                areaNote = uiState.formAreaNote,
                formError = uiState.formError,
                isSaving = uiState.isSaving,
                onFullNameChange = viewModel::onFormFullNameChanged,
                onPhoneNumberChange = viewModel::onFormPhoneNumberChanged,
                onAreaNoteChange = viewModel::onFormAreaNoteChanged,
                onSave = viewModel::onSaveFarmer,
                onDismiss = viewModel::onDismissFormDialog
            )
        }

        // Delete Confirmation Dialog
        uiState.farmerPendingDelete?.let { farmer ->
            DeleteFarmerConfirmDialog(
                farmer = farmer,
                isDeleting = uiState.isDeleting,
                onConfirm = viewModel::onConfirmDeleteFarmer,
                onDismiss = viewModel::onDismissDeleteDialog
            )
        }
    }
}

@Composable
private fun FarmerTopBar(
    farmerCount: Int,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Danh mục nông dân",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.3).sp
            )
        }

        // Count Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AgriMintContainer)
                .border(1.dp, AgriMintBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$farmerCount người",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )
        }
    }
}

@Composable
private fun FarmerItemCard(
    farmer: Farmer,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar Badge with First Letter
                val initialChar = farmer.fullName.trim().take(1).uppercase().ifEmpty { "N" }
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AgriMintContainer)
                        .border(1.dp, AgriMintBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initialChar,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriGreenDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    // Full Name
                    Text(
                        text = farmer.fullName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // Phone Number
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = if (farmer.phoneNumber.isNullOrBlank()) Color(0xFF94A3B8) else AgriGreenPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (farmer.phoneNumber.isNullOrBlank()) "Chưa có SĐT" else farmer.phoneNumber,
                            fontSize = 13.sp,
                            color = if (farmer.phoneNumber.isNullOrBlank()) Color(0xFF94A3B8) else Color(0xFF475569)
                        )
                    }

                    // Field / Area note
                    if (!farmer.areaNote.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Landscape,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = farmer.areaNote,
                                fontSize = 12.sp,
                                color = Color(0xFF0284C7),
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Buttons (Edit and Delete)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Sửa nông dân",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Xóa nông dân",
                        tint = AgriError.copy(alpha = 0.85f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyFarmerList(
    isSearching: Boolean,
    onAddFarmer: () -> Unit,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(AgriMintContainer)
                .border(1.dp, AgriMintBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSearching) Icons.Default.Search else Icons.Default.Person,
                contentDescription = null,
                tint = AgriGreenPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) "Không tìm thấy nông dân nào" else "Chưa có nông dân trong danh bạ",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isSearching)
                "Hãy thử tìm kiếm với tên hoặc từ khóa khác."
            else
                "Thêm danh sách khách hàng để tiện ghi sổ bơm nước và theo dõi công nợ dễ dàng.",
            fontSize = 13.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isSearching) {
            AgriButton(
                text = "Xóa từ khóa tìm kiếm",
                onClick = onClearSearch,
                modifier = Modifier.height(44.dp)
            )
        } else {
            AgriButton(
                text = "+ Thêm nông dân đầu tiên",
                onClick = onAddFarmer,
                modifier = Modifier.height(46.dp)
            )
        }
    }
}
