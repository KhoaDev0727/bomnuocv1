package com.vn.bomnuocv1.presentation.farmers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.usecase.CreateFarmerUseCase
import com.vn.bomnuocv1.domain.usecase.DeleteFarmerUseCase
import com.vn.bomnuocv1.domain.usecase.GetFarmersUseCase
import com.vn.bomnuocv1.domain.usecase.UpdateFarmerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerViewModel @Inject constructor(
    private val getFarmersUseCase: GetFarmersUseCase,
    private val createFarmerUseCase: CreateFarmerUseCase,
    private val updateFarmerUseCase: UpdateFarmerUseCase,
    private val deleteFarmerUseCase: DeleteFarmerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmerUiState(isLoading = true))
    val uiState: StateFlow<FarmerUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadFarmers()
    }

    fun loadFarmers(keyword: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getFarmersUseCase(keyword)
            result.onSuccess { list ->
                _uiState.update {
                    it.copy(
                        farmers = list,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Không thể tải danh sách nông dân."
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce search
            loadFarmers(query.trim())
        }
    }

    fun onClearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        searchJob?.cancel()
        loadFarmers(null)
    }

    fun onOpenAddDialog() {
        _uiState.update {
            it.copy(
                showFormDialog = true,
                editingFarmer = null,
                formFullName = "",
                formPhoneNumber = "",
                formAreaNote = "",
                formError = null
            )
        }
    }

    fun onOpenEditDialog(farmer: Farmer) {
        _uiState.update {
            it.copy(
                showFormDialog = true,
                editingFarmer = farmer,
                formFullName = farmer.fullName,
                formPhoneNumber = farmer.phoneNumber.orEmpty(),
                formAreaNote = farmer.areaNote.orEmpty(),
                formError = null
            )
        }
    }

    fun onDismissFormDialog() {
        _uiState.update {
            it.copy(
                showFormDialog = false,
                editingFarmer = null,
                formError = null
            )
        }
    }

    fun onFormFullNameChanged(name: String) {
        _uiState.update { it.copy(formFullName = name, formError = null) }
    }

    fun onFormPhoneNumberChanged(phone: String) {
        _uiState.update { it.copy(formPhoneNumber = phone) }
    }

    fun onFormAreaNoteChanged(area: String) {
        _uiState.update { it.copy(formAreaNote = area) }
    }

    fun onSaveFarmer() {
        val currentState = _uiState.value
        val fullName = currentState.formFullName.trim()
        if (fullName.isEmpty()) {
            _uiState.update { it.copy(formError = "Họ và tên nông dân không được để trống") }
            return
        }

        val rawPhone = currentState.formPhoneNumber.trim()
        val phoneNumber = if (rawPhone.isNotEmpty()) {
            val cleaned = rawPhone.replace(Regex("[\\s\\-\\(\\)]"), "")
            if (!cleaned.matches(Regex("^(0|\\+84)[0-9]{9,10}$"))) {
                _uiState.update { it.copy(formError = "Số điện thoại không hợp lệ (phải gồm 10 chữ số).") }
                return
            }
            cleaned
        } else null

        val areaNote = currentState.formAreaNote.trim().ifEmpty { null }
        val editing = currentState.editingFarmer

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }

            val result = if (editing == null) {
                createFarmerUseCase(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    areaNote = areaNote
                )
            } else {
                updateFarmerUseCase(
                    id = editing.id,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    areaNote = areaNote
                )
            }

            result.onSuccess { savedFarmer ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showFormDialog = false,
                        editingFarmer = null,
                        successMessage = if (editing == null) "Thêm nông dân mới thành công!" else "Cập nhật hồ sơ thành công!"
                    )
                }
                loadFarmers(_uiState.value.searchQuery.trim())
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        formError = error.message ?: "Không thể lưu thông tin nông dân."
                    )
                }
            }
        }
    }

    fun onOpenDeleteDialog(farmer: Farmer) {
        _uiState.update { it.copy(farmerPendingDelete = farmer) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(farmerPendingDelete = null) }
    }

    fun onConfirmDeleteFarmer() {
        val farmer = _uiState.value.farmerPendingDelete ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            val result = deleteFarmerUseCase(farmer.id)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        farmerPendingDelete = null,
                        successMessage = "Đã xóa hồ sơ nông dân ${farmer.fullName}!"
                    )
                }
                loadFarmers(_uiState.value.searchQuery.trim())
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        farmerPendingDelete = null,
                        errorMessage = error.message ?: "Không thể xóa hồ sơ nông dân."
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
