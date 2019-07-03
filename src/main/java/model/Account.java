package model;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static util.IdGen.genAccID;

public class Account {

    private Long id;
    private AtomicReference<BigDecimal> balance;

    public Account() {

        this.id = genAccID();
        this.balance = new AtomicReference<>();
        this.balance.set(BigDecimal.ZERO);
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance.get();
    }

    public void sum(double amount) {

        while(true) {
            BigDecimal currentBalance = balance.get();
            if (balance.compareAndSet(currentBalance, currentBalance.add(BigDecimal.valueOf(amount))))
                return;
        }
    }

    public void subtract(double amount) {

        while(true) {
            BigDecimal currentBalance = balance.get();
            if (balance.compareAndSet(currentBalance, currentBalance.subtract(BigDecimal.valueOf(amount))))
                return;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Account))
            return false;

        Account that = (Account) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return 31 * 17 + id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
