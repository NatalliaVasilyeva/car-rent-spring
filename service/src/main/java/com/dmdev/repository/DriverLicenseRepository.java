package com.dmdev.repository;

import com.dmdev.domain.dto.DriverLicenseDto;
import com.dmdev.domain.dto.DriverLicenseFilter;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.DriverLicense_;
import com.dmdev.domain.entity.UserContact_;
import com.dmdev.domain.entity.UserDetails_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QDriverLicense.driverLicense;
import static com.dmdev.domain.entity.QUserDetails.userDetails;

public class DriverLicenseRepository extends BaseRepository<Long, DriverLicense> {

    public DriverLicenseRepository(EntityManager entityManager) {
        super(DriverLicense.class, entityManager);
    }

    public List<DriverLicense> findAllHql() {
        return getEntityManager().createQuery("select d from DriverLicense d", DriverLicense.class)
                .getResultList();
    }

    public List<DriverLicense> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<DriverLicense> findAllQueryDsl() {
        return new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .fetch();
    }

    public Optional<DriverLicense> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.equal(driverLicense.get(DriverLicense_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<DriverLicense> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .where(driverLicense.id.eq(id))
                .fetchOne());
    }

    public Optional<DriverLicense> findDriverLicenseByNumberCriteria(String driverLicenseNumber) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.equal(driverLicense.get(DriverLicense_.number), driverLicenseNumber));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public List<DriverLicense> findDriverLicenseByExpiredDateOrLessCriteria(DriverLicenseFilter driverLicenseFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.lessThanOrEqualTo(driverLicense.get(DriverLicense_.expiredDate), driverLicenseFilter.getExpiredDate()));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<DriverLicense> findDriverLicensesByIssueAndExpiredDateCriteria(DriverLicenseFilter driverLicenseFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(driverLicenseFilter.getIssueDate(), issueDate -> cb.greaterThanOrEqualTo(driverLicense.get(DriverLicense_.issueDate), issueDate))
                .add(driverLicenseFilter.getExpiredDate(), expiredDate -> cb.lessThanOrEqualTo(driverLicense.get(DriverLicense_.expiredDate), expiredDate))
                .getPredicates();

        criteria.select(driverLicense)
                .where(predicates);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<DriverLicense> findDriverLicensesByIssueAndExpiredDateQueryDsl(DriverLicenseFilter driverLicenseFilter) {
        var predicateIssueDte = QPredicate.builder()
                .add(driverLicenseFilter.getIssueDate(), driverLicense.issueDate::goe)
                .buildOr();

        var predicateExpiredDate = QPredicate.builder()
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::loe)
                .buildOr();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateIssueDte)
                .addPredicate(predicateExpiredDate)
                .buildAnd();

        return new JPAQuery<DriverLicense>(getEntityManager())
                .select(driverLicense)
                .from(driverLicense)
                .where(predicateAll)
                .fetch();
    }

    public List<DriverLicenseDto> findDriverLicensesByExpiredDateOrderBySurnameCriteria(DriverLicenseFilter driverLicenseFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicenseDto.class);
        var driverLicense = criteria.from(DriverLicense.class);
        var userDetails = driverLicense.join(DriverLicense_.userDetails);
        Predicate predicate = cb.lessThan(driverLicense.get(DriverLicense_.expiredDate), driverLicenseFilter.getExpiredDate());

        criteria.select(
                        cb.construct(DriverLicenseDto.class,
                                userDetails.get(UserDetails_.name),
                                userDetails.get(UserDetails_.surname),
                                userDetails.get(UserDetails_.userContact).get(UserContact_.phone),
                                driverLicense.get(DriverLicense_.number),
                                driverLicense.get(DriverLicense_.issueDate),
                                driverLicense.get(DriverLicense_.expiredDate))

                )
                .where(predicate)
                .orderBy(cb.asc(userDetails.get(UserDetails_.surname)));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Tuple> findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(DriverLicenseFilter driverLicenseFilter) {
        var predicate = QPredicate.builder()
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::loe)
                .buildOr();

        return new JPAQuery<Tuple>(getEntityManager())
                .select(userDetails.name, userDetails.surname,
                        userDetails.userContact.phone,
                        driverLicense.number, driverLicense.issueDate,
                        driverLicense.expiredDate)
                .from(driverLicense)
                .join(driverLicense.userDetails, userDetails)
                .where(predicate)
                .orderBy(userDetails.surname.asc())
                .fetch();
    }
}