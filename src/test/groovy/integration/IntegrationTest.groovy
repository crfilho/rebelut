package integration

import controller.AccountController
import controller.TransactionController
import data.InMemoryRepository
import service.AccountDataService
import service.IAccountDataService
import service.ITransactionDataService
import service.TransactionDataService
import spock.lang.Specification
import spock.lang.Unroll
import validator.TransactionValidator
import validator.exceptions.ErrorMessages
import validator.exceptions.InvalidAccountException
import validator.exceptions.InvalidTransactionException

public class IntegrationTest extends Specification {

    def accountService = new AccountDataService(InMemoryRepository.instance());
    def transactionService = new TransactionDataService(new TransactionValidator(), InMemoryRepository.instance(), accountService);

    @Unroll
    def "should update balance upon multiple transactions"() {
        given:
        def account1 = accountService.create()
        def account2 = accountService.create()

        when:
        transactionService.deposit(100d, account1.id)
        transactionService.withdraw(50d, account1.id)
        transactionService.transfer(50d, account1.id, account2.id)
        transactionService.withdraw(50d, account2.id)
        transactionService.deposit(300d, account2.id)
        transactionService.withdraw(100d, account2.id)
        transactionService.transfer(100d, account2.id, account1.id)

        then:
        100d == accountService.get(account1.id).balance
        100d == accountService.get(account2.id).balance
    }

    @Unroll
    def "should ensure balance computation is precise"() {
        given:
        def account1 = accountService.create()
        def account2 = accountService.create()

        when:
        400.times { transactionService.deposit(1d, account1.id) }
        200.times { transactionService.withdraw(1d, account1.id) }
        200.times { transactionService.transfer(1d, account1.id, account2.id) }
        200.times { transactionService.withdraw(1d, account2.id) }

        then:
        0 == accountService.get(account1.id).balance
        0 == accountService.get(account2.id).balance

        when:
        transactionService.withdraw(1d, account1.id)

        then:
        def e = thrown(InvalidTransactionException)
        e.message == ErrorMessages.INSUFFICIENT_FUNDS
    }

    @Unroll
    def "should be thread-safe"() {
        def threads = []
        def initialbalance = 1000d

        when:
        def account1 = accountService.create()
        transactionService.deposit(initialbalance, account1.id)

        def account2 = accountService.create()
        transactionService.deposit(initialbalance, account2.id)

        200.times {
            threads << new Thread( {
                transactionService.transfer(10d, account1.id, account2.id)
            })
            threads << new Thread( {
                transactionService.transfer(10d, account2.id, account1.id)
            })
        }
        threads.each { it.start() }
        threads.each { it.join() }

        then:
        initialbalance == accountService.get(account1.id).balance
        initialbalance == accountService.get(account2.id).balance
    }
}
