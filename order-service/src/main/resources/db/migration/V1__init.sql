CREATE TABLE `orders`
(
    `id` bigint(20) AUTO_INCREMENT PRIMARY KEY ,
    `order_number` varchar(255) DEFAULT NULL,
    `sku_code` varchar(255),
    `price` decimal(19,2),
    `quantity` int

);
