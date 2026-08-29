package com.vn.bomnuocv1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vn.bomnuocv1.presentation.home.HomeScreen
import com.vn.bomnuocv1.presentation.login.LoginScreen
import com.vn.bomnuocv1.presentation.otp.OtpScreen
import com.vn.bomnuocv1.presentation.register.RegisterScreen
import com.vn.bomnuocv1.presentation.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = { phone ->
                    navController.navigate(Screen.OtpVerification.createRoute(phone))
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToOtp = { phone, name, pin, vId ->
                    navController.navigate(Screen.OtpVerification.createRoute(phone, name, pin, vId))
                }
            )
        }

        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("fullName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("pinCode") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("verificationId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
            val rawName = backStackEntry.arguments?.getString("fullName").orEmpty()
            val name = try {
                java.net.URLDecoder.decode(rawName, java.nio.charset.StandardCharsets.UTF_8.toString())
            } catch (_: Exception) {
                rawName
            }
            val pin = backStackEntry.arguments?.getString("pinCode").orEmpty()
            val rawVid = backStackEntry.arguments?.getString("verificationId").orEmpty()
            val vId = try {
                java.net.URLDecoder.decode(rawVid, java.nio.charset.StandardCharsets.UTF_8.toString())
            } catch (_: Exception) {
                rawVid
            }

            OtpScreen(
                phoneNumber = phone,
                fullName = name,
                pinCode = pin,
                verificationId = vId,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
