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
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QDriverLicense.driverLicense;
import static com.dmdev.domain.entity.QUserDetails.userDetails;

public class DriverLicenseRepository implements Repository<Long, DriverLicense> {
    private static final DriverLicenseRepository INSTANCE = new DriverLicenseRepository();

    public static DriverLicenseRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<DriverLicense> findAllHQL(Session session) {
        return session.createQuery("select d from DriverLicense d", DriverLicense.class)
                .list();
    }

    @Override
    public List<DriverLicense> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<DriverLicense> findAllQueryDsl(Session session) {
        return new JPAQuery<DriverLicense>(session)
                .select(driverLicense)
                .from(driverLicense)
                .fetch();
    }

    @Override
    public Optional<DriverLicense> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.equal(driverLicense.get(DriverLicense_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<DriverLicense> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<DriverLicense>(session)
                .select(driverLicense)
                .from(driverLicense)
                .where(driverLicense.id.eq(id))
                .fetchOne());
    }

    public Optional<DriverLicense> findDriverLicenseByNumberCriteria(Session session, String driverLicenseNumber) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.equal(driverLicense.get(DriverLicense_.number), driverLicenseNumber));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public List<DriverLicense> findDriverLicenseByExpiredDateOrLessCriteria(Session session, LocalDate expiredDate) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);

        criteria.select(driverLicense)
                .where(cb.lessThanOrEqualTo(driverLicense.get(DriverLicense_.expiredDate), expiredDate));

        return session.createQuery(criteria)
                .list();
    }

    public List<DriverLicense> findDriverLicensesByIssueAndExpiredDateCriteria(Session session, DriverLicenseFilter driverLicenseFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicense.class);
        var driverLicense = criteria.from(DriverLicense.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(driverLicenseFilter.getIssueDate(), issueDate -> cb.greaterThanOrEqualTo(driverLicense.get(DriverLicense_.issueDate), issueDate))
                .add(driverLicenseFilter.getExpiredDate(), expiredDate -> cb.lessThanOrEqualTo(driverLicense.get(DriverLicense_.expiredDate), expiredDate))
                .getPredicates();

        criteria.select(driverLicense)
                .where(predicates);

        return session.createQuery(criteria)
                .list();
    }

    public List<DriverLicense> findDriverLicensesByIssueAndExpiredDateQueryDsl(Session session, DriverLicenseFilter driverLicenseFilter) {
        var predicateIssueDte = QPredicate.builder()
                .add(driverLicenseFilter.getIssueDate(), driverLicense.issueDate::eq)
                .add(driverLicenseFilter.getIssueDate(), driverLicense.issueDate::gt)
                .buildOr();

        var predicateExpiredDate = QPredicate.builder()
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::eq)
                .add(driverLicenseFilter.getExpiredDate(), driverLicense.expiredDate::lt)
                .buildOr();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateIssueDte)
                .addPredicate(predicateExpiredDate)
                .buildAnd();

        return new JPAQuery<DriverLicense>(session)
                .select(driverLicense)
                .from(driverLicense)
                .where(predicateAll)
                .fetch();
    }

    public List<DriverLicenseDto> findDriverLicensesByExpiredDateOrderBySurnameCriteria(Session session, LocalDate expiredDate) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(DriverLicenseDto.class);
        var driverLicense = criteria.from(DriverLicense.class);
        var userDetails = driverLicense.join(DriverLicense_.userDetails);
        Predicate predicate = cb.lessThan(driverLicense.get(DriverLicense_.expiredDate), expiredDate);

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

        return session.createQuery(criteria)
                .list();
    }

    public List<Tuple> findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(Session session, LocalDate expiredDate) {
        var predicate = QPredicate.builder()
                .add(expiredDate, driverLicense.expiredDate::eq)
                .add(expiredDate, driverLicense.expiredDate::lt)
                .buildOr();

        return new JPAQuery<Tuple>(session)
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