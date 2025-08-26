@echo off
echo Starting Online Banking Application...
echo.

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)

echo Java found. Starting application...
echo.

REM Run the main banking application
java -cp "lib/*;src/main/java;resources" com.banking.ui.MainWindow

echo.
echo Application closed. Press any key to exit...
pause >nul
