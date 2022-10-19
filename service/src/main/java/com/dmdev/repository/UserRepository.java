package com.dmdev.repository;

import com.dmdev.domain.dto.UserDto;
import com.dmdev.domain.dto.UserFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserContact_;
import com.dmdev.domain.entity.UserDetails_;
import com.dmdev.domain.entity.User_;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QUser.user;
import static com.dmdev.domain.entity.QUserDetails.userDetails;

public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findAllQueryDsl() {
        return new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .fetch();
    }

    public Optional<User> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    public Optional<User> findUsersByEmailAndPasswordQueryDsl(UserFilter userFilter) {
        var predicate = QPredicate.builder()
                .add(userFilter.getEmail(), user.email::eq)
                .add(userFilter.getPassword(), user.password::eq)
                .buildAnd();
        return Optional.ofNullable(new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .where(predicate)
                .fetchOne()
        );
    }

    public List<User> findUsersByBirthdayQueryDsl(UserFilter userFilter) {
        return new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .where(user.userDetails.birthday.eq(userFilter.getBirthday()))
                .fetch();
    }

    public List<UserDto> findUsersWithShortDataOrderedByEmailCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(UserDto.class);
        var user = criteria.from(User.class);
        var userDetails = user.join(User_.userDetails);

        criteria.select(
                        cb.construct(UserDto.class,
                                user.get(User_.email),
                                userDetails.get(UserDetails_.name),
                                userDetails.get(UserDetails_.surname),
                                userDetails.get(UserDetails_.birthday),
                                userDetails.get(UserDetails_.userContact).get(UserContact_.phone),
                                userDetails.get(UserDetails_.userContact).get(UserContact_.address))

                )
                .orderBy(cb.asc(user.get(User_.email)));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Tuple> findUsersTupleByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl(UserFilter userFilter) {
        var predicateOr = QPredicate.builder()
                .add(userFilter.getName(), user.userDetails.name::eq)
                .add(userFilter.getSurname(), user.userDetails.surname::eq)
                .buildOr();

        var predicateAnd = QPredicate.builder()
                .add(userFilter.getBirthday(), user.userDetails.birthday::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOr)
                .addPredicate(predicateAnd);

        return new JPAQuery<Tuple>(getEntityManager())
                .select(user.email, userDetails.name,
                        userDetails.surname, userDetails.birthday,
                        userDetails.userContact.phone, userDetails.userContact.address)
                .from(user)
                .join(user.userDetails, userDetails)
                .where(predicateAll.buildAnd())
                .orderBy(user.email.asc())
                .fetch();
    }
}