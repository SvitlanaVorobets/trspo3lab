package com.example.lab3.dto;


public class NewPaymentDto {
    private Long id;
    private Long credit_card_id;
    private Double amount;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCredit_card_id() {
        return credit_card_id;
    }

    public void setCredit_card_id(Long credit_card_id) {
        this.credit_card_id = credit_card_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
