@echo off

REM Проверка наличия Maven
where mvn >nul 2>nul
if errorlevel 1 (
    echo Ошибка: Maven не установлен. Установите Maven и повторите попытку.
    exit /b 1
)

echo 1. Очистка проекта
call mvn clean

echo 2. Сборка проекта
call mvn package

if %errorlevel% equ 0 (
    echo 3. Переименование JAR файла
    if exist "target\bigdata-flink-job-1.0-SNAPSHOT.jar" (
        copy /Y "target\bigdata-flink-job-1.0-SNAPSHOT.jar" "flink-app.jar"
        echo Сборка успешно завершена.
        echo JAR файл: flink-app.jar
        echo.
        echo Для запуска проекта выполните:
        echo   docker compose up -d
        echo   docker exec flink-jobmanager /opt/flink/bin/flink run -d /opt/flink/usrlib/flink-app.jar
    ) else (
        echo Ошибка: JAR файл не найден в директории target/
        exit /b 1
    )
) else (
    echo Ошибка при сборке проекта
    exit /b 1
)