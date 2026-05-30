package com.bigdataflink;

import com.bigdataflink.model.SaleRecord;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkStarSchemaJob {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("sales-topic")
                .setGroupId("flink-star-schema-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> kafkaStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "Kafka Source"
        );

        DataStream<SaleRecord> saleRecords = kafkaStream
                .map(new MapFunction<String, SaleRecord>() {
                    @Override
                    public SaleRecord map(String value) {
                        try {
                            SaleRecord record = objectMapper.readValue(value, SaleRecord.class);
                            if (record.getCustomerEmail() == null || record.getProductName() == null) {
                                return null;
                            }
                            return record;
                        } catch (Exception e) {
                            System.err.println("Ошибка при парсинге JSON: " + e.getMessage());
                            return null;
                        }
                    }
                })
                .filter(record -> record != null);

        saleRecords.addSink(new StarSchemaSink()).name("Star Schema Sink");

        env.execute("Flink Star Schema Transformation Job");
    }
}
