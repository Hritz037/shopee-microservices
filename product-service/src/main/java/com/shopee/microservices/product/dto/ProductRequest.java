package com.shopee.microservices.product.dto;

public record ProductRequest(String id, String name, String description,String skuCode, double price) {

}
