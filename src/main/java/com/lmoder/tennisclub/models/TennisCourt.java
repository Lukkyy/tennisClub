package com.lmoder.tennisclub.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Entity
@Table(name = "TENNIS_COURT")
@SQLDelete(sql = "UPDATE TENNIS_COURT SET deleted = true WHERE courtId=?")
@Where(clause = "deleted=false")
public class TennisCourt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courtId;

    @Column(name = "surface_type")
    @Enumerated(EnumType.STRING)
    private SurfaceType surfaceType;

    private boolean deleted = Boolean.FALSE;

    // Constructors, getters, and setters

    public TennisCourt() {
    }

    public TennisCourt(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    public TennisCourt(Long courtId, SurfaceType surfaceType) {
        this.courtId = courtId;
        this.surfaceType = surfaceType;
    }

    // Getters and setters

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}

