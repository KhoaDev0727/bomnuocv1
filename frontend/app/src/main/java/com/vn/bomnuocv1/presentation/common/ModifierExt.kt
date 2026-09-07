package com.vn.bomnuocv1.presentation.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * Modifier extension that dismisses the software keyboard and clears focus
 * when the user taps anywhere outside of an active input field.
 */
fun Modifier.clearFocusOnTap(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
            keyboardController?.hide()
        })
    }
}
