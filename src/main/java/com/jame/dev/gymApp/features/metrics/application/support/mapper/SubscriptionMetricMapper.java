package com.jame.dev.gymApp.features.metrics.application.support.mapper;

import com.jame.dev.gymApp.features.metrics.application.model.SubscriptionAnnualResumeModel;
import jakarta.persistence.Tuple;

import java.math.BigDecimal;

public final class SubscriptionMetricMapper {

   public static SubscriptionAnnualResumeModel resumeModelFromRows(final Tuple rows) {
      return SubscriptionAnnualResumeModel.builder()
         .membershipResume(SubscriptionAnnualResumeModel.MembershipResume.builder()
            .membershipCount(rows.get("membershipCount", Long.class))
            .membershipDistribution(SubscriptionAnnualResumeModel.MembershipResume.MembershipDistribution.builder()
               .biweeklyCount(rows.get("biweeklyCount", Long.class))
               .monthlyCount(rows.get("monthlyCount", Long.class))
               .quarterlyCount(rows.get("quarterlyCount", Long.class))
               .annualCount(rows.get("annualCount", Long.class))
               .build())
            .membershipWorthDistribution(SubscriptionAnnualResumeModel.MembershipResume.MembershipWorthDistribution.builder()
               .netWorth(rows.get("netWorth", BigDecimal.class))
               .biweeklyWorth(rows.get("biweeklyWorth", BigDecimal.class))
               .monthlyWorth(rows.get("monthlyWorth", BigDecimal.class))
               .quarterlyWorth(rows.get("quarterlyWorth", BigDecimal.class))
               .annualWorth(rows.get("annualWorth", BigDecimal.class))
               .build())
            .build())
         .membershipTop(rows.get("membershipTop", String.class))
         .build();
   }

   private SubscriptionMetricMapper() {
   }
}
