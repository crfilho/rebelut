package service

import model.Account
import data.ITransactionRepository
import spock.lang.Unroll
import spock.lang.Specification
import validator.ITransactionValidator

public class TransactionServiceTest extends Specification {

    def txRepositoryMock = Mock(ITransactionRepository)
    def accServiceMock = Mock(IAccountDataService)
    def txValidatorMock = Mock(ITransactionValidator)
    def transactionService = new TransactionDataService(txValidatorMock, txRepositoryMock, accServiceMock)

    @Unroll
    def "should process single deposit request"() {

        given:
        def account = new Account()
        accServiceMock.create() >> account
        accServiceMock.get(_) >> account

        when:
        transactionService.deposit(100.01d, account.id)

        then:
        1 * accServiceMock.credit(100.01,_)
    }

    @Unroll
    def "should process single withdraw request"() {

        given:
        def account = new Account()
        account.sum(100.01d)
        accServiceMock.get(account.id) >> account

        when:
        transactionService.withdraw(20.00d, account.id)

        then:
        1 * accServiceMock.debit(20.00d,_)
    }

    @Unroll
    def "should process single transfer request between accounts"() {
        given:
        def from = new Account()
        def to = new Account()
        accServiceMock.get(from.id) >> from
        accServiceMock.get(to.id) >> to

        when:
        transactionService.deposit(200.00d, from.id)
        transactionService.transfer(50.00d, from.id, to.id)

        then:
        1 * accServiceMock.credit(200.00d,from)
        1 * accServiceMock.debit(50.00d,from)
        1 * accServiceMock.credit(50.00d,to)
    }

    @Unroll
    def "should execute multiple transactions between accounts"() {

        given:
        def acc1 = new Account()
        def acc2 = new Account()
        def acc3 = new Account()
        accServiceMock.get(acc1.id) >> acc1
        accServiceMock.get(acc2.id) >> acc2
        accServiceMock.get(acc3.id) >> acc3

        when:
        transactionService.deposit(100.00d, acc1.id);
        transactionService.transfer(40.00d, acc1.id, acc2.id);
        transactionService.transfer(40.00d, acc1.id, acc3.id);
        transactionService.withdraw(20.00d, acc2.id);
        transactionService.withdraw(20.00d, acc3.id);
        
        then:
        1 * accServiceMock.credit(100.00d,acc1)
        2 * accServiceMock.debit(40.00d,acc1)
        1 * accServiceMock.debit(20.00d,acc2)
        1 * accServiceMock.debit(20.00d,acc3)
    }

    @Unroll
    def "should store transactions into repo"() {

        given:
        def acc1 = new Account()
        def acc2 = new Account()
        def acc3 = new Account()
        accServiceMock.get(acc1.id) >> acc1
        accServiceMock.get(acc2.id) >> acc2
        accServiceMock.get(acc3.id) >> acc3

        when:
        transactionService.deposit(200.00d, acc1.id)
        transactionService.transfer(100.00d, acc1.id, acc2.id)
        transactionService.withdraw(50.00d, acc2.id)

        then:
        4 * txRepositoryMock.store(_)
    }
}