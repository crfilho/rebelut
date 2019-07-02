package service

import model.Account
import data.ITransactionRepository
import spock.lang.Unroll
import spock.lang.Specification

public class TransactionServiceTest extends Specification {

    def txRepositoryMock = Mock(ITransactionRepository)
    def accServiceMock = Mock(IAccountDataService)
    def transactionService = new TransactionDataService(txRepositoryMock, accServiceMock)

    @Unroll
    def "should execute single deposit request on #account.id"() {
        when:

        def account = new Account()
        accServiceMock.create() >> account
        accServiceMock.get(_) >> account
        transactionService.deposit(100.01d, account.id)

        then:
        account.balance == 100.01d
    }

    @Unroll
    def "should reject deposit because account is invalid"() {
        when:
        transactionService.deposit(100.01d, 1020L);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid account'
    }

    @Unroll
    def "should execute single withdraw request on #account.id"() {

        when:
        def account = new Account()
        account.balance = 100.01d
        accServiceMock.create() >> account
        accServiceMock.get(_) >> account
        transactionService.withdraw(20.00d, account.id)

        then:
        account.balance == 80.01d;
    }

    @Unroll
    def "should reject withdraw because account is invalid"() {
        when:
        transactionService.withdraw(50.01d, 1030L)

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid account'
    }

    @Unroll
    def "should reject withdraw because of insufficient funds"() {
        when:
        accServiceMock.get(_) >> new Account()
        transactionService.withdraw(50.01d, 1030L)

        then:
        def e = thrown(RuntimeException )
        e.message == 'Insufficient funds'
    }

    @Unroll
    def "should execute single transfer request between accounts"() {
        when:
        def from = new Account()
        def to = new Account()
        accServiceMock.get(from.id) >> from
        accServiceMock.get(to.id) >> to
        transactionService.deposit(200.00d, from.id)
        transactionService.transfer(50.00d, from.id, to.id)

        then:
        from.balance == 150.00
        to.balance == 50.00
    }

    @Unroll
    def "should reject single transfer because invalid destination account"() {
        when:
        def from = new Account()
        accServiceMock.get(from.id) >> from
        transactionService.deposit(200.00d, from.id);
        transactionService.transfer(50.00d, from.id, 1040L);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid transaction'
    }

    @Unroll
    def "should reject single transfer because invalid origin account"() {
        when:
        def to = new Account()
        accServiceMock.get(to.id) >> to
        transactionService.transfer(50.00d, 1050L, to.id);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid transaction'
    }

    @Unroll
    def "should reject single transfer because invalid amount"() {
        when:
        def from = new Account()
        def to = new Account()
        accServiceMock.get(from.id) >> from
        accServiceMock.get(to.id) >> to
        transactionService.transfer(0d, from.id, to.id);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid amount'
    }

    @Unroll
    def "should reject single transfer because insufficient funds"() {
        when:
        def from = new Account()
        def to = new Account()
        accServiceMock.get(from.id) >> from
        accServiceMock.get(to.id) >> to
        transactionService.transfer(250.00d, from.id, to.id);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Insufficient funds'
    }

    @Unroll
    def "should reject single transfer because destination == origin"() {
        when:
        def acc = new Account()
        accServiceMock.get(acc.id) >> acc
        transactionService.transfer(150.00d, acc.id, acc.id);

        then:
        def e = thrown(RuntimeException)
        e.message == 'Invalid transaction'
    }

    @Unroll
    def "should execute single transfer request between accounts"() {
        when:
        def acc1 = new Account()
        def acc2 = new Account()
        def acc3 = new Account()
        accServiceMock.get(acc1.id) >> acc1
        accServiceMock.get(acc2.id) >> acc2
        accServiceMock.get(acc3.id) >> acc3
        transactionService.deposit(100.00d, acc1.id);
        transactionService.transfer(40.00d, acc1.id, acc2.id);
        transactionService.transfer(40.00d, acc1.id, acc3.id);
        transactionService.withdraw(20.00d, acc2.id);
        transactionService.withdraw(20.00d, acc3.id);
        
        then:
        acc1.balance == 20.00d
        acc2.balance == 20.00d
        acc3.balance == 20.00d
    }

    @Unroll
    def "should store transactions into repo"() {
        when:
        def acc1 = new Account()
        def acc2 = new Account()
        def acc3 = new Account()

        accServiceMock.get(acc1.id) >> acc1
        accServiceMock.get(acc2.id) >> acc2
        accServiceMock.get(acc3.id) >> acc3

        transactionService.deposit(200.00d, acc1.id)
        transactionService.transfer(100.00d, acc1.id, acc2.id)
        transactionService.withdraw(50.00d, acc2.id)

        then:
        4 * txRepositoryMock.store(_)
    }
}