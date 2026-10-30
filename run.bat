@echo off
echo ============================================
echo  QUIZ MANAGEMENT SYSTEM — Build and Run
echo ============================================
echo.

REM Find mysql-connector jar dynamically
set JAR_FILE=
for %%f in (lib\mysql-connector-j-*.jar) do set JAR_FILE=%%f

if "%JAR_FILE%"=="" (
    echo ERROR: MySQL Connector JAR not found in lib\ folder!
    echo.
    echo Please download mysql-connector-j from:
    echo https://dev.mysql.com/downloads/connector/j/
    echo and place the .jar file in the lib\ folder.
    echo.
    pause
    exit /b 1
)

echo Found: %JAR_FILE%
echo.

REM Clean and create output directory
if exist out rmdir /s /q out
mkdir out

echo Compiling Java source files...
javac -encoding UTF-8 -cp "%JAR_FILE%" -d out src\*.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo COMPILATION FAILED! Please check the error messages above.
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting Quiz Management System...
echo.

java -Dfile.encoding=UTF-8 -cp "out;%JAR_FILE%" Main

pause
