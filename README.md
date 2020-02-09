# SpringBoot-AWSDemo
Sample SpringBoot project using AWS SDK

Project includes sample code for following features: -
  - Spring MVC (REST endpoints for POST, GET, DELETE and search, custom REST exception handler etc.). Following endpoints are exposed: 
    - HTTP POST /dog/ : - to fetch random object from public API and store the object in DynamoDB and image in S3.
    - HTTP GET /dog/id : - fetches object in DynamoDB with passed key
    - HTTP DELETE /dog/{id} : - to delete the database record and S3 image, whose ID matches the path parameter.
    - HTTP GET /dog?breedName={breedNamelike} : - to fetch all the resources that contains the query parameter. 
    - HTTP GET /breedNames : - to fetch list of all distinct breed names in the database. 
  - Spring Data - JPA (using Spring Data DynamoDb dependency).
  - Rest client using RestTemplate for public API call.
  - Amazon DynamoDB - to store public API response as key-value pair. Table is defined as per DogDynamoDB entity class. 
  - AWS S3 - to store image fetched using URL from public API.
  - AWS SQS - standard queue implementation for async processing of public API responses. Following endpoints are exposed: -
    - HTTP POST /sqs/queueWithListener : - to store API response in SQS queue. Spring frameworks messaging listener is associated to this queue for immediate processing of messages in queue.
    - HTTP POST /sqs/queueWOListener : - to API response in SQS queue. It will only add messages to SQS.
    - HTTP POST /sqs/retrieve : - to retrieve messages from SQS queue. It will then insert data into DynamoDB and S3. 
