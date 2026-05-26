import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Matreshka"
            isStatic = false
            /* Костыль! Говорим линкеру считать этот конкретный класс динамическим/необязательным.
            Передает линкеру команду unresolved, разрешая символу _OBJC_CLASS_$_UITextLoupeSession оставаться неопределенным
            во время компиляции фреймворка. Без этого при сборке iOS-приложения может возникать ошибка
            "Undefined symbols for architecture x86_64: _OBJC_CLASS_$_UITextLoupeSession" из-за того, что этот класс
            используется внутри UIKit, но не всегда включается в финальный бинарник. */
            linkerOpts("-Wl,-U,_OBJC_CLASS_\$_UITextLoupeSession")
            binaryOptions["bundleId"] = "com.habitloop.app.matreshka"
        }
    }
    
    android {
       namespace = "com.habitloop.app.shared.matreshka"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(compose.materialIconsExtended)

            implementation(compose.components.uiToolingPreview)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
