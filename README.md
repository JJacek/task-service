How to run the task-service
==============================

First we have to build the task-service by using Maven:
```
mvn clean package
```
Then to start the task-service with all other necessary services execute a command:
```
docker-compose --project-directory ./setup up
```
It will start the task-service and a postgres database.

We can configure how long can be delay between task processing iteration by setting in application.yml:
```
task:
  processor:
    delay-iteration: 3000 #  in millis
```

How to use the API
==============================
To interact with task-service's API we can use Swagger UI:
```
http://localhost:8888/swagger-ui/index.html
```

There are three endpoints related with tasks:
```
- POST /api/v1/task         ---> Creates a task and returns it's id
- GET  /api/v1/task/{id}    ---> Returns a task with the given id
- POST /api/v1/task/search  ---> Returns page of tasks
```



