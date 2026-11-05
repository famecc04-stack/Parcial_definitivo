Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host "  SERF - Sistema Empresarial de Gestión de Reportes Financieros" -ForegroundColor White
Write-Host "  FinanCorp S.A. - Moneda Base: Euros (EUR)" -ForegroundColor White
Write-Host "=============================================================" -ForegroundColor Cyan

$javac = "javac"
$java = "java"

if (-not (Get-Command "javac" -ErrorAction SilentlyContinue)) {
    $jbrJavac = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin\javac.exe"
    $jbrJava = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr\bin\java.exe"
    if (Test-Path $jbrJavac) {
        $javac = $jbrJavac
        $java = $jbrJava
    } else {
        Write-Error "No se encontró javac en el sistema. Instale Java JDK 17 o superior."
        exit 1
    }
}

Write-Host "[*] Compilando clases Java..." -ForegroundColor Yellow
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
$sources = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
& $javac -encoding UTF-8 -d bin $sources

if ($LASTEXITCODE -ne 0) {
    Write-Error "Error en la compilación de Java."
    exit 1
}

Write-Host "[*] Sincronizando interfaz web estática..." -ForegroundColor Yellow
Copy-Item -Path "src/main/resources/static" -Destination "bin/static" -Recurse -Force

Write-Host "[*] Iniciando servidor web SERF..." -ForegroundColor Green
Write-Host "    --> Abra su navegador en: http://localhost:8080" -ForegroundColor Cyan
Write-Host "    --> Presione Ctrl + C para detener el servicio." -ForegroundColor DarkGray

& $java -cp bin com.financorp.serf.App
