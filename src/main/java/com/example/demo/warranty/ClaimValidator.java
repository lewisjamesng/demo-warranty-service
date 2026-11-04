package com.example.demo.warranty;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/** Validates warranty claims against synthetic coverage rules. */
public final class ClaimValidator {
    private static final Pattern VIN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");

    /** Coverage period in months and distance limit per component. */
    private static final Map<String, int[]> COVERAGE = Map.of(
            "battery", new int[] {96, 160_000},
            "drive-unit", new int[] {48, 80_000},
            "infotainment", new int[] {24, 50_000});

    private static final double MAX_AMOUNT = 25_000.0;

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    public WarrantyClaim parse(String json) throws IOException {
        return mapper.readValue(json, WarrantyClaim.class);
    }

    public ValidationResult validate(WarrantyClaim claim) {
        List<String> reasons = new ArrayList<>();
        if (claim.getVin() == null || !VIN.matcher(claim.getVin()).matches()) {
            reasons.add("invalid VIN");
        }
        int[] rule = COVERAGE.get(claim.getComponent());
        if (rule == null) {
            reasons.add("component not covered: " + claim.getComponent());
        } else if (claim.getPurchaseDate() == null || claim.getFailureDate() == null) {
            reasons.add("purchaseDate and failureDate are required");
        } else {
            long months = ChronoUnit.MONTHS.between(claim.getPurchaseDate(), claim.getFailureDate());
            if (months < 0) {
                reasons.add("failureDate is before purchaseDate");
            } else if (months >= rule[0]) {
                reasons.add("outside coverage period of " + rule[0] + " months");
            }
            if (claim.getOdometerKm() > rule[1]) {
                reasons.add("odometer above limit of " + rule[1] + " km");
            }
        }
        if (claim.getAmount() <= 0 || claim.getAmount() > MAX_AMOUNT) {
            reasons.add("amount must be between 0 and " + MAX_AMOUNT);
        }
        return new ValidationResult(claim.getClaimId(), reasons.isEmpty(), reasons);
    }

    public String validateJson(String json) throws IOException {
        return mapper.writeValueAsString(validate(parse(json)));
    }
}
