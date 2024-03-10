# Test task - Library API

6 services whose name are shown below have been devised within the scope of this project.

<img src="screenshots/APPLIFE.png" alt="Main Information" width="885" height="433">


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


## Valid Request Body (All requests must contain the Bearer token)

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


### 🔨 Run the App

<b>Local</b>

<b>1 )</b> Download your project from this link `https://github.com/Nikita-ctr/Library-API.git`

<b>2 )</b> Go to the project's home directory :  `cd library-api-gateway`

<b>3 )</b> Run <b>Service Registry (Eureka Server)</b>

<b>4 )</b> Run <b>config server</b>

<b>5 )</b> Run <b>zipkin</b> through these command shown below on <b>Docker</b>
```
    docker run -d -p 9411:9411 openzipkin/zipkin
```

<b>6 )</b> Run <b>Gateway service</b>

<b>7 )</b> Run other services (<b>authentication-service</b>, <b>library-service</b>, <b>book-service</b>)


<b>Docker</b>

<b>1 )</b> Install <b>Docker Desktop</b>. Here is the installation <b>link</b> : https://docs.docker.com/docker-for-windows/install/

<b>2 )</b> Build <b>jar</b> file for all services shown below

<table style="width:100%">
  <tr>
    <th>Service</th>
    <th>Command</th>
  </tr>
  <tr>
    <td>service-registry</td>
    <td>mvn clean install</td>
  </tr>
  <tr>
    <td>configserver</td>
    <td>mvn clean install</td>
  </tr>
  <tr>
    <td>apigateway</td>
    <td>mvn clean install -DskipTests</td>
  </tr>
  <tr>
    <td>auth-service</td>
    <td>mvn clean install -DskipTests</td>
  </tr>
  <tr>
    <td>libraryservice</td>
    <td>mvn clean install -DskipTests</td>
  </tr>
  <tr>
    <td>bookservice</td>
    <td>mvn clean install -DskipTests</td>
  </tr>
</table>

<b>3 )</b> Build all <b>images</b> and push to <b>Docker Hub</b>
```
    1 ) service-registry
     
        - docker build -t library/serviceregistry:0.0.1 .
        - docker tag library/serviceregistry:0.0.1 pomer2002/serviceregistry
        - docker push pomer2002/serviceregistry
        
    2 ) configserver
     
        - docker build -t library/configserver:0.0.1 .
        - docker tag library/configserver:0.0.1 pomer2002/configserver
        - docker push pomer2002/configserver
    
    3 ) api-gateway
     
        - docker build -t library/apigateway:0.0.1 .
        - docker tag library/apigateway:0.0.1 pomer2002/apigateway
        - docker push pomer2002/apigateway
    
    4 ) auth-service
     
        - docker build -t library/authservice:0.0.1 .
        - docker tag library/authservice:0.0.1 pomer2002/authservice
        - docker push pomer2002/authservice
        
    5 ) book-service
     
        - docker build -t library/bookservice:0.0.1 .
        - docker tag library/bookservice:0.0.1 pomer2002/bookservice
        - docker push pomer2002/bookservice
        
    6 ) orderservice
     
        - docker build -t library/libraryservice:0.0.1 .
        - docker tag library/libraryservice:0.0.1 pomer2002/libraryservice
        - docker push pomer2002/libraryservice
        
```

<b>4 )</b> Run all <b>Containers</b> through this command shown below under main folder
```
    docker-compose up -d
```

<b>Kubernetes</b>

<b>1 )</b> Install <b>minikube</b> to access this link https://minikube.sigs.k8s.io/docs/start/

<b>2 )</b> Open <b>command prompt</b> and install <b>kubectl</b> through this command shown below 
```
    minikube kubectl --
```

<b>3 )</b> Start <b>minikube</b> through this command shown below.
```
    minikube start
```

<b>4 )</b> Open <b>minikube dashboard</b> through this command shown below.
```
    minikube dashboard
```

<b>5 )</b> Run all <b>images</b> coming from Docker hub on Kubernetes through this command shown below.
```
    kubectl apply -f k8s
```

<b>6 )</b> Show all information about images running on <b>Kubernetes</b> through this command
```
    kubectl get all
```

<b>7 )</b> Show all <b>services</b> running on Kubernetes through this command
```
    kubectl get services
```

<b>8 )</b> Show <b>eureka server</b> on Kubernetes through this command
```
    minikube service eureka-lb
```

<b>9 )</b> Show <b>api gateway</b> on Kubernetes through this command
```
    minikube service cloud-gateway-svc
```
<b>10 )</b> Copy <b>IP address</b> and Replace <b>it</b> with <b>localhost</b> of the <b>endpoints</b>

