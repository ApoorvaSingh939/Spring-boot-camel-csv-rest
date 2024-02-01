# Assignment2 - spring-camel-csv-rest

## Instructions

1. Create a database "bank"

2. run the application using
   
       mvn spring-boot:run

### CSV File Input 

1. Put a csv file in data/input folder.
2. Get response in data/output/.failed or data/output/.done folder according to results.


### Rest api
1. getAllCustomers
curl --location 'http://localhost:8081/api/customers/getAll'

2. deleteById
curl --location --request DELETE 'http://localhost:8081/api/customers/deleteById/{id}'

1. deleteByFilename
curl --location --request DELETE 'http://localhost:8081/api/customers/deleteByFilename/{fileName}'

## Unit Test

Jacoco Report in jacocoReport folder.


