@echo off
echo Testing Email Service...
echo.

REM Compile and run the email test
javac -cp "lib/*;src/main/java" src/main/java/com/banking/services/EmailTest.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running Email Test...
echo.
java -cp "lib/*;src/main/java;resources" com.banking.services.EmailTest

echo.
echo Test completed. Press any key to exit...
pause >nul
