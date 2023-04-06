
# Laptop API - Spring Boot Hibenate JPA




## API Reference

#### Authenticate user

```http
  POST /api/v1/authenticate

```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username`, `password` | `JSON` | **Required**. Authenticate user and return the accesstoken for successful logged user |

#### Delete a product

```http
  DELETE /api/v1/products/{productId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId`      | `Long` | **Required**. Delete a product by provided id |


#### Update product quantity in the shopping cart

```http
  
PUT /api/v1/cart/update/{productId}/{type}

```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId` ,   `type`   | `integer`,  `string` | **Required**. Increase/ decrease  quantity in shopping cart |



#### Update product quantity in the shopping cart

```http
  
PUT /api/v1/cart/update/{productId}/{type}

```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productId` ,   `type`   | `integer`,  `string` | **Required**. Increase/ decrease  quantity in shopping cart |





## Screenshots

![App Screenshot](https://i.ibb.co/Kx7FJkR/2c3b2b55-c65a-4452-af13-53f4136763eb.png)


![App Screenshot](https://i.ibb.co/PFqrfJ9/cb94848b-40f4-49d0-aa98-5459069388ed.png)


Order Endpoint

![App Screenshot](https://i.ibb.co/BLdJrWf/b8e5361b-b1aa-4ed3-b088-27ff113d3f11.png)


Authentication Enpoint
![App Screenshot](https://i.ibb.co/Lk5Wk4C/authen.png)

Send mail order detail

![App Screenshot](https://i.ibb.co/93Y9p6T/order.jpg)


Request Forgot Password 

![App Screenshot](https://i.ibb.co/b6JrrDZ/forgotpass.png)






## Installation

🛠️ Requirements

Intellij IDEA Community Edition
 
Visual Studio Code


Download to your local machine

```bash
  git clone https://github.com/xiexie2904/API_LAPTOP.git
```
    

Front-end - ReactJS : 

https://github.com/xiexie2904/LAPTOP_FE.git

Download FE project to local machine: 
```bash
  npm install
  cd LAPTOP_FE
```

Run this command to install dependencies base on package.json file: 

```bash
  npm install
  cd LAPTOP_FE
```




## Acknowledgements

 - [FreeMarker Templates](https://www.javainuse.com/spring/spring-boot-freemarker-hello-world)

 - [Rest with Spring Tutorail](https://www.baeldung.com/rest-with-spring-series)
 - [Restful API documentation homepage](https://spring.io/guides/tutorials/rest/)

