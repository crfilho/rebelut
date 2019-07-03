package validator

import model.Account
import data.ITransactionRepository
import model.Transaction
import model.TransactionType
import spock.lang.Unroll
import spock.lang.Specification

public class BasicTransactionValidatorTest extends Specification {

    def transactionValidator = new BasicTransactionValidator()

    @Unroll
    def "should validate single deposit request"() {

        given:
        def transaction = new Transaction(TransactionType.DEPOSIT, 100d, new Account());

        when:
        transactionValidator.validate(transaction);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should reject deposit because account is invalid"() {

        given:
        def transaction = new Transaction(TransactionType.DEPOSIT, 100d, null);

        when:
        transactionValidator.validate(transaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_ACCOUNT
    }

    @Unroll
    def "should validate single withdraw request with enough balance"() {

        given:
        Account account = new Account();
        account.sum(100d);
        def transaction = new Transaction(TransactionType.WITHDRAW, -20d, account);

        when:
        transactionValidator.validate(transaction);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should reject withdraw because account is invalid"() {

        given:
        def transaction = new Transaction(TransactionType.WITHDRAW, -20d, null);

        when:
        transactionValidator.validate(transaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_ACCOUNT
    }

    @Unroll
    def "should validate withdraw because of insufficient funds"() {
        given:
        Account account = new Account();
        def transaction = new Transaction(TransactionType.WITHDRAW, -20d, account);

        when:
        transactionValidator.validate(transaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INSUFFICIENT_FUNDS
    }

    @Unroll
    def "should validate single transfer request between accounts with valid balance"() {

        given:
        def account = new Account()
        account.sum(210d);
        def origTransaction = new Transaction(TransactionType.TRANSFER_OUT, -200d, account);
        def destTransaction = new Transaction(TransactionType.TRANSFER_IN, 200d, new Account());

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        noExceptionThrown()
    }

    @Unroll
    def "should reject single transfer because invalid destination account"() {

        given:
        def account = new Account()
        account.sum(210d);
        def origTransaction = new Transaction(TransactionType.TRANSFER_OUT, -200d, account);
        def destTransaction = new Transaction(TransactionType.TRANSFER_IN, 200d, null);

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_DEST_ACCOUNT
    }

    @Unroll
    def "should reject single transfer because invalid origin account"() {

        given:
        def origTransaction = new Transaction(TransactionType.TRANSFER_OUT, -200d, null);
        def destTransaction = new Transaction(TransactionType.TRANSFER_IN, 200d, new Account());

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_ORIG_ACCOUNT
    }

    @Unroll
    def "should reject single transfer because invalid amount"() {

        given:
        def origTransaction = new Transaction(TransactionType.WITHDRAW, 0, new Account());
        def destTransaction = new Transaction(TransactionType.WITHDRAW, 0, new Account());

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_AMOUNT
    }

    @Unroll
    def "should reject single transfer because insufficient funds"() {

        given:
        def origTransaction = new Transaction(TransactionType.TRANSFER_OUT, -100, new Account());
        def destTransaction = new Transaction(TransactionType.TRANSFER_IN, 100, new Account());

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INSUFFICIENT_FUNDS
    }

    @Unroll
    def "should reject single transfer because destination == origin"() {

        given:
        def account = new Account()
        account.sum(110d);
        def origTransaction = new Transaction(TransactionType.TRANSFER_OUT, -100, account);
        def destTransaction = new Transaction(TransactionType.TRANSFER_IN, 100, account);

        when:
        transactionValidator.validateTransfer(origTransaction, destTransaction);

        then:
        def e = thrown(InvalidTransactionException)
        e.message == TransactionErrorMessage.INVALID_TRANSACTION
    }
}