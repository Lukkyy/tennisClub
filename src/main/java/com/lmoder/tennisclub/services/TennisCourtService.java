package com.lmoder.tennisclub.services;

import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.repositories.TennisCourtDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TennisCourtService {

    @Autowired
    private TennisCourtDAO courtRepository;

    public TennisCourtService(TennisCourtDAO courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<TennisCourt> list(){
        courtRepository.openCurrentSession();
        List<TennisCourt> courts =  courtRepository.findAll();
        courtRepository.closeCurrentSession();
        return courts;
    }

    public Optional<TennisCourt> get(long id){
        courtRepository.openCurrentSession();
        Optional<TennisCourt> court = Optional.ofNullable(courtRepository.findById(id));
        courtRepository.closeCurrentSession();
        return court;
    }

    public void remove(long id){
        courtRepository.openCurrentSessionWithTransaction();
        courtRepository.deleteById(id);
        courtRepository.closeCurrentSessionwithTransaction();
    }

    public void create(TennisCourt tennisCourt){
        courtRepository.openCurrentSessionWithTransaction();
        courtRepository.save(tennisCourt);
        courtRepository.closeCurrentSessionwithTransaction();

    }

    public void update(TennisCourt tennisCourt){
        courtRepository.openCurrentSessionWithTransaction();
        courtRepository.update(tennisCourt);
        courtRepository.closeCurrentSessionwithTransaction();
    }

}
