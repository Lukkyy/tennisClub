package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.Customer;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO extends HibernateDAO<Customer>{
    public CustomerDAO(){
        setClazz(Customer.class);
    }

}
