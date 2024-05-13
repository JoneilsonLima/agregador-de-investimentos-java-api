package tech.buildrun.agregadorinvestimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_accounts")
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @OneToOne(mappedBy = "account")
    @PrimaryKeyJoinColumn
    private BillingAddress billingAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

}
