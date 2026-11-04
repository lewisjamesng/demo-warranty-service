package com.example.demo.warranty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ClaimValidatorTest {
    private final ClaimValidator validator = new ClaimValidator();

    private static String claim(String component, String failureDate, int km, double amount) {
        return "{\"claimId\":\"WC-1001\",\"vin\":\"XXXDEMV0000000001\",\"component\":\"" + component
                + "\",\"purchaseDate\":\"2023-01-15\",\"failureDate\":\"" + failureDate
                + "\",\"odometerKm\":" + km + ",\"amount\":" + amount + "}";
    }

    @Test
    void approvesClaimInsideCoverage() throws IOException {
        ValidationResult r = validator.validate(validator.parse(claim("battery", "2025-06-01", 42_000, 4200.0)));
        assertTrue(r.isApproved(), r.getReasons().toString());
    }

    @Test
    void rejectsClaimOutsideCoveragePeriodAndDistance() throws IOException {
        ValidationResult r = validator.validate(validator.parse(claim("infotainment", "2025-06-01", 60_000, 300.0)));
        assertFalse(r.isApproved());
        assertEquals(2, r.getReasons().size());
    }

    @Test
    void rejectsUnknownComponentAndBadAmount() throws IOException {
        ValidationResult r = validator.validate(validator.parse(claim("sunroof", "2024-01-01", 1_000, 0)));
        assertTrue(r.getReasons().contains("component not covered: sunroof"));
        assertTrue(r.getReasons().stream().anyMatch(s -> s.startsWith("amount")));
    }

    @Test
    void rejectsUnknownJsonFields() {
        assertThrows(IOException.class, () -> validator.parse("{\"claimId\":\"X\",\"unexpected\":true}"));
    }

    @Test
    void serialisesResultAsJson() throws IOException {
        JsonNode node = new ObjectMapper().readTree(validator.validateJson(claim("drive-unit", "2024-01-01", 10_000, 900.0)));
        assertEquals("WC-1001", node.get("claimId").asText());
        assertTrue(node.get("approved").asBoolean());
    }
}
