@echo off
REM
REM Renders the AsciiDoc chapters to PDF (and HTML, and DocBook), then converts
REM the DocBook to Word via Pandoc.
REM
REM The PDF is produced by asciidoctorj-pdf, which is pure Java, so Maven alone
REM is enough for it. There is no pure-Java AsciiDoc backend that writes .docx,
REM so the Word step shells out to Pandoc (https://pandoc.org), a single
REM self-contained binary, which reads the DocBook that the same Maven build
REM produces.
REM
REM Usage:
REM   generate-docs.bat              - clean build -^> PDF, HTML, DocBook, Word
REM   generate-docs.bat --no-clean   - incremental, skips `mvn clean`
REM   generate-docs.bat --skip-word  - PDF/HTML/DocBook only, ignores Pandoc
REM
setlocal enabledelayedexpansion

cd /d "%~dp0"

set "CLEAN=clean"
set "SKIP_WORD=false"

:parse
if "%~1"=="" goto parsed
if /i "%~1"=="--no-clean"  ( set "CLEAN=" & shift & goto parse )
if /i "%~1"=="--skip-word" ( set "SKIP_WORD=true" & shift & goto parse )
if /i "%~1"=="-h"          goto usage
if /i "%~1"=="--help"      goto usage
echo Unknown option: %~1 ^(try --help^)>&2
exit /b 2

:usage
echo Usage:
echo   generate-docs.bat              - clean build -^> PDF, HTML, DocBook, Word
echo   generate-docs.bat --no-clean   - incremental, skips `mvn clean`
echo   generate-docs.bat --skip-word  - PDF/HTML/DocBook only, ignores Pandoc
exit /b 0

:parsed
set "DOCBOOK_DIR=target\docbook"
set "WORD_DIR=target\word"

REM --- 1. AsciiDoc -> PDF + HTML + DocBook ------------------------------------

REM The build runs under the JDK 26 toolchain declared by the parent aggregator.
REM JAVA_HOME_26 is a *user* environment variable, so a shell that was already
REM open when it was defined will not see it; fall back to the default install
REM location rather than failing with "Non-existing JDK home".
if not defined JAVA_HOME_26 (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-26.0.1.8-hotspot" (
        set "JAVA_HOME_26=C:\Program Files\Eclipse Adoptium\jdk-26.0.1.8-hotspot"
        echo Note: JAVA_HOME_26 was not set, using the default Adoptium JDK 26 location.
    ) else (
        echo Note: JAVA_HOME_26 is not set. If the toolchains plugin fails with
        echo       "Non-existing JDK home", set it to your JDK 26 installation.
    )
)

echo ==^> Rendering the chapters with Maven
call mvn %CLEAN% package
if errorlevel 1 exit /b 1

REM --- 2. DocBook -> Word -----------------------------------------------------

if "%SKIP_WORD%"=="true" (
    echo ==^> Skipping Word output ^(--skip-word^)
    exit /b 0
)

REM Pandoc may be on the PATH or under PANDOC_HOME, which is also what activates
REM the `word` Maven profile.
set "PANDOC="
for /f "delims=" %%p in ('where pandoc 2^>nul') do if not defined PANDOC set "PANDOC=%%p"
if not defined PANDOC if defined PANDOC_HOME if exist "%PANDOC_HOME%\pandoc.exe"     set "PANDOC=%PANDOC_HOME%\pandoc.exe"
if not defined PANDOC if defined PANDOC_HOME if exist "%PANDOC_HOME%\bin\pandoc.exe" set "PANDOC=%PANDOC_HOME%\bin\pandoc.exe"

if not defined PANDOC (
    echo.
    echo WARNING: Pandoc was not found, so no .docx was written.
    echo          PDF, HTML and DocBook are in target\ and are complete.
    echo          Install Pandoc from https://pandoc.org/installing.html
    echo          ^(or "winget install --id JohnMacFarlane.Pandoc"^), or set
    echo          PANDOC_HOME to the directory holding the binary, then re-run.
    exit /b 0
)

if not exist "%WORD_DIR%" mkdir "%WORD_DIR%"

REM Every chapter, rather than a hard-coded list, so a third chapter needs no edit.
for %%f in ("%DOCBOOK_DIR%\*.xml") do (
    echo ==^> %%~nf.docx
    REM `call`, so that a .bat/.cmd wrapper around Pandoc returns here.
    call "%PANDOC%" --from=docbook --to=docx --highlight-style=tango --toc ^
        "--output=%WORD_DIR%\%%~nf.docx" "%%f"
    if errorlevel 1 exit /b 1
)

echo.
echo Done:
echo   PDF     target\pdf
echo   HTML    target\html
echo   DocBook target\docbook
echo   Word    target\word

endlocal
