package com.jame.dev.gymApp.features.metrics.infrastructure.query;

import com.jame.dev.gymApp.features.metrics.application.model.*;
import com.jame.dev.gymApp.features.metrics.domain.model.CustomerEvolution;
import com.jame.dev.gymApp.features.metrics.domain.model.MonthTotal;
import com.jame.dev.gymApp.features.metrics.domain.model.SubscriberEvolution;
import com.jame.dev.gymApp.features.metrics.domain.repository.EvolutionMetricsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class EvolutionMetricsRepositorySqlAdapter implements EvolutionMetricsRepository {

   @PersistenceContext
   private final EntityManager em;

   private final Function<List<Tuple>, List<MonthlyEvolution>> monthlyEvolutionMapper = (tupleList) ->
      tupleList.stream()
         .map(
            rows -> MonthlyEvolution.builder()
               .month(rows.get("month", String.class))
               .joins(rows.get("joins", Long.class))
               .downs(rows.get("downs", Long.class))
               .build()
         )
         .toList();

   private final Function<Tuple, EvoInsights> evoInsightsMapper = tuple ->
      EvoInsights.builder()
         .joiningTotal(tuple.get("joins", Long.class))
         .downingTotal(tuple.get("downs", Long.class))
         .netMovement(tuple.get("netMovement", Long.class))
         .build();

   private final Function<List<Tuple>, List<MonthTotal>> monthlyBillingEvolution = tupleList ->
      tupleList.stream()
         .map(
            rows -> new MonthTotal(
               rows.get("month", String.class),
               rows.get("total", BigDecimal.class)
            )
         )
         .toList();

   private final Function<Tuple, BillingEvolutionModel.BillingEvoInsights> billingEvoInsightsMapper = tuple ->
      BillingEvolutionModel.BillingEvoInsights.builder()
         .totalRevenue(tuple.get("totalRevenue", BigDecimal.class))
         .bestMonth(tuple.get("bestMonth", String.class))
         .monthlyAverage(tuple.get("monthlyAverage", BigDecimal.class))
         .build();

   @Override
   public CustomerEvolutionModel calculateCustomerEvolution(long year) {
      final String monthlyEvolutionSql = """
             SELECT
                 TO_CHAR(
                     MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1),
                     'Mon'
                 ) AS month,
         
                 COUNT(c.id) FILTER (
                     WHERE EXTRACT(MONTH FROM c.created_at) = m.month_number
                       AND EXTRACT(YEAR FROM c.created_at) = :year
                 ) AS joins,
         
                 COUNT(c.id) FILTER (
                     WHERE EXTRACT(MONTH FROM c.deleted_at) = m.month_number
                       AND EXTRACT(YEAR FROM c.deleted_at) = :year
                 ) AS downs
         
             FROM generate_series(1, 12) AS m(month_number)
         
             LEFT JOIN customers c
                 ON (
                     c.created_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                     AND c.created_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
                 )
                 OR (
                     c.deleted_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                     AND c.deleted_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
                 )
             GROUP BY m.month_number
             ORDER BY m.month_number
         """;
      final String customersInsightsSql = """
         WITH customer_evo_insights AS (
             SELECT
                 COUNT(c.id) FILTER (
                  WHERE EXTRACT(YEAR FROM c.created_at) = :year
                ) AS joins,
                 COUNT(c.id) FILTER (
                  WHERE EXTRACT(YEAR FROM c.deleted_at) = :year
                ) AS downs
              FROM customers c
         ),
         customers_evo_net_grow AS (
             SELECT ABS(COALESCE(cei.joins - cei.downs, 0)) AS net_grow FROM customer_evo_insights cei
         )
         SELECT
             cei.joins AS "joins",
             cei.downs AS "downs",
             ceng.net_grow AS "netMovement"
         FROM customer_evo_insights cei
         CROSS JOIN customers_evo_net_grow ceng
         """;

      final List<Tuple> monthlyEvoRows = em.createNativeQuery(monthlyEvolutionSql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      final Tuple customerInsightsRows = (Tuple) em.createNativeQuery(customersInsightsSql, Tuple.class)
         .setParameter("year", year)
         .getSingleResultOrNull();

      return CustomerEvolutionModel.builder()
         .monthlyEvolution(this.monthlyEvolutionMapper.apply(monthlyEvoRows))
         .evoInsights(this.evoInsightsMapper.apply(Objects.requireNonNull(customerInsightsRows, "Something went wrong retrieving customer evolution metrics.")))
         .build();
   }

   @Override
   public SubscriberEvolutionModel calculateSubscriberEvolution(long year) {
      final String monthlyEvoSql = """
         SELECT
               TO_CHAR(
                   MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1),
                   'Mon'
               ) AS month,
         
               COUNT(DISTINCT s.id) FILTER (
                   WHERE EXTRACT(MONTH FROM s.created_at) = m.month_number
                     AND EXTRACT(YEAR FROM s.created_at) = :year AND (s.status = 'PAID' OR s.status = 'RENEWED')
               ) AS joins,
         
               COUNT(DISTINCT s.id) FILTER (
                   WHERE EXTRACT(MONTH FROM s.deleted_at) = m.month_number
                     AND EXTRACT(YEAR FROM s.deleted_at) = :year AND (s.status = 'DROPPED' OR s.status = 'FINALIZED')
               ) AS downs
         
           FROM generate_series(1, 12) AS m(month_number)
         
           LEFT JOIN subscriptions s
               ON (
                   s.created_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                   AND s.created_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
               )
               OR (
                   s.deleted_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                   AND s.deleted_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
               )
           LEFT JOIN
              subscription_periods sp ON sp.subscription_id = s.id
           LEFT JOIN
              periods p ON p.id = sp.period_id AND
              p.end_period - p.start_period > 0
           GROUP BY m.month_number
           ORDER BY m.month_number
         """;

      final String insightsSql = """
         WITH subscriber_insights_evo AS (
             SELECT
                 COUNT(s.id) FILTER (
                  WHERE EXTRACT(YEAR FROM s.created_at) = :year AND (s.status = 'PAID' OR s.status = 'RENEWED')
                ) AS joins,
                 COUNT(s.id) FILTER (
                  WHERE EXTRACT(YEAR FROM s.deleted_at) = :year AND (s.status = 'DROPPED' OR s.status = 'FINALIZED')
                ) AS downs
              FROM subscriptions s
         ),
         subscription_net_grow_evo AS (
             SELECT ABS(COALESCE(sie.joins - sie.downs, 0)) AS net_grow FROM subscriber_insights_evo sie
         )
         SELECT
             sie.joins AS "joins",
             sie.downs AS "downs",
             snge.net_grow AS "netMovement"
         FROM subscriber_insights_evo sie
         CROSS JOIN subscription_net_grow_evo snge
         """;

      final List<Tuple> subscriberMonthlyEvoRows = em.createNativeQuery(monthlyEvoSql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      final Tuple subscriberInsightsEvoRows = (Tuple) em.createNativeQuery(insightsSql, Tuple.class)
         .setParameter("year", year)
         .getSingleResultOrNull();

      final List<MonthlyEvolution> monthlyEvolution = monthlyEvolutionMapper.apply(subscriberMonthlyEvoRows);
      final EvoInsights evoInsights = evoInsightsMapper.apply(
         Objects.requireNonNull(subscriberInsightsEvoRows, "Something went wrong, could not retrieve subscriber evo insights.")
      );

      return SubscriberEvolutionModel.builder()
         .monthlyEvolution(monthlyEvolution)
         .evoInsights(evoInsights)
         .build();
   }

   @Override
   public BillingEvolutionModel calculateBillingEvolutionInsights(long year) {
      final String monthlyEvoSql = """
         SELECT
             TO_CHAR(
                 MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1),
                 'Mon'
             ) AS month,
             COALESCE(SUM(p.amount) FILTER ( WHERE p.status NOT IN ('PENDING', 'REFUNDED', 'FAILED') ), 0) AS total
         FROM generate_series(1, 12) AS m(month_number)
         
         LEFT JOIN payments p
             ON (
                 p.created_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                 AND p.created_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
             )
         GROUP BY m.month_number
         ORDER BY m.month_number
         """;

      final String billingInsightsSql = """
         WITH total_revenue AS (
             SELECT
                 COALESCE(SUM(p.amount), 0) AS totalRevenue
             FROM payments p
             WHERE p.status NOT IN ('PENDING', 'REFUNDED', 'FAILED') AND EXTRACT(YEAR FROM p.created_at) = :year
         ),
         monthly_avg AS (
             SELECT
                CAST(tr.totalRevenue / 12 AS DECIMAL(10, 2)) AS monthly_avg
             FROM total_revenue tr
         ),
         best_month AS (
             SELECT
                 TO_CHAR(MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1), 'Mon') AS best_month
             FROM generate_series(1, 12) AS m(month_number)
             LEFT JOIN payments p
                 ON (
                     p.created_at >= MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1)
                 AND p.created_at < MAKE_DATE(CAST(:year AS INTEGER), m.month_number, 1) + INTERVAL '1 month'
                 AND p.status NOT IN ('PENDING', 'REFUNDED', 'FAILED')
                 )
             GROUP BY m.month_number
             ORDER BY SUM(p.amount) DESC NULLS LAST
             LIMIT 1
         )
         SELECT
             tr.totalRevenue AS "totalRevenue",
             bm.best_month AS "bestMonth",
             ma.monthly_avg AS "monthlyAverage"
         FROM total_revenue tr
         CROSS JOIN best_month bm
         CROSS JOIN monthly_avg ma
         """;

      final List<Tuple> monthlyEvoRows = em.createNativeQuery(monthlyEvoSql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      final Tuple billingInsightsRows = (Tuple) em.createNativeQuery(billingInsightsSql, Tuple.class)
         .setParameter("year", year)
         .getSingleResultOrNull();

      final var billingMonthlyEvo = monthlyBillingEvolution.apply(monthlyEvoRows);
      final var billingInsights = billingEvoInsightsMapper.apply(
         Objects.requireNonNull(billingInsightsRows, "Something went wrong, could not compute billing evolutions.")
      );

      return BillingEvolutionModel.builder()
         .billingEvolutions(billingMonthlyEvo)
         .billingEvoInsights(billingInsights)
         .build();
   }

   @Override
   public List<CustomerEvolution> calculateJoiningCustomerEvolution(final long year) {
      final String sql = """
            SELECT
             TO_CHAR(MAKE_DATE(EXTRACT(YEAR FROM CURRENT_DATE)::int, m.month_number, 1), 'Mon') AS month,
             COUNT(c.id) AS customersNum
         FROM generate_series(1, 12) AS m(month_number)
         LEFT JOIN customers c
             ON EXTRACT(MONTH FROM c.created_at) = m.month_number
             AND EXTRACT(YEAR FROM c.created_at) = :year
         GROUP BY m.month_number
         ORDER BY m.month_number
         """;
      final List<Tuple> rows = em.createNativeQuery(sql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      return rows.stream()
         .map(row -> new CustomerEvolution(
            row.get("month", String.class),
            row.get("customersNum", Long.class)
         ))
         .toList();
   }

   @Override
   public List<CustomerEvolution> calculateDowningCustomerEvolution(final long year) {
      final String sql = """
            SELECT
             TO_CHAR(MAKE_DATE(EXTRACT(YEAR FROM CURRENT_DATE)::int, m.month_number, 1), 'Mon') AS month,
             COUNT(c.id) AS customersNum
         FROM generate_series(1, 12) AS m(month_number)
         LEFT JOIN customers c
             ON EXTRACT(MONTH FROM c.deleted_at) = m.month_number
             AND EXTRACT(YEAR FROM c.deleted_at) = :year
         GROUP BY m.month_number
         ORDER BY m.month_number
         """;
      final List<Tuple> rows = em.createNativeQuery(sql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      return rows.stream()
         .map(row -> new CustomerEvolution(
            row.get("month", String.class),
            row.get("customersNum", Long.class)
         ))
         .toList();
   }

   @Override
   public List<SubscriberEvolution> calculateJoiningSubscriberEvolution(final long year) {
      final String sql = """
            SELECT
             TO_CHAR(MAKE_DATE(EXTRACT(YEAR FROM CURRENT_DATE)::int, m.month_number, 1), 'Mon') AS month,
             COUNT(s.id) AS subscriptionsNum
         FROM generate_series(1, 12) AS m(month_number)
         LEFT JOIN subscriptions s
             ON EXTRACT(MONTH FROM s.created_at) = m.month_number
             AND s.status = 'PAID'
             AND EXTRACT(YEAR FROM s.created_at) = :year
         GROUP BY m.month_number
         ORDER BY m.month_number
         """;
      final List<Tuple> rows = em.createNativeQuery(sql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      return rows.stream()
         .map(row -> new SubscriberEvolution(
            row.get("month", String.class),
            row.get("subscriptionsNum", Long.class)
         ))
         .toList();
   }

   @Override
   public List<SubscriberEvolution> calculateDowningSubscribersBeforeEndTime(long year) {
      final String sql = """
         SELECT
             TO_CHAR(MAKE_DATE(EXTRACT(YEAR FROM CURRENT_DATE)::int, m.month_number, 1), 'Mon') AS month,
             COUNT(DISTINCT s.id) AS subscriptionsNum
         FROM generate_series(1, 12) AS m(month_number)
         LEFT JOIN subscriptions s
             ON EXTRACT(MONTH FROM s.created_at) = m.month_number
             AND (s.status = 'FINALIZED' OR s.active = false)
             AND EXTRACT(YEAR FROM s.created_at) = :year
         LEFT JOIN
             subscription_periods sp ON sp.subscription_id = s.id
         LEFT JOIN
             periods p ON p.id = sp.period_id AND
             p.end_period - p.start_period > 0
         GROUP by m.month_number
         ORDER BY m.month_number
         """;
      final List<Tuple> rows = em.createNativeQuery(sql, Tuple.class)
         .setParameter("year", year)
         .getResultList();
      return rows.stream()
         .map(
            r -> new SubscriberEvolution(
               r.get("month", String.class),
               r.get("subscriptionsNum", Long.class))
         )
         .toList();
   }

   @Override
   public List<MonthTotal> calculateBillingEvolution(final long year) {
      final String sql = """
         SELECT
             TO_CHAR(MAKE_DATE(EXTRACT(YEAR FROM CURRENT_DATE)::int, m.month_number, 1), 'Mon') AS month,
             COALESCE(SUM(p.amount), 0) AS total
         FROM generate_series(1, 12) AS m(month_number)
         LEFT JOIN payments p
             ON EXTRACT(MONTH FROM p.created_at) = m.month_number
             AND p.amount > 0
             AND EXTRACT(YEAR FROM p.created_at) = :year
         GROUP BY m.month_number
         ORDER BY m.month_number
         """;
      final List<Tuple> rows = em.createNativeQuery(sql, Tuple.class)
         .setParameter("year", year)
         .getResultList();

      return rows.stream()
         .map(row -> new MonthTotal(
            row.get("month", String.class),
            row.get("total", BigDecimal.class)
         ))
         .toList();
   }
}
