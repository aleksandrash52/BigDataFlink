# BigDataFlink
Анализ больших данных - лабораторная работа №3 - Streaming processing с помощью Flink

## Архитектура

```
CSV файлы → Kafka Producer → Kafka → Flink → PostgreSQL (Star Schema)
```

## Структура данных

### Исходные данные (CSV)

10 файлов по 1000 строк каждый с полями:
- данные о клиентах (имя, возраст, email, страна и т.д.)
- данные о продавцах
- данные о продуктах
- данные о магазинах
- данные о поставщиках
- данные о продажах

### Модель «звезда» в PostgreSQL

**Dimension таблицы:**
1. `dim_customer` — информация о клиентах
2. `dim_seller` — информация о продавцах
3. `dim_product` — информация о продуктах
4. `dim_store` — информация о магазинах
5. `dim_supplier` — информация о поставщиках

**Fact таблица:**
- `fact_sales` — факты продаж со ссылками на dimension таблицы

## Структура проекта

```
BigDataFlink/
├── исходные данные/          # CSV файлы (10 файлов по 1000 строк)
├── src/main/java/com/bigdataflink/
│   ├── model/SaleRecord.java
│   ├── FlinkStarSchemaJob.java
│   └── StarSchemaSink.java
├── docker-compose.yml
├── init.sql
├── kafka_producer.py
├── requirements.txt
├── pom.xml
├── build.sh / build.bat
├── run.sh
└── README.md
```

## Запуск

### Автоматический прогон

```bash
cd BigDataFlink
bash run.sh
```

Скрипт соберёт JAR, поднимет Docker, дождётся Kafka producer и запустит Flink job.

### Ручной запуск

**1. Сборка**

```bash
bash build.sh
```

Создаётся файл `flink-app.jar` в корне проекта.

**2. Инфраструктура**

```bash
docker compose up -d
```

Запускаются:
- Zookeeper (порт 2181)
- Kafka (порт 9092)
- PostgreSQL (порт 5432)
- Kafka Producer (читает CSV и пишет в топик `sales-topic`)
- Flink JobManager (порт 8081)
- Flink TaskManager

**3. Проверка сервисов**

```bash
docker compose ps
docker compose logs kafka-producer
docker exec -it postgres psql -U postgres -d analytics -c "\dt analytics.*"
```

Дождитесь в логах producer сообщения о завершении отправки данных.

**4. Flink job**

```bash
docker exec flink-jobmanager /opt/flink/bin/flink run -d /opt/flink/usrlib/flink-app.jar
```

Флаг `-d` запускает job в фоне (streaming job не завершается сам).

**5. Мониторинг**

- Flink Web UI: http://localhost:8081
- Kafka:
  ```bash
  docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic sales-topic --from-beginning --max-messages 5
  ```
- PostgreSQL:
  ```bash
  docker exec -it postgres psql -U postgres -d analytics -c "SELECT COUNT(*) FROM analytics.fact_sales;"
  ```

## Проверка результатов

Количество строк во всех таблицах:

```bash
docker exec postgres psql -U postgres -d analytics -c "
SELECT 'dim_customer' AS t, COUNT(*) FROM analytics.dim_customer
UNION ALL SELECT 'dim_seller', COUNT(*) FROM analytics.dim_seller
UNION ALL SELECT 'dim_product', COUNT(*) FROM analytics.dim_product
UNION ALL SELECT 'dim_store', COUNT(*) FROM analytics.dim_store
UNION ALL SELECT 'dim_supplier', COUNT(*) FROM analytics.dim_supplier
UNION ALL SELECT 'fact_sales', COUNT(*) FROM analytics.fact_sales;
"
```

Ожидаемо: `fact_sales` — около 10000 строк; в dimension таблицах меньше строк из-за дедупликации по ключам.

Проверка связей star schema:

```bash
docker exec postgres psql -U postgres -d analytics -c "
SELECT f.sale_id, c.customer_email, p.product_name, f.sale_date, f.total_amount
FROM analytics.fact_sales f
JOIN analytics.dim_customer c ON f.customer_id = c.customer_id
JOIN analytics.dim_product p ON f.product_id = p.product_id
LIMIT 10;
"
```

## Устранение неполадок

**Kafka / топик `sales-topic`**

```bash
docker compose logs kafka
docker exec kafka kafka-topics --create --if-not-exists \
  --topic sales-topic --bootstrap-server localhost:9092 \
  --partitions 1 --replication-factor 1
```

**PostgreSQL**

```bash
docker compose logs postgres
docker exec postgres psql -U postgres -d analytics -c "\dt analytics.*"
```

**Flink job**

```bash
docker compose logs flink-jobmanager
docker exec flink-jobmanager /opt/flink/bin/flink list
docker exec flink-jobmanager /opt/flink/bin/flink cancel <job-id>
docker exec flink-jobmanager /opt/flink/bin/flink run -d /opt/flink/usrlib/flink-app.jar
```

**Producer падает при импорте pandas**

Убедитесь, что в `requirements.txt` указан `numpy==1.24.4` (совместим с `pandas==2.0.3`).

## Очистка

```bash
docker compose down
docker compose down -v   # с удалением данных PostgreSQL
rm -f flink-app.jar
```

## Технологии

- Apache Flink 1.17.1
- Apache Kafka
- PostgreSQL 15
- Docker и Docker Compose
- Python 3.9 (Kafka Producer)
- Java 11 (Flink приложение)

## Примечания

1. CSV файлы должны находиться в каталоге `исходные данные/`
2. `flink-app.jar` не коммитится в git — создаётся командой `bash build.sh`
3. При первом запуске Docker может долго скачивать образы
