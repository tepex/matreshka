package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AuthStep {
    ENTER_PHONE, ENTER_CODE
}

@Composable
fun LoginScreen() {
    var currentStep by remember { mutableStateOf(AuthStep.ENTER_PHONE) }
    var phoneNumber by remember { mutableStateOf("") }
    var smsCode by remember { mutableStateOf("") }

    val backgroundWhite = Color(0xFFFFFFFF)
    val brandRed = Color(0xFFE53935)
    val textDark = Color(0xFF1A1A1A)
    val grayField = Color(0xFFF4F4F6)
    val buttonBlue = Color(0xFF3B638A)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // Иконка-логотип YouPlan
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .background(brandRed, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(2) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "YouPlan",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = brandRed,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            when (currentStep) {
                AuthStep.ENTER_PHONE -> {
                    Text(
                        text = "Войди, чтобы сохранять привычки",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = { Text("Телефон", color = Color.Gray.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = grayField,
                            unfocusedContainerColor = grayField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { currentStep = AuthStep.ENTER_CODE },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
                    ) {
                        Text("Получить код", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }

                AuthStep.ENTER_CODE -> {
                    Text(
                        text = "Введите код из СМС",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .size(width = 56.dp, height = 60.dp)
                                    .background(grayField, shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (smsCode.length > index) smsCode[index].toString() else "",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { /* Логика авторизации */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
                    ) {
                        Text("Отправить", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Отправить повторно через 0:47",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
