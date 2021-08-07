package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_balance")
@Getter
@Setter
public class UserBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_user_balance_group"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", foreignKey = @ForeignKey(name = "fk_user_balance_app_user1"))
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owes_to_app_user_id", foreignKey = @ForeignKey(name = "fk_user_balance_app_user2"))
    private AppUser owesTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @Column(name = "debt", nullable = false)
    private BigDecimal debt;
}
