package com.punkyideas.cakemgr.dao;

import com.punkyideas.cakemgr.model.CakeEntity;

import java.util.List;

public interface CakeService {
    public List<CakeEntity> findAll();
    public CakeEntity findByTitle(String title);
    public CakeEntity findById(int id);
    public boolean isCakePresentByTitle(String title);
    public CakeEntity save(CakeEntity cake);
    public void delete(int id);
}
