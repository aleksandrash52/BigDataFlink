package com.bigdataflink.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SaleRecord implements Serializable {
    private Integer id;
    private String customerFirstName;
    private String customerLastName;
    private Integer customerAge;
    private String customerEmail;
    private String customerCountry;
    private String customerPostalCode;
    private String customerPetType;
    private String customerPetName;
    private String customerPetBreed;
    private String sellerFirstName;
    private String sellerLastName;
    private String sellerEmail;
    private String sellerCountry;
    private String sellerPostalCode;
    private String productName;
    private String productCategory;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private String saleDate;
    private Integer saleCustomerId;
    private Integer saleSellerId;
    private Integer saleProductId;
    private Integer saleQuantity;
    private BigDecimal saleTotalPrice;
    private String storeName;
    private String storeLocation;
    private String storeCity;
    private String storeState;
    private String storeCountry;
    private String storePhone;
    private String storeEmail;
    private String petCategory;
    private BigDecimal productWeight;
    private String productColor;
    private String productSize;
    private String productBrand;
    private String productMaterial;
    private String productDescription;
    private BigDecimal productRating;
    private Integer productReviews;
    private String productReleaseDate;
    private String productExpiryDate;
    private String supplierName;
    private String supplierContact;
    private String supplierEmail;
    private String supplierPhone;
    private String supplierAddress;
    private String supplierCity;
    private String supplierCountry;

    // Конструкторы
    public SaleRecord() {}

    public SaleRecord(Integer id, String customerFirstName, String customerLastName, Integer customerAge,
                     String customerEmail, String customerCountry, String customerPostalCode,
                     String customerPetType, String customerPetName, String customerPetBreed,
                     String sellerFirstName, String sellerLastName, String sellerEmail,
                     String sellerCountry, String sellerPostalCode, String productName,
                     String productCategory, BigDecimal productPrice, Integer productQuantity,
                     String saleDate, Integer saleCustomerId, Integer saleSellerId,
                     Integer saleProductId, Integer saleQuantity, BigDecimal saleTotalPrice,
                     String storeName, String storeLocation, String storeCity, String storeState,
                     String storeCountry, String storePhone, String storeEmail, String petCategory,
                     BigDecimal productWeight, String productColor, String productSize,
                     String productBrand, String productMaterial, String productDescription,
                     BigDecimal productRating, Integer productReviews, String productReleaseDate,
                     String productExpiryDate, String supplierName, String supplierContact,
                     String supplierEmail, String supplierPhone, String supplierAddress,
                     String supplierCity, String supplierCountry) {
        this.id = id;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAge = customerAge;
        this.customerEmail = customerEmail;
        this.customerCountry = customerCountry;
        this.customerPostalCode = customerPostalCode;
        this.customerPetType = customerPetType;
        this.customerPetName = customerPetName;
        this.customerPetBreed = customerPetBreed;
        this.sellerFirstName = sellerFirstName;
        this.sellerLastName = sellerLastName;
        this.sellerEmail = sellerEmail;
        this.sellerCountry = sellerCountry;
        this.sellerPostalCode = sellerPostalCode;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.saleDate = saleDate;
        this.saleCustomerId = saleCustomerId;
        this.saleSellerId = saleSellerId;
        this.saleProductId = saleProductId;
        this.saleQuantity = saleQuantity;
        this.saleTotalPrice = saleTotalPrice;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeCity = storeCity;
        this.storeState = storeState;
        this.storeCountry = storeCountry;
        this.storePhone = storePhone;
        this.storeEmail = storeEmail;
        this.petCategory = petCategory;
        this.productWeight = productWeight;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productMaterial = productMaterial;
        this.productDescription = productDescription;
        this.productRating = productRating;
        this.productReviews = productReviews;
        this.productReleaseDate = productReleaseDate;
        this.productExpiryDate = productExpiryDate;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.supplierEmail = supplierEmail;
        this.supplierPhone = supplierPhone;
        this.supplierAddress = supplierAddress;
        this.supplierCity = supplierCity;
        this.supplierCountry = supplierCountry;
    }

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }

    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    public Integer getCustomerAge() { return customerAge; }
    public void setCustomerAge(Integer customerAge) { this.customerAge = customerAge; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerCountry() { return customerCountry; }
    public void setCustomerCountry(String customerCountry) { this.customerCountry = customerCountry; }

    public String getCustomerPostalCode() { return customerPostalCode; }
    public void setCustomerPostalCode(String customerPostalCode) { this.customerPostalCode = customerPostalCode; }

    public String getCustomerPetType() { return customerPetType; }
    public void setCustomerPetType(String customerPetType) { this.customerPetType = customerPetType; }

    public String getCustomerPetName() { return customerPetName; }
    public void setCustomerPetName(String customerPetName) { this.customerPetName = customerPetName; }

    public String getCustomerPetBreed() { return customerPetBreed; }
    public void setCustomerPetBreed(String customerPetBreed) { this.customerPetBreed = customerPetBreed; }

    public String getSellerFirstName() { return sellerFirstName; }
    public void setSellerFirstName(String sellerFirstName) { this.sellerFirstName = sellerFirstName; }

    public String getSellerLastName() { return sellerLastName; }
    public void setSellerLastName(String sellerLastName) { this.sellerLastName = sellerLastName; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getSellerCountry() { return sellerCountry; }
    public void setSellerCountry(String sellerCountry) { this.sellerCountry = sellerCountry; }

    public String getSellerPostalCode() { return sellerPostalCode; }
    public void setSellerPostalCode(String sellerPostalCode) { this.sellerPostalCode = sellerPostalCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public Integer getProductQuantity() { return productQuantity; }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity = productQuantity; }

    public String getSaleDate() { return saleDate; }
    public void setSaleDate(String saleDate) { this.saleDate = saleDate; }

    public Integer getSaleCustomerId() { return saleCustomerId; }
    public void setSaleCustomerId(Integer saleCustomerId) { this.saleCustomerId = saleCustomerId; }

    public Integer getSaleSellerId() { return saleSellerId; }
    public void setSaleSellerId(Integer saleSellerId) { this.saleSellerId = saleSellerId; }

    public Integer getSaleProductId() { return saleProductId; }
    public void setSaleProductId(Integer saleProductId) { this.saleProductId = saleProductId; }

    public Integer getSaleQuantity() { return saleQuantity; }
    public void setSaleQuantity(Integer saleQuantity) { this.saleQuantity = saleQuantity; }

    public BigDecimal getSaleTotalPrice() { return saleTotalPrice; }
    public void setSaleTotalPrice(BigDecimal saleTotalPrice) { this.saleTotalPrice = saleTotalPrice; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreLocation() { return storeLocation; }
    public void setStoreLocation(String storeLocation) { this.storeLocation = storeLocation; }

    public String getStoreCity() { return storeCity; }
    public void setStoreCity(String storeCity) { this.storeCity = storeCity; }

    public String getStoreState() { return storeState; }
    public void setStoreState(String storeState) { this.storeState = storeState; }

    public String getStoreCountry() { return storeCountry; }
    public void setStoreCountry(String storeCountry) { this.storeCountry = storeCountry; }

    public String getStorePhone() { return storePhone; }
    public void setStorePhone(String storePhone) { this.storePhone = storePhone; }

    public String getStoreEmail() { return storeEmail; }
    public void setStoreEmail(String storeEmail) { this.storeEmail = storeEmail; }

    public String getPetCategory() { return petCategory; }
    public void setPetCategory(String petCategory) { this.petCategory = petCategory; }

    public BigDecimal getProductWeight() { return productWeight; }
    public void setProductWeight(BigDecimal productWeight) { this.productWeight = productWeight; }

    public String getProductColor() { return productColor; }
    public void setProductColor(String productColor) { this.productColor = productColor; }

    public String getProductSize() { return productSize; }
    public void setProductSize(String productSize) { this.productSize = productSize; }

    public String getProductBrand() { return productBrand; }
    public void setProductBrand(String productBrand) { this.productBrand = productBrand; }

    public String getProductMaterial() { return productMaterial; }
    public void setProductMaterial(String productMaterial) { this.productMaterial = productMaterial; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public BigDecimal getProductRating() { return productRating; }
    public void setProductRating(BigDecimal productRating) { this.productRating = productRating; }

    public Integer getProductReviews() { return productReviews; }
    public void setProductReviews(Integer productReviews) { this.productReviews = productReviews; }

    public String getProductReleaseDate() { return productReleaseDate; }
    public void setProductReleaseDate(String productReleaseDate) { this.productReleaseDate = productReleaseDate; }

    public String getProductExpiryDate() { return productExpiryDate; }
    public void setProductExpiryDate(String productExpiryDate) { this.productExpiryDate = productExpiryDate; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getSupplierContact() { return supplierContact; }
    public void setSupplierContact(String supplierContact) { this.supplierContact = supplierContact; }

    public String getSupplierEmail() { return supplierEmail; }
    public void setSupplierEmail(String supplierEmail) { this.supplierEmail = supplierEmail; }

    public String getSupplierPhone() { return supplierPhone; }
    public void setSupplierPhone(String supplierPhone) { this.supplierPhone = supplierPhone; }

    public String getSupplierAddress() { return supplierAddress; }
    public void setSupplierAddress(String supplierAddress) { this.supplierAddress = supplierAddress; }

    public String getSupplierCity() { return supplierCity; }
    public void setSupplierCity(String supplierCity) { this.supplierCity = supplierCity; }

    public String getSupplierCountry() { return supplierCountry; }
    public void setSupplierCountry(String supplierCountry) { this.supplierCountry = supplierCountry; }

    // Вспомогательные методы
    public LocalDate getSaleDateAsLocalDate() {
        if (saleDate == null || saleDate.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            return LocalDate.parse(saleDate, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "SaleRecord{" +
                "id=" + id +
                ", customerEmail='" + customerEmail + '\'' +
                ", productName='" + productName + '\'' +
                ", saleDate='" + saleDate + '\'' +
                ", saleTotalPrice=" + saleTotalPrice +
                '}';
    }
}
