package com.example.demo.warranty;

import java.util.List;

public final class ValidationResult {
    private final String claimId;
    private final boolean approved;
    private final List<String> reasons;

    public ValidationResult(String claimId, boolean approved, List<String> reasons) {
        this.claimId = claimId;
        this.approved = approved;
        this.reasons = List.copyOf(reasons);
    }

    public String getClaimId() { return claimId; }
    public boolean isApproved() { return approved; }
    public List<String> getReasons() { return reasons; }
}
