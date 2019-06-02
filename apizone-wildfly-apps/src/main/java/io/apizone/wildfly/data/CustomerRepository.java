
package io.apizone.wildfly.data;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import io.apizone.wildfly.model.Customer;



@ApplicationScoped
public class CustomerRepository {

    @Inject
    private EntityManager em;

    public Customer findById(Long id) {
        return em.find(Customer.class, id);
    }

    public List<Customer> findAllOrderedByName() {
        // using Hibernate Session and Criteria Query via Hibernate Native API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> customers = query.from(Customer.class);
        query.orderBy(cb.asc(customers.get("name")));
        return em.createQuery(query).getResultList();
    }
}
