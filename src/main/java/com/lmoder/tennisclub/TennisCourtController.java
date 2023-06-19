package com.lmoder.tennisclub;

import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.services.TennisCourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TennisCourtController {
    private final TennisCourtService tennisCourtService;

    @Autowired
    public TennisCourtController(TennisCourtService service){
        this.tennisCourtService = service;
    }

    @GetMapping("api/court")
    public List<TennisCourt> getAllTennisCourts(){
        return tennisCourtService.list();
    }

    @GetMapping("api/court/{courtid}")
    public Optional<TennisCourt> getTennisCourt(@PathVariable("courtid") long courtId){
        return tennisCourtService.get(courtId);
    }

    @DeleteMapping("api/court/{courtid}")
    private void deleteCourt(@PathVariable("courtid") long courtId)
    {
        tennisCourtService.remove(courtId);
    }

    @PostMapping("api/court")
    private long saveCourt(@RequestBody TennisCourt tennisCourt)
    {
        tennisCourtService.create(tennisCourt);
        return tennisCourt.getCourtId();
    }

    @PutMapping("api/court")
    private TennisCourt update(@RequestBody TennisCourt tennisCourt)
    {
        tennisCourtService.update(tennisCourt);
        return tennisCourt;
    }

}
