6 services whose name are shown below have been devised within the scope of this project.

- Authentication Service
- Book Service
- Library Service
- Config Server
- Gateway API
- Dicovery(Registry) Server

### Git Backend for Config server

<a href="https://github.com/Nikita-ctr/springappconfig">Link</a>

### Explore Rest APIs

<table style="width:100%">
  <tr>
      <th>Method</th>
      <th>Url</th>
      <th>Description</th>
      <th>Valid Request Body</th>
  </tr>
    <tr>
      <td>GET</td>
      <td>/books</td>
      <td>Get all books</td>
      <td></td>
  </tr>
    <tr>
      <td>GET</td>
      <td>/books/{book_id}</td>
      <td>Get book by id</td>
      <td></td>
  </tr>
    <tr>
      <td>GET</td>
      <td>/library/loans</td>
      <td>Get all available books</td>
      <td></td>
  </tr>
    <tr>
      <td>GET</td>
      <td>/books/isbn/{book_isbn}</td>
      <td>Get book by isbn</td>
      <td></td>
  </tr>
  <tr>
      <td>POST</td>
      <td>authenticate/signup</td>
      <td>Signup for User</td>
      <td><a href="README.md#signup">Info</a></td>
  </tr>
  <tr>
      <td>POST</td>
      <td>authenticate/login</td>
      <td>Login for User</td>
      <td><a href="README.md#login">Info</a></td>
  </tr>
  <tr>
      <td>POST</td>
      <td>/books</td>
      <td>Add new book</td>
      <td><a href="README.md#createbook">Info</a></td>
  </tr>
  <tr>
      <td>PUT</td>
      <td>/books/{book_id}</td>
      <td>Update book by id</td>
      <td><a href="README.md#updatebook">Info</a></td>
  </tr>
  <tr>
      <td>DELETE</td>
      <td>/books/{book_id}</td>
      <td>Delete book by id</td>
      <td></td>
  </tr>

</table>



### Used Dependencies
* Core
    * Spring
        * Spring Boot
        * Spring Boot Test
        * Spring Security
        * Spring Web
            * RestTemplate
        * Spring Data
            * Spring Data JPA
        * Spring Cloud
            * Spring Cloud Gateway Server
            * Spring Cloud Config Server
            * Spring Cloud Config Client
    * Netflix
        * Eureka Server
        * Eureka Client
* Database
    * Mysql
* Zipkin
* Docker
* Kubernetes
* Log4j2


## Valid Request Body

##### <a id="signup"> Signup for User
```
    http://localhost:9090/authenticate/signup
    
     {
        "username" : "User",
        "password" : "User123",
        "email" : "user.test@gmail.com"
     }
    
```

##### <a id="login"> Login for User
```
    http://localhost:9090/authenticate/login
    
    {
        "username" : "User",
        "password" : "User"
    }
```

##### <a id="createbook"> Add new book
```
    http://localhost:9090/books
    
  {
        "isbn" : "41334sdf123",
        "title" : "BLOOD MONEY",
        "genre" : "thriller",
        "description" : "The author of "Red-Handed" depicts",
        "author" : "Peter Schweizer"
  }
```

##### <a id="updatebook"> Update book info
```
    http://localhost:9090/books/{book_id}
    
  {
        "isbn" : "41334sdf123",
        "title" : "BLOOD MONEY",
        "genre" : "thriller",
        "description" : "The author of "Red-Handed" depicts",
        "author" : "Peter Schweizer"
  }
```
