#!/bin/bash

# Проверка наличия Maven
if ! command -v mvn &> /dev/null; then
    echo "Ошибка: Maven не установлен. Установите Maven и повторите попытку."
    exit 1
fi

echo "1. Очистка проекта"
mvn clean

echo "2. Сборка проекта"
mvn package

if [ $? -eq 0 ]; then
    echo "3. Переименование JAR файла"
    if [ -f "target/bigdata-flink-job-1.0-SNAPSHOT.jar" ]; then
        cp target/bigdata-flink-job-1.0-SNAPSHOT.jar flink-app.jar
        echo "Сборка успешно завершена."
        echo "JAR файл: flink-app.jar"
        echo ""
        echo "Для запуска проекта выполните:"
        echo "  docker compose up -d"
        echo "  docker exec flink-jobmanager /opt/flink/bin/flink run -d /opt/flink/usrlib/flink-app.jar"
    else
        echo "Ошибка: JAR файл не найден в директории target/"
        exit 1
    fi
else
    echo "Ошибка при сборке проекта"
    exit 1
fi