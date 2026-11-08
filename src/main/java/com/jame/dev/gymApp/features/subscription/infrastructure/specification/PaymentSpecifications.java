package com.jame.dev.gymApp.features.subscription.infrastructure.specification;

import com.jame.dev.gymApp.features.subscription.domain.model.PaymentEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.PaymentMethod;
import com.jame.dev.gymApp.features.subscription.domain.model.PaymentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public interface PaymentSpecifications {

   static Specification<PaymentEntity> hasSearchIn(final String search) {
      return (root, query, cb) -> {
         if (Objects.isNull(search) || search.isBlank()) return cb.conjunction();

         final String like = '%' + search + '%';
         final String trimmed = search.trim();

         final var predicates = new ArrayList<Predicate>();
         predicates.add(cb.equal(cb.lower(root.get("currency")), like));

         try {
            final String UPPER = trimmed.toUpperCase();
            final var paymentStatusPredicate = cb.equal(root.get("paymentStatus").as(PaymentStatus.class), PaymentStatus.valueOf(UPPER));
            final var paymentMethodPredicate = cb.equal(root.get("paymentMethod").as(PaymentMethod.class), PaymentMethod.valueOf(UPPER));
            predicates.addAll(Set.of(paymentMethodPredicate, paymentStatusPredicate));
         } catch (IllegalArgumentException ignored) {
         }

         try {
            final var amount = new BigDecimal(trimmed);
            final var amountPredicate = cb.equal(root.get("amount").as(BigDecimal.class), amount);
            predicates.add(amountPredicate);
         } catch (NumberFormatException ignored) {
         }

         return cb.or(predicates.toArray(new Predicate[0]));
      };
   }
}
