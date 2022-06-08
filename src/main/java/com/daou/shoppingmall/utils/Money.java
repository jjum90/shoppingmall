package com.daou.shoppingmall.utils;

import java.math.BigDecimal;

/**
 * 돈 계산을 위한 Util Class
 */
public class Money {
    public static final Money ZERO = wons(0L);

    private final BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }
    public static Money wons(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public Money times(int amount) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(amount)));
    }

    public boolean isLessThan(Money other) {return amount.compareTo(other.amount) < 0;}
    public boolean isGreaterThanOrEqual(Money other) {return amount.compareTo(other.amount) >= 0;}
    public boolean isEqual(Money other) {return amount.compareTo(other.amount) == 0;}
}
