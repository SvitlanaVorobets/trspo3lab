package com.example.lab3.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "last_top_up_date")
    private String lastTopUpDate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    @JsonIgnore
    private CreditCard creditCard;

    public Account() {
    }

    public Account(Long id, Double balance, boolean blocked, String lastTopUpDate, CreditCard creditCard) {
        this.id = id;
        this.balance = balance;
        this.blocked = blocked;
        this.lastTopUpDate = lastTopUpDate;
        this.creditCard = creditCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getLastTopUpDate() {
        return lastTopUpDate;
    }

    public void setLastTopUpDate(String lastTopUpDate) {
        this.lastTopUpDate = lastTopUpDate;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", blocked=" + blocked +
                ", lastTopUpDate='" + lastTopUpDate + '\'' +
                ", creditCard=" + creditCard +
                '}';
    }
}
