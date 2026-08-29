package com.vn.bomnuocv1.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.presentation.common.AgriCardContainer
import com.vn.bomnuocv1.presentation.common.AgriInputField
import com.vn.bomnuocv1.presentation.common.AgriPrimaryButton
import com.vn.bomnuocv1.presentation.common.BrandLogoBadge
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriError
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.vn.bomnuocv1.ui.theme.AppFontFamily

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToOtp: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(uiState.isOtpSent) {
        if (uiState.isOtpSent) {
            val phone = uiState.phoneNumber.trim()
            val name = uiState.fullName.trim()
            val pin = uiState.pinCode.trim()
            val vId = uiState.verificationId.trim()
            viewModel.resetOtpSent()
            onNavigateToOtp(phone, name, pin, vId)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .imePadding()
    ) {
        val minHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Brand Logo Emblem (Mint container + leaf icon + "Bơm Nước")
            BrandLogoBadge(size = 96.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // Header Title: Tạo tài khoản Chủ trạm bơm
            Text(
                text = "Tạo tài khoản Chủ trạm bơm",
                fontFamily = AppFontFamily,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Main White Card Container matching 100% with the provided UI mockup
            AgriCardContainer {
                // Full Name Input
                AgriInputField(
                    value = uiState.fullName,
                    onValueChange = viewModel::onFullNameChanged,
                    label = "Họ và tên",
                    placeholder = "Nhập họ và tên của bạn",
                    leadingIcon = Icons.Outlined.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Phone Number Input
                AgriInputField(
                    value = uiState.phoneNumber,
                    onValueChange = viewModel::onPhoneNumberChanged,
                    label = "Số điện thoại",
                    placeholder = "Nhập số điện thoại",
                    leadingIcon = Icons.Outlined.Call,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 4-digit PIN Code Input
                AgriInputField(
                    value = uiState.pinCode,
                    onValueChange = viewModel::onPinCodeChanged,
                    label = "Mã PIN bảo mật (4 số)",
                    placeholder = "Nhập mã PIN 4 số",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm PIN Code Input
                AgriInputField(
                    value = uiState.confirmPinCode,
                    onValueChange = viewModel::onConfirmPinCodeChanged,
                    label = "Xác nhận mã PIN",
                    placeholder = "Nhập lại mã PIN",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.onRegisterClicked(activity)
                        }
                    )
                )

                // Inline Error Message if any
                AnimatedVisibility(visible = !uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        fontFamily = AppFontFamily,
                        color = AgriError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Tạo tài khoản ngay
                AgriPrimaryButton(
                    text = "Tạo tài khoản ngay",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onRegisterClicked(activity)
                    },
                    isLoading = uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom Prompt outside card: "Đã có tài khoản? Đăng nhập"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNavigateToLogin
                    )
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    fontFamily = AppFontFamily,
                    fontSize = 15.sp,
                    color = Color(0xFF4A5568)
                )
                Text(
                    text = "Đăng nhập",
                    fontFamily = AppFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenPrimary
                )
            }
        }
    }
}
