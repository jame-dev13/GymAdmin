package com.jame.dev.gymApp.features.metrics.infrastructure.query;

import com.jame.dev.gymApp.features.metrics.application.model.SubscriptionAnnualResumeModel;
import com.jame.dev.gymApp.features.metrics.application.support.mapper.SubscriptionMetricMapper;
import com.jame.dev.gymApp.features.metrics.domain.repository.SubscriptionMetricsRepositoryV2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SubscriptionMetricsRepositoryV2PsqlAdapter implements SubscriptionMetricsRepositoryV2 {

   @PersistenceContext
   private final EntityManager em;

   @Override
   public SubscriptionAnnualResumeModel calculateAnnualResume() {
      final String sql = """
         WITH membership_count AS (
             SELECT
                 m.membership,
                 COUNT(*) AS total,
                 COALESCE(SUM(m.price), 0) AS amount
             FROM subscriptions s
             INNER JOIN memberships m
                 ON m.id = s.membership_id
             WHERE s.status IN (
                 'FINALIZED',
                 'PAID',
                 'DROPPED',
                 'RENEWED'
             )
             GROUP BY m.membership
         ),
         subscription_membership_metrics AS (
             SELECT
                 COALESCE(SUM(mc.total), 0)::bigint AS membership_count,
         
                 COALESCE(SUM(mc.total) FILTER ( WHERE mc.membership = 'BIWEEKLY'), 0)::bigint AS biweekly_count,
         
                 COALESCE(SUM(mc.total) FILTER ( WHERE mc.membership = 'MONTHLY'), 0 )::bigint AS monthly_count,
         
                 COALESCE(SUM(mc.total) FILTER ( WHERE mc.membership = 'QUARTERLY'), 0)::bigint AS quarterly_count,
         
                 COALESCE(SUM(mc.total) FILTER ( WHERE mc.membership = 'ANNUAL'), 0 )::bigint AS annual_count
         
             FROM membership_count mc
         ),
         subscription_membership_worth AS (
             SELECT
                 COALESCE(SUM(mc.amount), 0)::decimal(10, 2) AS membership_net_worth,
         
                 COALESCE(SUM(mc.amount) FILTER ( WHERE mc.membership = 'BIWEEKLY'), 0)::decimal(10, 2) AS biweekly_worth,
         
                 COALESCE(SUM(mc.amount) FILTER ( WHERE mc.membership = 'MONTHLY'), 0 )::decimal(10, 2) AS monthly_worth,
         
                 COALESCE(SUM(mc.amount) FILTER ( WHERE mc.membership = 'QUARTERLY'), 0)::decimal(10, 2) AS quarterly_worth,
         
                 COALESCE(SUM(mc.amount) FILTER ( WHERE mc.membership = 'ANNUAL'), 0 )::decimal(10, 2) AS annual_worth
         
             FROM membership_count mc
         ),
         top_membership AS (
             SELECT
                 mc.membership
             FROM membership_count mc
             ORDER BY
                 mc.total DESC,
                 mc.amount DESC,
                 mc.membership
             LIMIT 1
         )
         SELECT
             smm.membership_count AS "membershipCount",
             smm.biweekly_count AS "biweeklyCount",
             smm.monthly_count AS "monthlyCount",
             smm.quarterly_count AS "quarterlyCount",
             smm.annual_count AS "annualCount",
             smw.membership_net_worth AS "netWorth",
             smw.biweekly_worth AS "biweeklyWorth",
             smw.monthly_worth AS "monthlyWorth",
             smw.quarterly_worth AS "quarterlyWorth",
             smw.annual_worth AS "annualWorth",
             tm.membership AS "membershipTop"
         FROM subscription_membership_metrics smm
         LEFT JOIN top_membership tm ON TRUE
         LEFT JOIN subscription_membership_worth smw ON TRUE;
         """;

      final Tuple rows = (Tuple) em.createNativeQuery(sql, Tuple.class)
         .getSingleResultOrNull();
      final var safeRows = Objects.requireNonNull(rows, "Something went wrong, could not compute subscription annual resume.");
      return SubscriptionMetricMapper.resumeModelFromRows(safeRows);
   }
}
