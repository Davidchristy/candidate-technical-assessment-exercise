This is a demo for the technical assessment at Gaggle. It's meant only as a demo and should not be used in production without further review and permission from the author.

You can find the original specs for the assessment at the bottom of this README.


# How to build:
This is designed to be run with docker but is not necessary. Take your pick on how you want to build/run.

## With Docker:
If you have docker already installed run the following command:
```
./mvnw install dockerfile:build
```
**Note:** *All commands given are for a windows machine, the commands may vary slightly for different operating systems*

This command will build a Docker image: `gaggle_assessment/assessment_1:latest`

Now that the image is created start the docker image with the following command:

```
 docker run -p 8080:8080 -t gaggle_assessment/assessment_1:latest
```

This command will start a docker container running the web service on port 8080

**NOTE:** *Database will be lost if you restart the docker image. I could fix with making a shared volume with host and docker, but this would be outside the scope of the project and fall into implementation. If you want a persistant database use Mavin without Docker.*

## With Maven:
If you don't have docker installed you can run the following command
```
./mvnw install
```
Which will create the file: `target\assessment_1-0.0.1-SNAPSHOT.jar` 
If there are no build errors you can run the Web Service by the following command: 
``` 
.\mvnw spring-boot:run
```

## Run Test
If you just want to run the unit test do so with the following command:
```
./mvnw clean test
```
---
## Example Use

If you want an example of how to use this web service look in the python script `functionalTests.py`. 

It's a quick script I wrote to display the functionality of this demo. If you run it while the web service is running it will add a few names to the database and then search for different ID's and names.

---
# EndPoints
There are 3 endpoints in this version of the Web service.

`POST 'http://localhost:8080/api/contact/add'`

This will add new names to the contact repository. It accepts a JSON in the following format:
```
{"name":"NewContact"}
```
If Successful it returns a code 200 with no body.

If there is an error it returns a code 500 with the appropriate error message, in the following format:
```
{"Error":"ErrorMessage"}
```
---
`POST 'http://localhost:8080/api/contact/search/id'`

This searches for a contact by a unique ID. If the ID is not found, it returns a 404 status. 


It accepts a JSON in the following format:
```
{"id":uniqueId}
```

If the ID is found, it returns a 200 status code with the following JSON format:
```
{"Contact":"ContactName"}
```

If there is an error it returns a code 500 with the appropriate error message, in the following format:
```
{"Error":"ErrorMessage"}
```
---
`POST 'http://localhost:8080/api/contact/search/name'`

This searches for a list of contacts containing the "name" as a substring. If no contact contains that substring an empty list is given.

It accepts a JSON in the following format:
```
{
  "name":"searchQuery",
  "maxNames": int
 }
```
The maxNames variable in the input JSON is optional. Not including it will return all results however if you as an end user only need N results- for example in a auto-suggestion list- you can use this.


If there are no errors, it returns a 200 status code with the following JSON format:
```
{"Contacts":["name1", "name2", "name3",...]}
```


---
Orginal Text below:
---
---
---
# Overview
This exercise is meant to showcase your technical abilities.
* Include documentation about your development environment and instructions on how to run your program.
   * The assignment need only execute from a local machine and there are no requirements to deploy the implementation anywhere else.
* Include tests and instructions on how to run the tests.

# Requirements
Please complete the assignment using the following criteria. It is expected that you wil fork this repository in GitHub and share the link with us when complete.

Assume there is a database of your choice (can be relational or non-relational and the use of an embedded database is acceptable) with records that have the following fields:
* Unique identifier
* Name

Write the code for a Web Service that provides two possible operations for a client to call:
* Get a contact by ID
* Search for contacts by name
  * **_Example_**: If the database contains the full name of "Bruce Wayne", then it should be reasonable for the function to return this result given any of the following search strings: "bru", "Bruce", "Wayne", "Bruce Wayne", etc.
* Both the input and output of the operations should be formatted in JSON.
* Unit tests for the provided code are written
* **Bonus:** Leverage dependency injection
