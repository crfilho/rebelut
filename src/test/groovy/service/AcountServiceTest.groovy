package service

import spock.lang.Unroll
import spock.lang.Specification

class AcountServiceTest extends Specification {

    def accountService = new AccountDataService()

    @Unroll
    def "should create account and cache it"() {
        when:
        def acc = accountService.create()

        then:
        accountService.get(acc.id) == acc
    }

    @Unroll
    def "should create multiple ccounts with unique id"() {
        when:
        def acc1 = accountService.create()
        def acc2 = accountService.create()
        def acc3 = accountService.create()

        then:
        acc1.id != acc2.id != acc3.id
    }

    @Unroll
    def "should create account with no balance"() {
        when:
        def acc = accountService.create()

        then:
        accountService.get(acc.id).balance == 0
    }

    @Unroll
    def "should update the balance gor a given account"() {

        when:
        def acc = accountService.create()
        acc.sum(15.01)
        acc.subtract(0.01)
        accountService.update(acc)

        then:
        15.00 == accountService.get(acc.id).balance
    }

    @Unroll
    def "should retrieve all available accounts"() {

        when:
        accountService.create()
        accountService.create()
        accountService.create()

        then:
        3 == accountService.getAll().size();
    }

}
