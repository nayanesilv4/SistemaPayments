package com.portfolio.SistemaNotas.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_notes")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "value")
    private double value;

    @Column(name = "seller")
    private String seller;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="cpf_client")
    private Clients clients;

    public Notes() {
    }

    public Notes(String content, double value, String seller) {
        this.content = content;
        this.value = value;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Clients getClient() {
        return clients;
    }

    public void setClient(Clients clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", value=" + value +
                ", seller='" + seller + '\'' +
                '}';
    }

    public Clients getPayments() {
        return null;
    }
}
