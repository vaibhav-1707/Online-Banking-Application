@echo off
echo Building Online Banking Application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 17 or higher
    pause
    exit /b 1
)

REM Check if lib directory exists
if not exist "lib" (
    echo ERROR: lib directory not found
    echo Please ensure all required JAR files are in the lib\ directory
    pause
    exit /b 1
)

REM Check required JAR files
if not exist "lib\postgresql-42.7.3.jar" (
    echo ERROR: postgresql-42.7.3.jar not found in lib\ directory
    pause
    exit /b 1
)

if not exist "lib\javax.mail-1.6.2.jar" (
    echo ERROR: javax.mail-1.6.2.jar not found in lib\ directory
    pause
    exit /b 1
)

echo Dependencies check passed!
echo.

REM Create output directory
if not exist "out" mkdir out

echo Compiling main Java files...
javac -cp "lib\*" -d out src\main\java\com\banking\model\*.java src\main\java\com\banking\services\*.java src\main\java\com\banking\ui\*.java
if %errorlevel% neq 0 (
    echo ERROR: Main compilation failed
    pause
    exit /b 1
)

echo Compiling test Java files...
javac -cp "lib\*;out" -d out src\test\java\com\banking\model\*.java src\test\java\com\banking\services\*.java src\test\java\com\banking\*.java
if %errorlevel% neq 0 (
    echo ERROR: Test compilation failed
    pause
    exit /b 1
)

echo Compilation successful!
echo.

echo Testing database connection...
java -cp "lib\*;out" com.banking.services.DatabaseTest

echo.
echo Starting application...
java -cp "lib\*;out" com.banking.ui.MainWindow

pause
