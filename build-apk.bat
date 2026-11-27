@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

echo ============================================
echo   GorphinAI - Android APK Builder (Windows)
echo ============================================
echo.

cd /d "%~dp0"

:: 1. Detect JAVA_HOME (fallback to Android Studio bundled JBR)
if not defined JAVA_HOME (
    if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" (
        set "JAVA_HOME=%ProgramFiles%\Android\Android Studio\jbr"
    )
)
if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java JDK 17+ not found.
    echo Install Android Studio or JDK 17+ and set JAVA_HOME.
    pause
    exit /b 1
)

:: 2. Detect Android SDK
if not defined ANDROID_SDK_ROOT (
    if defined ANDROID_HOME (
        set "ANDROID_SDK_ROOT=%ANDROID_HOME%"
    ) else if exist "%LOCALAPPDATA%\Android\Sdk" (
        set "ANDROID_SDK_ROOT=%LOCALAPPDATA%\Android\Sdk"
        set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
    )
)

if not defined ANDROID_SDK_ROOT (
    echo [ERROR] Android SDK not found.
    echo Set ANDROID_SDK_ROOT or install Android SDK via Android Studio.
    pause
    exit /b 1
)
echo [OK] Android SDK: %ANDROID_SDK_ROOT%

:: 3. Generate debug.keystore if missing (required by app/build.gradle.kts)
if not exist "%~dp0debug.keystore" (
    echo [INFO] debug.keystore not found. Creating local debug.keystore...
    keytool -genkey -v -keystore "%~dp0debug.keystore" -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"
    if errorlevel 1 (
        echo [ERROR] Failed to generate debug.keystore with keytool.
        pause
        exit /b 1
    )
)

:: 4. Build Debug APK using gradlew.bat or system gradle
echo.
echo [INFO] Building GorphinAI Debug APK...
if exist "%~dp0gradlew.bat" (
    call "%~dp0gradlew.bat" assembleDebug
) else (
    where gradle >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Neither gradlew.bat nor system gradle was found.
        pause
        exit /b 1
    )
    call gradle assembleDebug
)

if errorlevel 1 (
    echo.
    echo [ERROR] Build failed. Check the Gradle output above.
    pause
    exit /b 1
)

:: 5. Copy APK to root folder for convenience
set "APK_SRC=%~dp0app\build\outputs\apk\debug\app-debug.apk"
set "APK_OUT=%~dp0GorphinAI-debug.apk"

if exist "%APK_SRC%" (
    copy /Y "%APK_SRC%" "%APK_OUT%" >nul
    echo.
    echo ============================================
    echo [SUCCESS] APK ready:
    echo   %APK_OUT%
    echo   (original: %APK_SRC%)
    echo ============================================
) else (
    echo [WARN] Build finished, but app-debug.apk was not found at expected path.
)

echo.
pause
endlocal
