package com.jame.dev.gymApp.features.subscription.infrastructure.specification;

import com.jame.dev.gymApp.features.customer.domain.model.CustomerEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.MembershipEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionStatus;
import com.jame.dev.gymApp.features.user.domain.model.UserEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface SubscriptionSpecifications {

   static Specification<SubscriptionEntity> hasEndPeriodBetween(final LocalDate today, final LocalDate limit) {
      return (root, query, cb) -> {
         query.distinct(true);

         final var periodJoin = root.join("subscriptionPeriods", JoinType.INNER);

         return cb.between(
            periodJoin.get("endPeriod"),
            today,
            limit
         );
      };
   }

   static Specification<SubscriptionEntity> hasStatuses(final Set<SubscriptionStatus> statuses) {
      return (root, query, cb) -> {
         query.distinct(true);
         return root.get("status").in(statuses);
      };
   }

   static Specification<SubscriptionEntity> containsSearch(final String search) {
      return (root, query, cb) -> {
         if (search == null || search.isBlank()) return cb.conjunction();

         final String like = "%" + search.toLowerCase() + "%";
         final String trimmed = search.trim();

         final Join<SubscriptionEntity, MembershipEntity> membershipJoin = root.join("membership", JoinType.LEFT);
         final Join<SubscriptionEntity, CustomerEntity> customerJoin = root.join("customer", JoinType.LEFT);
         final Join<CustomerEntity, UserEntity> userJoin = customerJoin.join("user", JoinType.LEFT);

         query.distinct(true);

         final List<Predicate> predicates = new ArrayList<>();
         predicates.add(cb.like(cb.lower(userJoin.get("email")), like));

         predicates.add(cb.equal(
            cb.lower(membershipJoin.get("membership")).as(String.class),
            trimmed.toLowerCase()
         ));

         try {
            predicates.add(cb.equal(
               root.get("status").as(SubscriptionStatus.class),
               SubscriptionStatus.valueOf(trimmed.toUpperCase())
            ));
         } catch (IllegalArgumentException ignored) {
         }

         try {
            BigDecimal price = new BigDecimal(trimmed);
            predicates.add(cb.equal(
               membershipJoin.get("price").as(BigDecimal.class),
               price
            ));
         } catch (NumberFormatException ignored) {
         }

         return cb.or(predicates.toArray(new Predicate[0]));
      };
   }
}
