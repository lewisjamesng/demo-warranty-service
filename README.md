# demo-warranty-service

Warranty claim validation service. It parses claim JSON and checks VIN format, component coverage period, distance limits and claim amount. **Synthetic demo repository.** All code, rules and data are fictional sample material for a security-remediation demo.

**Owner:** `@demo-org/aftersales-warranty-team` (see `.github/CODEOWNERS`)

## Develop

Requires Java 17 and Maven 3.9+.

```bash
mvn -B verify
```

## Security scanning

CI runs `osv-scanner` in the `security-scan` job. Reports are uploaded as artifacts, and the SARIF goes to GitHub code scanning.

## Licence

MIT
