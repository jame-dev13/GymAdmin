package com.jame.dev.gymApp.features.metrics.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubscriptionAnnualResumeModel(
   @JsonProperty("membershipResume") MembershipResume membershipResume,
   @JsonProperty("topMembership") String membershipTop
) {
   @Builder
   public record MembershipResume(
      @JsonProperty("membershipCount") long membershipCount,
      @JsonProperty("countDistribution") MembershipDistribution membershipDistribution,
      @JsonProperty("netWorthDistribution") MembershipWorthDistribution membershipWorthDistribution
   ) {
      @Builder
      public record MembershipDistribution(
         @JsonProperty("biweeklyCount") long biweeklyCount,
         @JsonProperty("monthlyCount") long monthlyCount,
         @JsonProperty("quarterlyCount") long quarterlyCount,
         @JsonProperty("annualCount") long annualCount
      ) {
      }

      @Builder
      public record MembershipWorthDistribution(
         @JsonProperty("netWorth") BigDecimal netWorth,
         @JsonProperty("biweeklyWorth") BigDecimal biweeklyWorth,
         @JsonProperty("monthlyWorth") BigDecimal monthlyWorth,
         @JsonProperty("quarterlyWorth") BigDecimal quarterlyWorth,
         @JsonProperty("annualWorth") BigDecimal annualWorth
      ) {
      }
   }
}
