
## Rebelut 1.0

**Build**
gradle buildit

**Test**
gradle test

**Run**
java -jar build\\libs\\rebelut-1.0.jar

**API endpoint**
http://localhost:8181/api/v1/

*Create new account*
curl -X POST http://localhost:8181/api/v1//accounts

*Get account*
curl http://localhost:8181/api/v1/accounts/:id

*Get statements*
curl http://localhost:8181/api/v1/accounts/:id/statements

 *Get all accounts*
curl http://localhost:8181/api/v1/accounts/

*Delete account*
curl -X DELETE http://localhost:8181/api/v1/accounts/:id

*Deposit*
curl -X POST http://localhost:8181/api/v1/accounts/:id/deposit/:amount

*Withdraw*
curl -X POST http://localhost:8181/api/v1/accounts/:id/:withdraw/:amount

*Deposit*
**POST** /accounts/:id/deposit/:amount
curl -X POST http://localhost:8181/api/v1/accounts/:id/deposit/:amount

*Transfer*
curl -X POST http://localhost:8181/api/v1/transactions/send -d "sum=:amount&from=:orig&to=:dest"