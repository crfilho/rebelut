package service

import data.IAccountRepository
import spock.lang.Unroll
import spock.lang.Specification

public class AcountServiceTest extends Specification {

    def accountRepoMock = Mock(IAccountRepository);
    def accountService = new AccountDataService(accountRepoMock)

    @Unroll
    def "should create account and persist it"() {
        when:
        def account = accountService.create()

        then:
        1 * accountRepoMock.create(_)
    }

    @Unroll
    def "should create multiple accounts with unique id"() {
        when:
        def acc1 = accountService.create()
        def acc2 = accountService.create()
        def acc3 = accountService.create()

        then:
        3 * accountRepoMock.create(_)
        acc1.id != acc2.id != acc3.id
    }

    @Unroll
    def "should create account with no balance"() {
        when:
        def acc = accountService.create()

        then:
        acc.balance == 0
    }

    @Unroll
    def "should update the balance when credit into a given account"() {

        given:
        def acc = accountService.create()

        when:
        accountService.credit(15.01, acc)

        then:
        1 * accountRepoMock.update(acc)
    }

    @Unroll
    def "should update the balance when debit into a given account"() {

        given:
        def acc = accountService.create()

        when:
        accountService.debit(1000.00d, acc)

        then:
        1 * accountRepoMock.update(acc)
    }

    @Unroll
    def "should delete a given account from repo"() {

        when:
        accountService.delete(1234)

        then:
        1 * accountRepoMock.delete(1234)
    }

    @Unroll
    def "should retrieve all available accounts"() {

        when:
        accountService.getAll()

        then:
        1 * accountRepoMock.getAll()
    }

}
