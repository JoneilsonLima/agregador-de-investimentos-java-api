package tech.buildrun.agregadorinvestimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.buildrun.agregadorinvestimentos.entity.id.AccountStockId;

@Entity
@Table(name = "tb_accounts_stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStock {

    @EmbeddedId
    private AccountStockId id;

    @ManyToOne
    @MapsId(value = "accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId(value = "stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "quantity")
    private Integer quantity;
}
