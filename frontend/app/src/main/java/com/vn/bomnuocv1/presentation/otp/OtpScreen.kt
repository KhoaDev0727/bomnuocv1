package com.vn.bomnuocv1.presentation.otp

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.presentation.common.AgriCardContainer
import com.vn.bomnuocv1.presentation.common.AgriInputField
import com.vn.bomnuocv1.presentation.common.AgriPrimaryButton
import com.vn.bomnuocv1.presentation.common.BrandLogoBadge
import com.vn.bomnuocv1.presentation.common.OtpDigitBoxes
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriError
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriLabelText
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.vn.bomnuocv1.ui.theme.AppFontFamily

@Composable
fun OtpScreen(
    phoneNumber: String? = null,
    fullName: String? = null,
    pinCode: String? = null,
    verificationId: String? = null,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(phoneNumber, fullName, pinCode, verificationId) {
        if (!phoneNumber.isNullOrBlank() && uiState.phoneNumber.isBlank()) {
            viewModel.setRegistrationData(
                phone = phoneNumber,
                name = fullName.orEmpty(),
                pin = pinCode.orEmpty(),
                verificationId = verificationId.orEmpty()
            )
        }
    }

    LaunchedEffect(uiState.isVerifySuccess) {
        if (uiState.isVerifySuccess) {
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

            // Screen Header Title
            Text(
                text = "Xác thực số điện thoại",
                fontFamily = AppFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle with phone number
            val displayPhone = uiState.phoneNumber.ifBlank { phoneNumber.orEmpty() }
            Text(
                text = if (displayPhone.isNotBlank()) {
                    "Mã OTP 6 chữ số đã được gửi đến\n$displayPhone"
                } else {
                    "Vui lòng nhập mã OTP 6 chữ số để xác thực"
                },
                fontFamily = AppFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5568),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main White Card Container matching 100% with the provided UI mockup
            AgriCardContainer {
                // If phone is missing, allow user to input phone number first
                if (displayPhone.isBlank()) {
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
                }

                // OTP Label
                Text(
                    text = "Mã xác thực OTP (6 số)",
                    fontFamily = AppFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AgriLabelText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 6-box OTP visual digit entry
                OtpDigitBoxes(
                    otpValue = uiState.otpCode,
                    onOtpChange = viewModel::onOtpCodeChanged,
                    length = 6
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resend OTP countdown or button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.canResend) {
                        Text(
                            text = "Chưa nhận được mã? ",
                            fontFamily = AppFontFamily,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568)
                        )
                        Text(
                            text = "Gửi lại ngay",
                            fontFamily = AppFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { viewModel.onResendOtpClicked(activity) }
                                )
                                .padding(vertical = 4.dp)
                        )
                    } else {
                        Text(
                            text = "Gửi lại mã sau ",
                            fontFamily = AppFontFamily,
                            fontSize = 14.sp,
                            color = Color(0xFF4A5568)
                        )
                        Text(
                            text = "${uiState.resendCountdown}s",
                            fontFamily = AppFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                }

                // Inline Error Message if any
                AnimatedVisibility(visible = !uiState.errorMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        fontFamily = AppFontFamily,
                        color = AgriError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }

                // Inline Info Message if any
                AnimatedVisibility(visible = !uiState.infoMessage.isNullOrBlank()) {
                    Text(
                        text = uiState.infoMessage.orEmpty(),
                        fontFamily = AppFontFamily,
                        color = AgriGreenPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Xác thực & Hoàn tất
                AgriPrimaryButton(
                    text = "Xác thực & Tiếp tục",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onVerifyOtpClicked()
                    },
                    isLoading = uiState.isLoading,
                    enabled = uiState.otpCode.length == 6
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom Prompt outside card: "Quay lại Đăng nhập"
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
                    text = "Quay lại ",
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
