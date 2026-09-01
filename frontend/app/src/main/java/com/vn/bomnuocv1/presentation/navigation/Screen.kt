package com.vn.bomnuocv1.presentation.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object OtpVerification : Screen("otp?phoneNumber={phoneNumber}&fullName={fullName}&pinCode={pinCode}&verificationId={verificationId}") {
        fun createRoute(
            phoneNumber: String = "",
            fullName: String = "",
            pinCode: String = "",
            verificationId: String = ""
        ): String {
            val encodedName = URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString())
            val encodedVid = URLEncoder.encode(verificationId, StandardCharsets.UTF_8.toString())
            return "otp?phoneNumber=$phoneNumber&fullName=$encodedName&pinCode=$pinCode&verificationId=$encodedVid"
        }
    }
    data object Home : Screen("home")
    data object Pricing : Screen("pricing")
    data object Settings : Screen("settings")
}

