package com.punkyideas.cakemgr.dao;

import com.punkyideas.cakemgr.model.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CakeEntityDAO extends JpaRepository<CakeEntity, Long> {
    List<CakeEntity> findAll();

    CakeEntity findById(int id);

    CakeEntity findByTitle(String title);

    void deleteById(int id);
}
