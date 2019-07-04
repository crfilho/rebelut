package validator

import model.Account
import model.Transaction
import model.TransactionType
import model.Transfer
import spock.lang.Unroll
import spock.lang.Specification
import validator.exceptions.ErrorMessages
import validator.exceptions.InvalidTransactionException

public class TransactionValidatorTest extends Specification {

    def transactionValidator = new TransactionValidator()

    @Unroll
    def "should validate single deposit request"() {

        given:
        def account = new Account();
        def transaction = new Transaction(TransactionType.DEPOSIT, 100d, account);

        when:
        transactionValidator.validateTransaction(transaction);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should validate single withdraw request with enough balance"() {

        given:
        Account account = new Account();
        account.sum(100d);
        def transaction = new Transaction(TransactionType.WITHDRAW, -20d, account);

        when:
        transactionValidator.validateTransaction(transaction);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should validate withdraw because of insufficient funds"() {
        given:
        Account account = new Account();
        def transaction = new Transaction(TransactionType.WITHDRAW, -20d, account);

        when:
        transactionValidator.validateTransaction(transaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == ErrorMessages.INSUFFICIENT_FUNDS
    }

    @Unroll
    def "should validate single transfer request between accounts with valid balance"() {

        given:
        def account = new Account()
        account.sum(210d);
        def transfer = new Transfer(200, account, new Account())

        when:
        transactionValidator.validateTransfer(transfer);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should reject single transfer because invalid amount"() {

        given:
        def transfer = new Transfer(-100, new Account(), new Account())

        when:
        transactionValidator.validateTransfer(transfer);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == ErrorMessages.INVALID_AMOUNT
    }

    @Unroll
    def "should reject single transfer because insufficient funds"() {

        given:
        def transfer = new Transfer(200, new Account(), new Account())

        when:
        transactionValidator.validateTransfer(transfer);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == ErrorMessages.INSUFFICIENT_FUNDS
    }

    @Unroll
    def "should reject single transfer because destination == origin"() {

        given:
        def account = new Account()
        account.sum(210d);
        def transfer = new Transfer(200, account, account)

        when:
        transactionValidator.validateTransfer(transfer);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == ErrorMessages.INVALID_TRANSACTION
    }
}