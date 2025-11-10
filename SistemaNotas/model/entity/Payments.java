package com.portfolio.SistemaNotas.model.entity;

import com.portfolio.SistemaNotas.model.enums.PaymentMethod;
import com.portfolio.SistemaNotas.model.enums.PaymentType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_payments")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_notes", nullable = false)
    private Notes note;

    @Column(name = "amountPaid")
    private BigDecimal amountPaid;

    @Column(name = "remainingAmount")
    private BigDecimal remainingAmount;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "seller")
    private String seller;

    public Payments() {
    }

    public Payments(BigDecimal amountPaid, BigDecimal remainingAmount, LocalDateTime paymentDate, PaymentType type, PaymentMethod method, String seller) {
        this.amountPaid = amountPaid;
        this.remainingAmount = remainingAmount;
        this.paymentDate = paymentDate;
        this.type = type;
        this.method = method;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Notes getNote() {
        return note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", note=" + note +
                ", amountPaid=" + amountPaid +
                ", remainingAmount=" + remainingAmount +
                ", paymentDate=" + paymentDate +
                ", type=" + type +
                ", method=" + method +
                ", seller='" + seller + '\'' +
                '}';
    }
}
