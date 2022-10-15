package com.dmdev.repository;

import com.dmdev.domain.dto.UserDetailsFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.domain.entity.UserDetails_;
import com.dmdev.domain.entity.User_;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QUser.user;
import static com.dmdev.domain.entity.QUserDetails.userDetails;

public class UserDetailsRepository implements Repository<Long, UserDetails> {
    private static final UserDetailsRepository INSTANCE = new UserDetailsRepository();

    public static UserDetailsRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<UserDetails> findAllHql(Session session) {
        return session.createQuery("select ud from UserDetails ud", UserDetails.class)
                .list();
    }

    @Override
    public List<UserDetails> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(UserDetails.class);
        var userDetails = criteria.from(UserDetails.class);

        criteria.select(userDetails);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<UserDetails> findAllQueryDsl(Session session) {
        return new JPAQuery<UserDetails>(session)
                .select(userDetails)
                .from(userDetails)
                .fetch();
    }

    @Override
    public Optional<UserDetails> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(UserDetails.class);
        var userDetails = criteria.from(UserDetails.class);

        criteria.select(userDetails)
                .where(cb.equal(userDetails.get(UserDetails_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<UserDetails> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<User>(session)
                .select(userDetails)
                .from(userDetails)
                .where(userDetails.id.eq(id))
                .fetchOne());
    }

    public Optional<UserDetails> findUserDetailsByUserIdCriteria(Session session, Long userId) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(UserDetails.class);
        var userDetails = criteria.from(UserDetails.class);
        var user = userDetails.join(UserDetails_.user);

        criteria.select(userDetails)
                .where(cb.equal(user.get(User_.id), userId));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public List<UserDetails> findUserDetailsByNameAndSurnameQueryDsl(Session session, UserDetailsFilter userDetailsFilter) {
        var predicate = QPredicate.builder()
                .add(userDetailsFilter.getName(), userDetails.name::eq)
                .add(userDetailsFilter.getSurname(), userDetails.surname::eq)
                .buildAnd();

        return new JPAQuery<UserDetails>(session)
                .select(userDetails)
                .from(userDetails)
                .where(predicate)
                .fetch();
    }

    public List<Tuple> findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(Session session, LocalDate localDate) {
        var predicate = QPredicate.builder()
                .add(localDate.getMonth().getValue(), userDetails.birthday.month().intValue()::eq)
                .add(localDate.getDayOfMonth(), userDetails.birthday.dayOfMonth()::eq)
                .buildAnd();

        return new JPAQuery<Tuple>(session)
                .select(userDetails.surname, userDetails.name, userDetails.birthday, userDetails.userContact.phone, user.email)
                .from(userDetails)
                .join(userDetails.user, user)
                .where(predicate)
                .orderBy(userDetails.surname.asc(), userDetails.name.asc())
                .fetch();
    }
}