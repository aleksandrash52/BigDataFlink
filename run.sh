#!/bin/bash
set -euo pipefail

cd "$(dirname "$0")"

echo "=== 1. Сборка Flink JAR ==="
bash build.sh

echo ""
echo "=== 2. Запуск Docker Compose ==="
docker compose down -v
docker compose up -d

echo ""
echo "=== 3. Ожидание Kafka и producer ==="
echo "Ждём kafka-init..."
docker compose logs -f kafka-init &
INIT_PID=$!
sleep 15
kill $INIT_PID 2>/dev/null || true

echo "Ждём завершения kafka-producer (до 5 минут)..."
for i in $(seq 1 60); do
  if docker compose logs kafka-producer 2>&1 | grep -q "Все .* записей успешно отправлены"; then
    echo "Producer завершил отправку."
    break
  fi
  if docker compose logs kafka-producer 2>&1 | grep -q "Traceback"; then
    echo "Ошибка в kafka-producer:"
    docker compose logs kafka-producer --tail=30
    exit 1
  fi
  sleep 5
done

echo ""
echo "=== 4. Запуск Flink job (фоновый режим) ==="
docker exec flink-jobmanager /opt/flink/bin/flink run -d /opt/flink/usrlib/flink-app.jar

echo ""
echo "=== 5. Ожидание обработки и проверка PostgreSQL ==="
echo "Streaming job работает в фоне; ждём запись в БД (~60 сек)..."
sleep 60
docker exec postgres psql -U postgres -d analytics -c "
SELECT 'dim_customer' AS t, COUNT(*) FROM analytics.dim_customer
UNION ALL SELECT 'dim_seller', COUNT(*) FROM analytics.dim_seller
UNION ALL SELECT 'dim_product', COUNT(*) FROM analytics.dim_product
UNION ALL SELECT 'dim_store', COUNT(*) FROM analytics.dim_store
UNION ALL SELECT 'dim_supplier', COUNT(*) FROM analytics.dim_supplier
UNION ALL SELECT 'fact_sales', COUNT(*) FROM analytics.fact_sales;
"

echo ""
echo "Готово. Flink UI: http://localhost:8081"
