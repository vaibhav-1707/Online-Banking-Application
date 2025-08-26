@echo off
setlocal enabledelayedexpansion

REM Simple MD022-like check: ensure one blank line after headings
REM Usage: scripts\check-md-headings.bat [file...]

set EXIT_CODE=0

if "%1"=="" (
    echo Checking all markdown files...
    for /r . %%f in (*.md) do call :check_file "%%f"
) else (
    for %%f in (%*) do call :check_file "%%f"
)

if %EXIT_CODE% equ 0 (
    echo All markdown files passed heading spacing checks.
) else (
    echo Some markdown files have heading spacing issues.
)

exit /b %EXIT_CODE%

:check_file
set file=%~1
set line_num=0
set prev_was_heading=0

for /f "usebackq delims=" %%l in ("%file%") do (
    set /a line_num+=1
    set line=%%l
    
    if !prev_was_heading! equ 1 (
        if not "!line!"=="" (
            echo %file%:!line_num!: MD022/blanks-around-headings: Expected 1 blank line below heading
            set EXIT_CODE=1
        )
        set prev_was_heading=0
    )
    
    echo !line!| findstr /r "^#.*" >nul
    if !errorlevel! equ 0 (
        set prev_was_heading=1
    )
)

goto :eof
