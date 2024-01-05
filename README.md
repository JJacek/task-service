Task description
==============================
1. Design API which allows to create a task, list all tasks, read the status and the results of the task
2. When the task is created the customer receives the unique id of the task
3. The customer can check the status and the results of the task using the received id

Functional requirements
==============================
- Task accepts two strings as the parameters: the pattern and the input
- Task should find the first best match – position of the pattern in the input with the least number of different characters. For example:
  - Input: ABCD, pattern: BCD -> position:1, typos:0 (‘BCD’ matches exactly substring on position 1) 
  - Input: ABCD, pattern: BWD -> position:1, typos:1 (‘BCD’ matches, ‘W’ is a typo here)
  - Input: ABCDEFG, pattern: CFG -> position:4, typos:1 (‘EFG’ is better match than ‘CDE’)
  - Input: ABCABC, pattern: ABC -> position:0, typos:0 (matches exactly twice, selects first)
  - Input: ABCDEFG, pattern: TDD -> position:1, typos:2 (match first - BCD, not CDE)
- The result of the task is the position of the pattern in the input and the number of the typos 
- While one task is executing the next tasks can also be started and processed, API is not blocked 
- The status of the task should contain the information about the progress (in %)

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



