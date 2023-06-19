package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.TennisCourt;

import java.io.Serializable;
import java.util.List;

public interface DAOInterface <T, Id extends Serializable>{

        void save(T entity);

        void update(T entity);

        T findById(Id id);

        void deleteById(Id id);

        List<T> findAll();

        //public void deleteAll();
}
