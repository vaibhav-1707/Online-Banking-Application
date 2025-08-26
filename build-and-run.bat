@echo off
echo ========================================
echo   Online Banking Application Builder
echo ========================================
echo.

REM Check if Java is available
echo Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)
echo Java found! ✓
echo.

REM Create output directory if it doesn't exist
if not exist "build" mkdir build

REM Compile all Java files
echo Compiling Java source files...
javac -cp "lib/*" -d build -sourcepath src/main/java src/main/java/com/banking/**/*.java
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check for syntax errors in your Java files
    pause
    exit /b 1
)
echo Compilation successful! ✓
echo.

REM Copy resources to build directory
echo Copying configuration files...
if exist "resources" (
    xcopy "resources" "build" /E /I /Y >nul
    echo Resources copied! ✓
) else (
    echo Warning: resources folder not found
)
echo.

REM Run the application
echo Starting Online Banking Application...
echo.
java -cp "lib/*;build" com.banking.ui.MainWindow

echo.
echo Application closed. Press any key to exit...
pause >nul
