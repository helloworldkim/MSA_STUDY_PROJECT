package com.example.catalogservice;


import com.example.catalogservice.jpa.CatalogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {

            CatalogEntity catalog1 = new CatalogEntity();
            catalog1.setProductId("CATALOG_001");
            catalog1.setProductName("Berlin");
            catalog1.setStock(100);
            catalog1.setUnitPrice(1500);

            CatalogEntity catalog2 = new CatalogEntity();
            catalog2.setProductId("CATALOG_002");
            catalog2.setProductName("Tokyo");
            catalog2.setStock(110);
            catalog2.setUnitPrice(1000);

            CatalogEntity catalog3 = new CatalogEntity();
            catalog3.setProductId("CATALOG_003");
            catalog3.setProductName("Stockholm");
            catalog3.setStock(120);
            catalog3.setUnitPrice(2000);

            em.persist(catalog1);
            em.persist(catalog2);
            em.persist(catalog3);





        }





    }
}


