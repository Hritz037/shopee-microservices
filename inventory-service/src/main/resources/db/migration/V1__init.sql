CREATE TABLE `inventory`
(
    `id` bigint(20) AUTO_INCREMENT PRIMARY KEY ,
    `sku_code` varchar(255) DEFAULT NULL,
    `quantity` int DEFAULT NULL
);