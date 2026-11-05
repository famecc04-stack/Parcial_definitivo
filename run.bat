@echo off
setlocal enabledelayedexpansion

title SERF - FinanCorp S.A.
echo =============================================================
echo   SERF - Sistema Empresarial de Gestion de Reportes Financieros
echo   FinanCorp S.A. - Moneda Base: Euros (EUR)
echo =============================================================

set "JAVAC_EXE=javac"
set "JAVA_EXE=java"

where java >nul 2>&1
if %errorlevel% neq 0 (
    if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin\java.exe" (
        set "JAVAC_EXE=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin\javac.exe"
        set "JAVA_EXE=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin\java.exe"
    ) else (
        echo [!] No se encontro Java instalado en el sistema.
        echo Asegurese de tener instalado Java JDK 17 o superior.
        pause
        exit /b 1
    )
)

:: Si ya esta compilado, inicia directamente sin demoras
if not exist "bin\com\financorp\serf\App.class" (
    echo [*] Compilando clases Java por primera vez...
    if not exist "bin" mkdir "bin"
    dir /s /b src\main\java\*.java > sources_temp.txt
    "%JAVAC_EXE%" -encoding UTF-8 -d bin @sources_temp.txt
    del sources_temp.txt
    if not exist "bin\static" mkdir "bin\static"
    xcopy /E /I /Y "src\main\resources\static" "bin\static" >nul
)

echo [*] Abriendo la pagina web en tu navegador...
start "" "http://localhost:8080"

echo [*] Servidor en ejecucion en http://localhost:8080
echo [*] Para detener la aplicacion, simplemente cierra esta ventana.
echo.

"%JAVA_EXE%" -cp bin com.financorp.serf.App

pause
