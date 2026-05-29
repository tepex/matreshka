package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AuthStep {
    ENTER_PHONE, ENTER_CODE/*, HABIT_DAY*/
}

@Composable
fun LoginScreen(
    onSuccess: () -> Unit = {}
) {
    val settings = remember { Settings() }

    var currentStep by remember { mutableStateOf(AuthStep.ENTER_PHONE) }
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }

    // Флаги ошибок для шагов
    var isEmailError by remember { mutableStateOf(false) }
    var isCodeError by remember { mutableStateOf(false) }

    // Для управления скрытым полем ввода кода
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                    println("[Habit]: saved_email on start: ${settings.getString("saved_email", "")}")
                    Text(
                        text = "Войди, чтобы сохранять привычки",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailError = false // сбрасываем ошибку при вводе
                        },
                        placeholder = { Text("E-mail", color = Color.Gray.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = isEmailError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = grayField,
                            unfocusedContainerColor = grayField,
                            errorContainerColor = grayField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = brandRed
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    if (isEmailError) {
                        Text(
                            text = "Email не найден в настройках",
                            color = brandRed,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val cleanEmail = email.trim().lowercase()
                            val savedEmail = settings.getString("saved_email", "")

                            println("[Habit]: savedEmail: $savedEmail")

                            if (cleanEmail.isNotEmpty() && cleanEmail == savedEmail) {
                                settings["is_logged_in"] = true

                                focusManager.clearFocus()
                                onSuccess()
                                //currentStep = AuthStep.HABIT_DAY
                            } else {
                                // Email НЕ совпал с сохраненным (или пустой) -> идем запрашивать код "0000"
                                isEmailError = false
                                currentStep = AuthStep.ENTER_CODE
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
                    ) {
                        Text("Войти или получить код", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }

                AuthStep.ENTER_CODE -> {
                    Text(
                        text = "Введите код",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ПУНКТ 3: Красивые боксы кода, связанные со скрытым текстовым полем
                    Box(contentAlignment = Alignment.Center) {
                        // Скрытый TextField, который собирает фокус и ввод цифровой клавиатуры
                        TextField(
                            value = verificationCode,
                            onValueChange = { input ->
                                if (input.length <= 4) {
                                    verificationCode = input
                                    isCodeError = false // сбрасываем ошибку при вводе нового символа
                                }
                            },
                            modifier = Modifier
                                .size(1.dp)
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        // ряд боксов для 4 цифр
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { focusRequester.requestFocus() }, // фокус при клике на боксы
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            repeat(4) { index ->
                                val char = if (verificationCode.length > index) verificationCode[index].toString() else ""
                                Box(
                                    modifier = Modifier
                                        .size(width = 56.dp, height = 60.dp)
                                        .background(
                                            if (isCodeError) brandRed.copy(alpha = 0.1f) else grayField,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCodeError) brandRed else textDark
                                    )
                                }
                            }
                        }
                    }
                    if (isCodeError) {
                        Text(
                            text = "Неверный код",
                            color = brandRed,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            // @TODO: Хардкодинг. Валидация кода
                            if (verificationCode == "0000") {
                                isCodeError = false
                                settings["is_logged_in"] = true
                                settings["saved_email"] = email.trim().lowercase()
                                focusManager.clearFocus()
                                //currentStep = AuthStep.HABIT_DAY
                                onSuccess()
                            } else {
                                isCodeError = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
                    ) {
                        Text("Отправить", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    // @TODO: сделать таймер переотправки кода.
                    Text(
                        text = "Отправить повторно через 0:47",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
                /*
                AuthStep.HABIT_DAY -> {
                    HabitDayScreen()
                }*/
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
