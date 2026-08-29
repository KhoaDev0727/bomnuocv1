package com.vn.bomnuocv1.presentation.login

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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Smartphone
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
import com.vn.bomnuocv1.ui.theme.AppFontFamily

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToHome()
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

            Spacer(modifier = Modifier.height(18.dp))

            // App Title & Subtitle
            Text(
                text = "Bơm Nước",
                fontFamily = AppFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Đăng nhập nhanh bằng mã PIN",
                fontFamily = AppFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5568)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main White Card Container matching 100% with the provided UI mockup
            AgriCardContainer {
                // Phone Number Field
                AgriInputField(
                    value = uiState.phoneNumber,
                    onValueChange = viewModel::onPhoneNumberChanged,
                    label = "Số điện thoại",
                    placeholder = "Nhập số điện thoại",
                    leadingIcon = Icons.Outlined.Smartphone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PIN Code Field with Lock icon & Eye toggle
                AgriInputField(
                    value = uiState.pinCode,
                    onValueChange = viewModel::onPinCodeChanged,
                    label = "Mã PIN",
                    placeholder = "Nhập mã PIN",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (uiState.phoneNumber.isNotBlank() && uiState.pinCode.isNotBlank()) {
                                viewModel.onLoginClicked()
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Forgot PIN link (Quên mã PIN?) aligned to the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Quên mã PIN?",
                        fontFamily = AppFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onNavigateToForgotPassword(uiState.phoneNumber)
                            }
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    )
                }

                // Inline Error Message if any
                AnimatedVisibility(visible = !uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        fontFamily = AppFontFamily,
                        color = AgriError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Primary Action Button: Đăng nhập
                AgriPrimaryButton(
                    text = "Đăng nhập",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onLoginClicked()
                    },
                    isLoading = uiState.isLoading,
                    enabled = uiState.phoneNumber.isNotBlank() && uiState.pinCode.isNotBlank()
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Bottom prompt inside card: "Chưa có tài khoản? Đăng ký ngay"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onNavigateToRegister
                        )
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chưa có tài khoản? ",
                        fontFamily = AppFontFamily,
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568)
                    )
                    Text(
                        text = "Đăng ký ngay",
                        fontFamily = AppFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    )
                }
            }
        }
    }
}
