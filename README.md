

## rebelut-api 1.0

**Build**<br/>
```
./gradlew build
```
**Test**<br/>
```
./gradlew test
```
**Run**<br/>
```
java -jar build/libs/rebelut-1.0.jar
```
*Create new account*<br/>
```
curl -X POST http://localhost:8181/api/v1/accounts
```
*Get account*<br/>
```
curl http://localhost:8181/api/v1/accounts/:id
```
*Get statements*<br/>
```
curl http://localhost:8181/api/v1/accounts/:id/statements
```
*Get all accounts*<br/>
```
curl http://localhost:8181/api/v1/accounts/
```
*Delete account*<br/>
```
curl -X DELETE http://localhost:8181/api/v1/accounts/:id
```
*Deposit*<br/>
```
curl -X POST http://localhost:8181/api/v1/accounts/:id/deposit/:amount
```
*Withdraw*<br/>
```
curl -X POST http://localhost:8181/api/v1/accounts/:id/withdraw/:amount
```
*Transfer*<br/>
```
curl -X POST http://localhost:8181/api/v1/transactions/send -d "sum=:amount&from=:orig&to=:dest"
```
