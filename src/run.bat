@echo off
title Iniciando Juego...

:: Buscar Java automaticamente
set JAVA_EXE=java.exe

:: Verificar si java esta disponible
%JAVA_EXE% -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: No se encontro Java
    echo.
    echo Por favor instala Java JDK desde:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo Iniciando con memoria optimizada...
echo.

:: Compilar si es necesario
if not exist "build\classes" (
    echo Compilando proyecto...
    javac -d build\classes src\*.java
)

:: Ejecutar
cd build\classes
%JAVA_EXE% -Xmx2048m -Xms512m Main

pause
```

---

## 📋 **Checklist para verificar:**

✅ El archivo se llama exactamente `run.bat` (no `run.bat.txt`)  
✅ Está en la carpeta raíz del proyecto  
✅ Cambiaste `Main` por el nombre de tu clase principal si es diferente  
✅ Java está instalado (escribe `java -version` en CMD)

---

## 🎯 **Estructura de tu proyecto debería verse así:**
```
MiJuego/
├── src/
│   ├── minijuego1.java
│   ├── Main.java
│   └── ...
├── build/
│   └── classes/
├── dist/
│   └── MiJuego.jar (si compilaste)
├── run.bat  ← AQUÍ va el archivo
└── nbproject/ o .idea/