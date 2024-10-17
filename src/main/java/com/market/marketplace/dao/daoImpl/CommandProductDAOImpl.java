package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.entities.CommandProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class CommandProductDAOImpl implements CommandProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(CommandProductDAOImpl.class);
    private EntityManager entityManager;

    public CommandProductDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addCommandProduct(CommandProduct commandProduct) {

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(commandProduct);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<CommandProduct> findCurrentCartForClient(int clientId) {

            String jpql = "SELECT cp FROM CommandProduct cp WHERE cp.command.client.id = :clientId AND cp.isValid = false";
            TypedQuery<CommandProduct> query = entityManager.createQuery(jpql, CommandProduct.class);
            query.setParameter("clientId", clientId);
            return query.getResultList();

    }

    @Override
    public void deleteByCommandId(int commandId) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("DELETE FROM CommandProduct cp WHERE cp.command.id = :commandId");
        query.setParameter("commandId", commandId);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}
