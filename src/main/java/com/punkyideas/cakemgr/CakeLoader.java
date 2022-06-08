package com.punkyideas.cakemgr;

import com.google.gson.Gson;
import com.punkyideas.cakemgr.dao.CakeService;
import com.punkyideas.cakemgr.model.CakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

@Component
public class CakeLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CakeLoader.class);

    private CakeService cakeService;

    public CakeLoader(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @PostConstruct
    public void init() {
        Gson gson = new Gson();
        try {
            LOG.info("Loading cakes from GitHub Gist.");
            URL url = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json");
            InputStream input = url.openStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            CakeEntity[] cakes = gson.fromJson(reader, CakeEntity[].class);
            for(CakeEntity cake : cakes) {
                if(cakeService.findByTitle(cake.getTitle()) == null)
                    cakeService.save(cake);
                else
                    LOG.warn("Skipping cake with duplicate title " + cake.getTitle());
            }
        } catch(IOException e) {
            LOG.error("Error trying to load cakes from GitHub Gist");
        }
    }
}
