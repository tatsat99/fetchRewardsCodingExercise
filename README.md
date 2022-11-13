# Fetch Rewards Coding Exercise - Backend Software Engineering

A web service that accepts HTTP requests and returns responses based on the application requirements and conditions using Java Spring Boot

## Prerequisites:
##### JavaSDK 11
##### Maven 3.x or higher

## To execute the server
##### Compile the code: https://github.com/tatsat99/fetchRewardsCodingExercise/blob/master/src/main/java/com/fetchrewards/codingexercise/payertransaction/PayertransactionApplication.java

## API's and their responses
### /addPayer
##### -POST request a valid JSON body with { "payer": "payerName", "points": points, "timestamp": "timestamp" }
##### -Response is an HTTP Status of execution at the server or any exception that may occur
![image](https://user-images.githubusercontent.com/52033390/201537798-a60faf0f-c508-4de2-9f81-4d93d92dacaf.png)


### /spendPoints
##### -POST request valid JSON body with { "points": points }
##### -Response is an HTTP Status, if valid a key-value pair of payer name and corresponding deducted points or any exception that may occur
![image](https://user-images.githubusercontent.com/52033390/201537928-5d8a7266-728e-48c6-a07f-6873a049669f.png)


### /payerPoints
##### -GET request for current balance of points for each payer in the system
##### -Response is a key-value pair of payer name and corresponding current point balance
![image](https://user-images.githubusercontent.com/52033390/201537935-af5e5279-aba3-4fa7-b40b-a03f4e522fdd.png)
