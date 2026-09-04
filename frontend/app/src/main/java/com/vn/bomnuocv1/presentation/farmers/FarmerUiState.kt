package com.vn.bomnuocv1.presentation.farmers

import com.vn.bomnuocv1.domain.model.Farmer

data class FarmerUiState(
    val farmers: List<Farmer> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Search query
    val searchQuery: String = "",

    // Add / Edit Dialog state
    val showFormDialog: Boolean = false,
    val editingFarmer: Farmer? = null,
    val formFullName: String = "",
    val formPhoneNumber: String = "",
    val formAreaNote: String = "",
    val formError: String? = null,

    // Delete confirmation dialog state
    val farmerPendingDelete: Farmer? = null
)
