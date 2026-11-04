package com.jame.dev.gymApp.features.metrics.infrastructure.query;

import com.jame.dev.gymApp.features.metrics.api.response.OverviewResponse;
import com.jame.dev.gymApp.features.metrics.domain.repository.OverviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class OverviewRepositoryPsqlAdapter implements OverviewRepository {
   private final EntityManager entityManager;

   @Override
   public OverviewResponse calculateOverviewMetrics() {
      final String sql = """
             WITH active_members AS (
                 SELECT
                     COUNT(c.id) AS active_count
                 FROM customers c
                 WHERE c.active = TRUE
             ),
             monthly_revenue AS (
                 SELECT
                     COALESCE(SUM(m.price), 0) AS current_month_total
                 FROM subscriptions s
                 INNER JOIN memberships m
                     ON m.id = s.membership_id
                 INNER JOIN payments p
                     ON p.subscription_id = s.id AND p.status = 'COMPLETED'
                 WHERE
                     EXTRACT(MONTH FROM s.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
                     AND EXTRACT(YEAR FROM s.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
             ),
             expiring_subscriptions AS (
                 SELECT
                     COUNT(DISTINCT s.id) AS expiring_total
                 FROM subscriptions s
                 INNER JOIN subscription_periods sp
                     ON sp.subscription_id = s.id
                 INNER JOIN periods p
                     ON p.id = sp.period_id
                 WHERE
                     p.end_period BETWEEN CURRENT_DATE
                     AND CURRENT_DATE + INTERVAL '7 days'
             ),
             new_signups AS (
                 SELECT
                     COUNT(DISTINCT u.id) AS signup_count
                 FROM users u
                 INNER JOIN tokens t
                     ON t.user_id = u.id
                     AND t.verified = TRUE
                 WHERE
                     u.created_at >= DATE_TRUNC('month', CURRENT_DATE)
                     AND u.created_at < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month'
             )
             SELECT
                 am.active_count AS "activeMembers",
                 mr.current_month_total AS "monthlyRevenue",
                 es.expiring_total AS "expiringSubscriptions",
                 ns.signup_count AS "newSignups"
             FROM active_members am
             CROSS JOIN monthly_revenue mr
             CROSS JOIN expiring_subscriptions es
             CROSS JOIN new_signups ns
         """;

      final Tuple row = (Tuple) entityManager.createNativeQuery(sql, Tuple.class)
         .getSingleResult();

      return new OverviewResponse(
         row.get("activeMembers", Long.class),
         row.get("monthlyRevenue", BigDecimal.class),
         row.get("expiringSubscriptions", Long.class),
         row.get("newSignups", Long.class)
      );
   }
}
