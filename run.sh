#!/usr/bin/env bash
set -e

echo "============================================================="
echo "  SERF - Sistema Empresarial de Gestión de Reportes Financieros"
echo "  FinanCorp S.A. - Moneda Base: Euros (EUR)"
echo "============================================================="

echo "[*] Compilando clases Java..."
mkdir -p bin
javac -encoding UTF-8 -d bin $(find src/main/java -name "*.java")

echo "[*] Sincronizando interfaz web estática..."
mkdir -p bin/static
cp -r src/main/resources/static/* bin/static/

echo "[*] Iniciando servidor web en http://localhost:8080 ..."
echo "Presione Ctrl + C para detener."
java -cp bin com.financorp.serf.App
