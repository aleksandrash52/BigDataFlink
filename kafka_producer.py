import os
import json
import time
import math
import pandas as pd
from kafka import KafkaProducer
from kafka.errors import NoBrokersAvailable


def clean_value(value):
    if value is None:
        return None
    if isinstance(value, float) and math.isnan(value):
        return None
    if hasattr(value, "item"):
        return value.item()
    return value


def clean_record(record):
    return {key: clean_value(val) for key, val in record.items()}


def read_csv_files(data_dir):
    """Чтение всех CSV файлов из директории"""
    all_data = []
    for filename in os.listdir(data_dir):
        if filename.endswith('.csv') and not filename.endswith('Zone.Identifier'):
            filepath = os.path.join(data_dir, filename)
            print(f"Чтение файла: {filename}")
            try:
                df = pd.read_csv(filepath)
                all_data.extend(clean_record(row) for row in df.to_dict('records'))
            except Exception as e:
                print(f"Ошибка при чтении файла {filename}: {e}")
    return all_data

def create_producer(bootstrap_servers):
    """Создание Kafka Producer"""
    max_retries = 10
    retry_delay = 5
    
    for i in range(max_retries):
        try:
            producer = KafkaProducer(
                bootstrap_servers=bootstrap_servers,
                value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                acks='all',
                retries=3
            )
            print("Kafka Producer успешно создан")
            return producer
        except NoBrokersAvailable:
            if i < max_retries - 1:
                print(f"Брокер Kafka недоступен, повторная попытка через {retry_delay} секунд ({i+1}/{max_retries})")
                time.sleep(retry_delay)
            else:
                print("Не удалось подключиться к Kafka после всех попыток")
                raise

def send_to_kafka(producer, topic, data):
    """Отправка данных в Kafka"""
    total_records = len(data)
    print(f"Начинаю отправку {total_records} записей в топик {topic}")
    
    for i, record in enumerate(data, 1):
        try:
            producer.send(topic, value=record)
            
            if i % 100 == 0:
                print(f"Отправлено {i}/{total_records} записей")
                producer.flush()
                
        except Exception as e:
            print(f"Ошибка при отправке записи {i}: {e}")
    
    producer.flush()
    print(f"Все {total_records} записей успешно отправлены в Kafka")

def main():
    # Получение настроек из переменных окружения
    bootstrap_servers = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:9092')
    topic = os.getenv('KAFKA_TOPIC', 'sales-topic')
    data_dir = '/app/data'
    
    print(f"Настройки:")
    print(f"  Kafka серверы: {bootstrap_servers}")
    print(f"  Топик: {topic}")
    print(f"  Директория с данными: {data_dir}")
    
    # Чтение данных из CSV файлов
    print("\nЧтение CSV файлов")
    data = read_csv_files(data_dir)
    print(f"Прочитано {len(data)} записей")
    
    if not data:
        print("Нет данных для отправки")
        return
    
    # Создание Kafka Producer
    print("\nПодключение к Kafka")
    producer = create_producer(bootstrap_servers)
    
    # Отправка данных в Kafka
    print("\nОтправка данных в Kafka")
    send_to_kafka(producer, topic, data)
    
    # Закрытие producer
    producer.close()
    print("\nВсе данные отправлены в Kafka")

if __name__ == "__main__":
    main()