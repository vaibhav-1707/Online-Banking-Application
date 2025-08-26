@echo off
setlocal enabledelayedexpansion

echo Running Online Banking Application Tests...
echo.

echo 1. Checking markdown heading spacing...
call scripts\check-md-headings.bat IMPLEMENTATION_SUMMARY.md
if %errorlevel% neq 0 (
    echo FAILED: Markdown heading spacing issues found
    set OVERALL_RESULT=1
) else (
    echo PASSED: Markdown heading spacing is correct
)

echo.
echo 2. Checking markdown heading spacing in README.md...
call scripts\check-md-headings.bat README.md
if %errorlevel% neq 0 (
    echo FAILED: Markdown heading spacing issues found in README.md
    set OVERALL_RESULT=1
) else (
    echo PASSED: README.md heading spacing is correct
)

echo.
echo 3. Compiling Java source in correct order...
echo   - Compiling model classes first...
javac -cp "lib\*" -d . src\main\java\com\banking\model\*.java
if %errorlevel% neq 0 (
    echo FAILED: Model classes compilation error
    set OVERALL_RESULT=1
    goto :end
)

echo   - Compiling service classes...
javac -cp "lib\*;." -d . src\main\java\com\banking\services\*.java
if %errorlevel% neq 0 (
    echo FAILED: Service classes compilation error
    set OVERALL_RESULT=1
    goto :end
)

echo   - Compiling UI classes...
javac -cp "lib\*;." -d . src\main\java\com\banking\ui\*.java
if %errorlevel% neq 0 (
    echo FAILED: UI classes compilation error
    set OVERALL_RESULT=1
    goto :end
)

echo PASSED: Java compilation successful

:end
echo.
if defined OVERALL_RESULT (
    echo ========================================
    echo SOME TESTS FAILED - Please fix the issues above
    echo ========================================
    exit /b 1
) else (
    echo ========================================
    echo ALL TESTS PASSED! 
    echo ========================================
    echo.
    echo You can now run the application with:
    echo   build.bat
)
