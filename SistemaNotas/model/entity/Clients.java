package com.portfolio.SistemaNotas.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Clients {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_clients")
    private Long id;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "cpf_client")
    private String cpf;

    @Column(name = "address")
    private String address;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @OneToMany(mappedBy = "clients",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Notes> notes;

    public Clients() {
    }

    public Clients(String fullName, String cpf, String address, String phoneNumber) {
        this.fullName = fullName;
        this.cpf = cpf;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", cpf='" + cpf + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public void add(Notes tempNotes){
        if(notes == null){
            notes = new ArrayList<>();
        }
        notes.add(tempNotes);

        tempNotes.setClient(this);
    }
}
