package com.vn.bomnuocv1.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Helper to convert ISO date "yyyy-MM-dd" to Vietnamese display format "dd/MM/yyyy".
 */
fun formatIsoToVietnameseDate(isoDate: String): String {
    return try {
        val parts = isoDate.trim().split("-")
        if (parts.size == 3) {
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            isoDate
        }
    } catch (_: Exception) {
        isoDate
    }
}

/**
 * Beautiful Agricultural themed DatePickerDialog using Compose Material 3 DatePicker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriDatePickerDialog(
    initialIsoDate: String? = null,
    onDateSelected: (isoDate: String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = remember(initialIsoDate) {
        try {
            if (!initialIsoDate.isNullOrBlank()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                sdf.parse(initialIsoDate.trim())?.time
            } else {
                System.currentTimeMillis()
            }
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                            timeInMillis = millis
                        }
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val iso = String.format(Locale.US, "%04d-%02d-%02d", year, month, day)
                        onDateSelected(iso)
                    }
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgriGreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Chọn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hủy",
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        )
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "CHỌN NGÀY",
                    modifier = Modifier.padding(start = 24.dp, top = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AgriGreenPrimary
                )
            },
            headline = {
                Text(
                    text = "Chọn ngày thực hiện",
                    modifier = Modifier.padding(start = 24.dp, bottom = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AgriGreenDark
                )
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                selectedDayContainerColor = AgriGreenPrimary,
                selectedDayContentColor = Color.White,
                todayDateBorderColor = AgriGreenPrimary,
                todayContentColor = AgriGreenPrimary,
                dayContentColor = Color(0xFF1E293B),
                weekdayContentColor = Color(0xFF64748B),
                subheadContentColor = AgriGreenDark
            )
        )
    }
}
