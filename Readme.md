# Spring Boot Concurrency
 
### Description
This project deals with running multiple task in Parallel using Java Callable, Future and CompletableFuture.

Here we are also using <Strong>'Ehcache'</Strong> to demonstrate caching and reducing computing again for the same task.

<strong>Run all the tasks :</strong>
- http://localhost:8080/v1/execute/1234

<strong>Get Value form Cache (reducing re-computation) :</strong>
- http://localhost:8080/v1/getFromCache/1234

<strong>Clear Cache :</strong>
- http://localhost:8080/v1/clear

<strong>Sample Response of first 2 API</strong>
```json
{
   "id":1234,
   "serviceOneResponse":"Response From Service 1",
   "serviceTwoResponse":"Response From Service 2",
   "serviceThreeResponse":"Response From Service 3",
   "independentServiceOneResponse":"Response from IDS 1",
   "independentServiceTwoResponse":"Response from Independent Service 2"
}
```