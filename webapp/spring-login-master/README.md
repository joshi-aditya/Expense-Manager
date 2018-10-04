
## User Creation by Cloud Native Web Application

### A simple cloud native web application where you can create account.

Below are the functionalities which code follows :-

As a user, 
> You will be able to make GET requests to /. 
> The API will respond with current time if you provide the basic authentication headers. 
> System will inform for already created account if you are trying to create account with alread existing email.
> Password security using BCrypt password hashing scheme with salt.
> Input to API request/response should be in JSON.

Installation:
1. Install mysql server
2. Install maven 
    - mvn clean 
    - mvn clean install
3. Install Postman

Below are the steps to start the application:

1. Download the project folder (copy the same to local file) and start the mysql server
    ```systemctl start mysqld.service```
2. Create cloud schema
    ```create database cloud```
3. Specify the username and password for mysql root user in application.properties file 
3. Run spring boot project in command line
    ```java -jar target/login-0.0.1-SNAPSHOT.jar```
    >  This will create user table under cloud databse along with loading of the unit test cases for the project)

Open Postman and run
> For creation of new user, call http://localhost:8080/user/register with POST and specify JSON data in Request body
> For getting the current time, call http://localhost:8080/time with GET and specify authorization as 'Basic Auth' and input username, password 

### Transactions workflow
- A user can create, update, view and delete the transactions using the Postman app. Here's the transaction schema
```
{
    id*	string($uuid)
    example: d290f1ee-6c54-4b01-90e6-d701748f0851
    description*	string
    example: coffee
    merchant*	string
    example: starbucks
    amount*	string
    example: 2.69
    date*	string
    example: 09/25/2018
    category*	string
    example: food
}
```
#### Authentication Rules for Transactions
- All transaction requests must contain the correct user credentials and must satisfy basic auth criteria
- Users can only edit, delete and fetch transactions created by themselves. Failing to provide correct credentials will return a unauthorized status response

#### Sample Requests for Transactions
- Transactions can be created by using ```http://localhost:8080/transaction``` API with POST action. Here's the sample request body:
```
{
  "description": "coffee",
  "merchant": "starbucks",
  "amount": 2.69,
  "date": "09/25/2018",
  "category": "food"
}
```

- Transactions created by user can be viewed by using ```http://localhost:8080/transaction``` API with GET action. 

- Transactions can be updated by using ```http://localhost:8080/transaction/{id}``` API with PUT action. The "id" in path parameter is the transaction ID of transaction to be updated. Here's the sample request body:
```
{
  "description": "Sandwich",
  "merchant": "starbucks",
  "amount": 2.99,
  "date": "09/25/2018",
  "category": "food"
}
```

- Transactions can be deleted by using ```http://localhost:8080/transaction/{id}``` API with DELETE action. The "id" in path parameter is the transaction ID of transaction to be deleted
