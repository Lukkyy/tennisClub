package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.TennisCourt;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TennisCourtDAO extends HibernateDAO<TennisCourt>{

    public TennisCourtDAO(){
        setClazz(TennisCourt.class);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
    }
}
