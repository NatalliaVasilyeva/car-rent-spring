package com.dmdev.repository;

import com.dmdev.domain.dto.UserDetailsFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.domain.entity.UserDetails_;
import com.dmdev.domain.entity.User_;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QUser.user;
import static com.dmdev.domain.entity.QUserDetails.userDetails;

@Repository
public class UserDetailsRepository extends BaseRepository<Long, UserDetails> {

    public UserDetailsRepository(EntityManager entityManager) {
        super(UserDetails.class, entityManager);
    }

    public List<UserDetails> findAllQueryDsl() {
        return new JPAQuery<UserDetails>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .fetch();
    }

    public Optional<UserDetails> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<User>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .where(userDetails.id.eq(id))
                .fetchOne());
    }

    public Optional<UserDetails> findUserDetailsByUserIdCriteria(Long userId) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(UserDetails.class);
        var userDetails = criteria.from(UserDetails.class);
        var user = userDetails.join(UserDetails_.user);

        criteria.select(userDetails)
                .where(cb.equal(user.get(User_.id), userId));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public List<UserDetails> findUserDetailsByNameAndSurnameQueryDsl(UserDetailsFilter userDetailsFilter) {
        var predicate = QPredicate.builder()
                .add(userDetailsFilter.getName(), userDetails.name::eq)
                .add(userDetailsFilter.getSurname(), userDetails.surname::eq)
                .buildAnd();

        return new JPAQuery<UserDetails>(getEntityManager())
                .select(userDetails)
                .from(userDetails)
                .where(predicate)
                .fetch();
    }

    public List<Tuple> findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(LocalDate localDate) {
        var predicate = QPredicate.builder()
                .add(localDate.getMonth().getValue(), userDetails.birthday.month().intValue()::eq)
                .add(localDate.getDayOfMonth(), userDetails.birthday.dayOfMonth()::eq)
                .buildAnd();

        return new JPAQuery<Tuple>(getEntityManager())
                .select(userDetails.surname, userDetails.name, userDetails.birthday, userDetails.userContact.phone, user.email)
                .from(userDetails)
                .join(userDetails.user, user)
                .where(predicate)
                .orderBy(userDetails.surname.asc(), userDetails.name.asc())
                .fetch();
    }
}