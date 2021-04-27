package de.helaba.jets.g2.domain.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractJpaRepositoryFactorySetup {

    /*
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("de.helaba.jets.g2ApplicationTest");

    private EntityManager em;

    protected EntityManager getEntityManager() {
        if (em == null) {
            setUp();
        }
        return em;
    }

    private void setUp() {
        em = factory.createEntityManager();
    }

    /**
     * Rollback transaction.
     *
    @AfterEach
    public void tearDown() {
        em.clear();
        em.close();
    }
*/

}
