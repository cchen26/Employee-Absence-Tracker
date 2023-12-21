package com.github.cchen26.employeeabsencetracker.repository;

import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import java.util.List;

@Repository
public class LeaveManageNativeSqlRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<LeaveDetails> getAllLeavesOnStatus(String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LeaveDetails> cq = cb.createQuery(LeaveDetails.class);
        Root<LeaveDetails> root = cq.from(LeaveDetails.class);

        cq.select(root).where(cb.equal(root.get("status"), status));

        var query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
