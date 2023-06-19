package com.lmoder.tennisclub.models;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Entity
@Table(name = "CUSTOMERS")
@SQLDelete(sql = "UPDATE CUSTOMERS SET deleted = true WHERE customerId=?")
@Where(clause = "deleted=false")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "customer_name")
    private String customerName;
    private boolean deleted = Boolean.FALSE;

    public Customer() {
    }

    public Customer(String phoneNumber, String customerName) {
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
