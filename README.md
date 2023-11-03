<div align="center"> <h1 > <img src=logo.jpg width="300" align="center" alt="50"/></h1></div>

<div align="center"><h1>Bookish Corner </h1></div>



___

#### Welcome to the Bookish Corner project. 📚 
#### This reliable and secure Java Spring Boot program will help you create your own high-performance and successful projects faster and easier. This project combines a wide range of features and technologies to provide a secure e-commerce platform for users. You don't need to install the app to experience the features of my app, because it already runs on AWS. 
#### Below you can familiarize yourself with the key aspects and features of my program.


## Table of contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)

<hr>
<div id="project-overview" align="center">
  <h2 > Project Overview</h2>
</div>
<hr>


[Back to content](#table-of-contents) 

### Technologies Used

>**Spring Boot:**     The project is built using the Spring Boot templates and conventions.
> 
>**Spring Web:** Spring Web is used for HTTP requests handling, managing sessions, and processing web-related tasks.
> 
>**Spring Security:** Authentication and authorization are managing to ensure secure access to endpoints with Spring Security.
> 
>**Spring Data JPA:** Spring Data JPA is used for simplifies database operations and enables easy interaction with the database.
> 
>**Liquibase:** Liquibase is employed for database version control and management.
> 
>**MapStruct:** Mapstruct is used for object mapping between DTOs and entities.
> 
>**Docker:** Docker is used for containerization database and for testing.
> 
>**Swagger UI:** Swagger UI is used to provide interactive API documentation, testing use APIs more easily.

### Entities

You can easily change the name and description of these models, using them as the basis for your own business logic, and also add your own.

- **User**: Represents registered users with authentication details and personal information.
- **Role**: Defines user roles such as admin or regular user.
- **Book**: Represents books available in the store.
- **Category**: Represents books categories.
- **ShoppingCart**: Represents a user's shopping cart.
- **CartItem**: Represents an item in a user's shopping cart.
- **Order**: Represents an order placed by a user.
- **OrderItem**: Represents an item in a user's order.

### Controllers

This project has five controllers waiting for customer requests:

| Сontrollers              |  Endpoint   |                                     Description                                     |
|--------------------------|:-----------:|:-----------------------------------------------------------------------------------:|
| AuthenticationController |    /auth    |                     handles user registration and authorization                     |
| BookController           |   /books    |      manages book operations, such as search, creation, updating and deleting       |
| CategoryController       | /categories |          manages categories, allows to create, update, retrieve and delete          |
| ShoppingCartController   |    /cart    | manages shopping cart state, allows to add, update, retrieve and delete cart items  |
| OrderController          |   /orders   | handles order management, creating, updating, retrieving order history and deleting |


### Features

> The project implements role-based access control: there are two roles: USER (default) and ADMIN.

#### Non Authenticated User Can:

- **Register**: Users can register to access app features.
- **Sign In**: Users can sign in with his own email and password.

#### Logged In User Can:

- **Browse Books**: 
- Get a list of all available books
- Get book by id
- **Browse Book Categories**: 
- Get a list of all available categories
- Get category by id
- Get a list of all available books by category id
- **Use the Basket**: 
- Get users shopping cart with its items
- Create new item in shopping cart
- Change book quantity in item by id
- Delete book from cart by item id
- **Purchase Books**: 
- Retrieve user's order history
- Retrieve all OrderItems for a specific order
- Retrieve a specific OrderItem within an order by item id
- Place an order

#### Admin Can:

- **Manage Books**: Create, change and delete book.
- **Manage Book Categories**: Create, change and delete category.
- **Manage Orders**: Update order status `(COMPLETED, PENDING, DELIVERED)` for user's order 


> - For security, I added JWT Support: JSON Web Tokens are used for authentication and securing API endpoints.
> - To handle exceptions gracefully and provide consistent error responses GlobalExceptionHandler was implemented.
> - Thanks to the soft delete concept implemented in this project, data remains available in the database even after it is deleted by users or administrators. This functionality may be useful for generating application usage statistics in the future.

<hr>
<div id="prerequisites" align="center">
  <h2 > Prerequisites</h2>
</div>
<hr>

[Back to content](#table-of-contents)

#### Ensure that you have installed:
- Java 17 (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Docker (https://www.docker.com/)
- IntelliJ IDEA (https://www.jetbrains.com/idea/)
- You may also need Postman and Git.

<hr>
<div id="installation" align="center">
  <h2 > Installation</h2>
</div>
<hr>


[Back to content](#table-of-contents)

#### To run this application on local machine please follow these steps:
1. Clone this repository: 

    ``git clone https://github.com/irynamekh/bookstore.git``

2. Go to the root project directory and build the project with command:

    ``mvn clean install``

3. Build docker container:

    ``docker build -t your-image-name .``

4. Run docker container:

    ``docker compose up``

<hr>
<div id="usage" align="center">
  <h2 > Usage</h2>
</div>
<hr>

[Back to content](#table-of-contents)

> ### In case the application is installed locally:

 **By link below, you can see and interact with all endpoints:**

`http://localhost:8080/api/swagger-ui/index.html`

**You can also use Postman and test this API using a ready-made collection.** 
**Here are all the requests that can be sent to my application's endpoints and receive valid responses.**

`Book Store.postman_collection.json`
> You can download the collection by following these instructions:
> 1. In Postman, in the upper left corner, find and click the `Import button`
> 2. Select `Files button` and locate the `Book Store.postman_collection.json` file in the root directory of this project.
> 3. Select this file and voilà - you have downloaded the postman.

 **By default, a user with the role of admin is already saved in the database, 
 so you can register as a moderator or log in as an admin using these credentials:**

`"email": "admin@gmail.com"`

`"password": "securePassword123"`

**In this video, you can see an example of using this application using the Postman collection:**

<a href="https://www.loom.com/share/b62d86bc835b401abcdda565df91c842?sid=1955732b-0288-4c61-9341-eac1d4f58536" target="_blank" class="social-icon">
Project presentation in Postman</a>

> ### In case the application is running on AWS:

By link below, you can see and interact with all endpoints:

`http://ec2-54-174-84-140.compute-1.amazonaws.com/api/swagger-ui/index.html#/`

This Postman collection is designed to test the functionality of the application, provided that the application is installed locally. 
If you want to access the app's endpoints without installing it, you can still use this collection, but with a different link. 
In requests, replace `http://localhost:8080/api/` with `http://ec2-54-174-84-140.compute-1.amazonaws.com/api/`.

Data about the default user (admin) are also relevant in this case.

<hr>
<div id="contributing" align="center">
  <h2 > Contributing</h2>
</div>
<hr>

Community contributions to improve the Online Bookstore project are always welcome. Fix a bug, improve an existing feature, or suggest a new feature, feel free to improve this project if you feel like it. your contributions are valuable to us. You can create a pull request for the project and I will definitely consider your changes.
If you wish to contact for additional information and cooperation: 
<a href="https://www.linkedin.com/in/iryna-mekh/" target="_blank" class="social-icon">
LinkedIn profile</a>
