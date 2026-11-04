package com.example.demo.warranty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/** A warranty claim submitted by a service centre. All data is synthetic. */
public final class WarrantyClaim {
    private final String claimId;
    private final String vin;
    private final String component;
    private final LocalDate purchaseDate;
    private final LocalDate failureDate;
    private final int odometerKm;
    private final double amount;

    @JsonCreator
    public WarrantyClaim(
            @JsonProperty("claimId") String claimId,
            @JsonProperty("vin") String vin,
            @JsonProperty("component") String component,
            @JsonProperty("purchaseDate") String purchaseDate,
            @JsonProperty("failureDate") String failureDate,
            @JsonProperty("odometerKm") int odometerKm,
            @JsonProperty("amount") double amount) {
        this.claimId = claimId;
        this.vin = vin;
        this.component = component;
        this.purchaseDate = purchaseDate == null ? null : LocalDate.parse(purchaseDate);
        this.failureDate = failureDate == null ? null : LocalDate.parse(failureDate);
        this.odometerKm = odometerKm;
        this.amount = amount;
    }

    public String getClaimId() { return claimId; }
    public String getVin() { return vin; }
    public String getComponent() { return component; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public LocalDate getFailureDate() { return failureDate; }
    public int getOdometerKm() { return odometerKm; }
    public double getAmount() { return amount; }
}
