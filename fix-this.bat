@echo off
echo ========================================
echo   FIXING YOUR BANKING APP RIGHT NOW
echo ========================================
echo.

REM Check Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found!
    pause
    exit /b 1
)

REM Create build directory
if not exist "build" mkdir build
if not exist "build\com\banking\model" mkdir "build\com\banking\model"
if not exist "build\com\banking\services" mkdir "build\com\banking\services"
if not exist "build\com\banking\ui" mkdir "build\com\banking\ui"

echo Compiling Java files...

REM Compile model classes
javac -cp "lib/*" -d build src/main/java/com/banking/model/*.java
if %errorlevel% neq 0 goto :error

REM Compile service classes  
javac -cp "lib/*;build" -d build src/main/java/com/banking/services/*.java
if %errorlevel% neq 0 goto :error

REM Compile UI classes
javac -cp "lib/*;build" -d build src/main/java/com/banking/ui/*.java
if %errorlevel% neq 0 goto :error

echo Compilation successful! ✓

REM Copy resources
echo Copying config files...
xcopy "resources" "build" /E /I /Y >nul

echo.
echo STARTING YOUR BANKING APP NOW...
echo.
java -cp "lib/*;build" com.banking.ui.MainWindow

echo.
echo App closed. Press any key to exit...
pause >nul
exit /b 0

:error
echo.
echo COMPILATION FAILED! Check your Java files for errors.
pause
exit /b 1
