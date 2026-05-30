package com.bigdataflink;

import com.bigdataflink.model.SaleRecord;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class StarSchemaSink extends RichSinkFunction<SaleRecord> {

    private static final String JDBC_URL = "jdbc:postgresql://postgres:5432/analytics";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private transient Connection connection;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        connection.setAutoCommit(false);
    }

    @Override
    public void invoke(SaleRecord record, Context context) throws Exception {
        int customerId = upsertCustomer(record);
        int sellerId = upsertSeller(record);
        int productId = upsertProduct(record);
        int storeId = upsertStore(record);
        int supplierId = upsertSupplier(record);
        insertFact(record, customerId, sellerId, productId, storeId, supplierId);
        connection.commit();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.close();
    }

    private int upsertCustomer(SaleRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.dim_customer (customer_email, first_name, last_name, age, country, postal_code, pet_type, pet_name, pet_breed) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (customer_email) DO NOTHING")) {
            statement.setString(1, record.getCustomerEmail());
            statement.setString(2, record.getCustomerFirstName());
            statement.setString(3, record.getCustomerLastName());
            statement.setInt(4, record.getCustomerAge() != null ? record.getCustomerAge() : 0);
            statement.setString(5, record.getCustomerCountry());
            statement.setString(6, record.getCustomerPostalCode());
            statement.setString(7, record.getCustomerPetType());
            statement.setString(8, record.getCustomerPetName());
            statement.setString(9, record.getCustomerPetBreed());
            statement.executeUpdate();
        }
        return lookupId("SELECT customer_id FROM analytics.dim_customer WHERE customer_email = ?", record.getCustomerEmail());
    }

    private int upsertSeller(SaleRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.dim_seller (seller_email, first_name, last_name, country, postal_code) "
                        + "VALUES (?, ?, ?, ?, ?) "
                        + "ON CONFLICT (seller_email) DO NOTHING")) {
            statement.setString(1, record.getSellerEmail());
            statement.setString(2, record.getSellerFirstName());
            statement.setString(3, record.getSellerLastName());
            statement.setString(4, record.getSellerCountry());
            statement.setString(5, record.getSellerPostalCode());
            statement.executeUpdate();
        }
        return lookupId("SELECT seller_id FROM analytics.dim_seller WHERE seller_email = ?", record.getSellerEmail());
    }

    private int upsertProduct(SaleRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.dim_product (product_name, product_brand, product_price, product_category, "
                        + "product_weight, product_color, product_size, product_material, product_rating, product_reviews) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (product_name) DO NOTHING")) {
            statement.setString(1, record.getProductName());
            statement.setString(2, record.getProductBrand());
            setBigDecimal(statement, 3, record.getProductPrice());
            statement.setString(4, record.getProductCategory());
            setBigDecimal(statement, 5, record.getProductWeight());
            statement.setString(6, record.getProductColor());
            statement.setString(7, record.getProductSize());
            statement.setString(8, record.getProductMaterial());
            setBigDecimal(statement, 9, record.getProductRating());
            statement.setInt(10, record.getProductReviews() != null ? record.getProductReviews() : 0);
            statement.executeUpdate();
        }
        return lookupId("SELECT product_id FROM analytics.dim_product WHERE product_name = ?", record.getProductName());
    }

    private int upsertStore(SaleRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.dim_store (store_name, store_location, store_city, store_state, store_country, store_phone, store_email) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (store_name) DO NOTHING")) {
            statement.setString(1, record.getStoreName());
            statement.setString(2, record.getStoreLocation());
            statement.setString(3, record.getStoreCity());
            statement.setString(4, record.getStoreState());
            statement.setString(5, record.getStoreCountry());
            statement.setString(6, record.getStorePhone());
            statement.setString(7, record.getStoreEmail());
            statement.executeUpdate();
        }
        return lookupId("SELECT store_id FROM analytics.dim_store WHERE store_name = ?", record.getStoreName());
    }

    private int upsertSupplier(SaleRecord record) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.dim_supplier (supplier_name, supplier_contact, supplier_email, supplier_phone, supplier_address, supplier_city, supplier_country) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (supplier_name) DO NOTHING")) {
            statement.setString(1, record.getSupplierName());
            statement.setString(2, record.getSupplierContact());
            statement.setString(3, record.getSupplierEmail());
            statement.setString(4, record.getSupplierPhone());
            statement.setString(5, record.getSupplierAddress());
            statement.setString(6, record.getSupplierCity());
            statement.setString(7, record.getSupplierCountry());
            statement.executeUpdate();
        }
        return lookupId("SELECT supplier_id FROM analytics.dim_supplier WHERE supplier_name = ?", record.getSupplierName());
    }

    private void insertFact(SaleRecord record, int customerId, int sellerId, int productId, int storeId, int supplierId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO analytics.fact_sales (customer_id, seller_id, product_id, store_id, supplier_id, sale_date, quantity, total_amount) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, customerId);
            statement.setInt(2, sellerId);
            statement.setInt(3, productId);
            statement.setInt(4, storeId);
            statement.setInt(5, supplierId);
            if (record.getSaleDateAsLocalDate() != null) {
                statement.setDate(6, Date.valueOf(record.getSaleDateAsLocalDate()));
            } else {
                statement.setNull(6, java.sql.Types.DATE);
            }
            statement.setInt(7, record.getSaleQuantity() != null ? record.getSaleQuantity() : 0);
            setBigDecimal(statement, 8, record.getSaleTotalPrice());
            statement.executeUpdate();
        }
    }

    private void setBigDecimal(PreparedStatement statement, int index, java.math.BigDecimal value) throws SQLException {
        if (value != null) {
            statement.setBigDecimal(index, value);
        } else {
            statement.setNull(index, java.sql.Types.NUMERIC);
        }
    }

    private int lookupId(String sql, String key) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        throw new SQLException("Dimension row not found for key: " + key);
    }
}
