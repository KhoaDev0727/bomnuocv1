package com.vn.bomnuocv1.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.bomnuocv1.ui.theme.AppFontFamily
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriError
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenLight
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriInputBorder
import com.vn.bomnuocv1.ui.theme.AgriLabelText
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer
import com.vn.bomnuocv1.ui.theme.AgriOfflineBackground
import com.vn.bomnuocv1.ui.theme.AgriOfflineText
import com.vn.bomnuocv1.ui.theme.AgriPlaceholder
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.vn.bomnuocv1.R
import com.vn.bomnuocv1.ui.theme.AgriSurface

/**
 * Brand Logo Badge displaying the custom water pump logo from app_logo.png inside a rounded mint container.
 */
@Composable
fun BrandLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 104.dp
) {
    Surface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(24.dp),
        color = AgriMintContainer,
        border = BorderStroke(1.2.dp, AgriMintBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo Bơm Nước",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

/**
 * Custom vector leaf drawing matching agricultural branding
 */
@Composable
private fun LeafIconGraphic(
    modifier: Modifier = Modifier,
    color: Color = AgriGreenLight
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            // Main organic curved leaf
            moveTo(w * 0.5f, h * 0.1f)
            cubicTo(
                w * 0.85f, h * 0.25f,
                w * 0.95f, h * 0.65f,
                w * 0.65f, h * 0.88f
            )
            cubicTo(
                w * 0.45f, h * 0.98f,
                w * 0.20f, h * 0.90f,
                w * 0.15f, h * 0.70f
            )
            cubicTo(
                w * 0.10f, h * 0.48f,
                w * 0.28f, h * 0.25f,
                w * 0.5f, h * 0.1f
            )
            close()

            // Inner droplet cutout / loop
            moveTo(w * 0.48f, h * 0.35f)
            cubicTo(
                w * 0.35f, h * 0.48f,
                w * 0.32f, h * 0.68f,
                w * 0.45f, h * 0.78f
            )
            cubicTo(
                w * 0.58f, h * 0.82f,
                w * 0.72f, h * 0.72f,
                w * 0.68f, h * 0.52f
            )
            cubicTo(
                w * 0.65f, h * 0.38f,
                w * 0.55f, h * 0.30f,
                w * 0.48f, h * 0.35f
            )
            close()
        }

        drawPath(
            path = path,
            color = color,
            style = Fill
        )
    }
}

/**
 * Standard white rounded Card container with darker distinct border.
 */
@Composable
fun AgriCardContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AgriSurface,
        border = BorderStroke(1.dp, AgriCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            content = content
        )
    }
}

/**
 * Redesigned Input Field matching the provided design:
 * - External label in high-contrast slate
 * - Rounded outline (14dp) with distinct darker border
 * - Leading icon & Optional Trailing icon / password toggle
 */
@Composable
fun AgriInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = AppFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AgriLabelText,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = AppFontFamily,
                    color = AgriPlaceholder,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = TextStyle(
                fontFamily = AppFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0F172A)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriInputBorder,
                focusedContainerColor = AgriSurface,
                unfocusedContainerColor = AgriSurface,
                errorBorderColor = AgriError,
                cursorColor = AgriGreenPrimary
            ),
            isError = isError
        )
        AnimatedVisibility(visible = isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage.orEmpty(),
                fontFamily = AppFontFamily,
                color = AgriError,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Primary Deep Green Button matching the mockup
 */
@Composable
fun AgriPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = AgriGreenPrimary,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.7f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = text,
                fontFamily = AppFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

/**
 * Premium 6-Box OTP Visual Input Display
 */
@Composable
fun OtpDigitBoxes(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    length: Int = 6,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = otpValue,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() }
            if (filtered.length <= length) {
                onOtpChange(filtered)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until length) {
                    val char = otpValue.getOrNull(i)?.toString().orEmpty()
                    val isFocused = otpValue.length == i || (i == length - 1 && otpValue.length == length)
                    
                    Box(
                        modifier = Modifier
                            .size(width = 46.dp, height = 54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AgriSurface)
                            .border(
                                width = if (isFocused) 1.5.dp else 1.dp,
                                color = if (isFocused) AgriGreenPrimary else AgriInputBorder,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontFamily = AppFontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

/**
 * Backward compatibility alias for AgriHeader and AgriButton
 */
@Composable
fun AgriHeader(
    title: String = "Bơm Nước",
    subtitle: String? = "Đăng nhập nhanh bằng mã PIN",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandLogoBadge(size = 96.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontFamily = AppFontFamily,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AgriGreenDark
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontFamily = AppFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5568),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AgriButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = AgriGreenPrimary,
    contentColor: Color = Color.White
) {
    AgriPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@Composable
fun OfflineStatusBadge(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = AgriOfflineBackground,
        border = BorderStroke(1.dp, AgriOfflineText.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AgriOfflineText)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Đang ở chế độ Ngoại tuyến (Offline)",
                fontFamily = AppFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriOfflineText
            )
        }
    }
}
