package com.punkyideas.cakemgr.dao;

import com.punkyideas.cakemgr.model.CakeEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service("CakeService")
public class CakeServiceImpl implements CakeService {
    @PersistenceContext
    private EntityManager em;

    private CakeEntityDAO cakeEntityDAO;

    public CakeServiceImpl(CakeEntityDAO cakeEntityDAO) {
        this.cakeEntityDAO = cakeEntityDAO;
    }

    public List<CakeEntity> findAll() {
        List<CakeEntity> cakes = cakeEntityDAO.findAll();
        if(cakes == null)
            return new ArrayList<>();
        return cakes;
    }

    public CakeEntity findByTitle(String title) {
        return cakeEntityDAO.findByTitle(title);
    }

    public boolean isCakePresentByTitle(String title) {
        if(findByTitle(title) == null)
            return false;
        return true;
    }


    public CakeEntity findById(int id) {
        return cakeEntityDAO.findById(id);
    }

    public CakeEntity save(CakeEntity cake) {
        return cakeEntityDAO.save(cake);
    }

    public void delete(int id) {
        cakeEntityDAO.deleteById(id);
    }
}
