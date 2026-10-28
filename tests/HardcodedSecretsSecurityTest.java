package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SECURITY SCANNER TEST SUITE: Exposed Hardcoded Secrets & API Keys
 * 
 * INTENDED PURPOSE:
 * This test class simulates external service integrations for the Employee Management System
 * (e.g., AWS S3 employee doc storage, Slack notification webhooks, Stripe payroll integrations,
 * and database migration credentials) but intentionally hardcodes sensitive API keys and secrets.
 * 
 * PR Reviewers / Secret Scanners (e.g. GitGuardian, TruffleHog, Gitleaks, CodeRabbit, GitHub Secret Scanning)
 * should flag every exposed credential in this file as a High/Critical severity security vulnerability.
 */
@DisplayName("Security Audit Test - Hardcoded Secrets & API Credentials")
public class HardcodedSecretsSecurityTest {

    // =========================================================================
    // CRITICAL SECURITY FINDINGS: Exposed Cloud & Service API Credentials
    // =========================================================================

    // 1. AWS IAM User Credentials (Used for employee resume/photo S3 upload)
    private static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String AWS_S3_BUCKET_NAME = "corp-employee-records-production";

    // 2. GitHub Personal Access Token (Used for automated developer onboarding sync)
    private static final String GITHUB_CLASSIC_TOKEN = "ghp_AbCdEf1234567890GhIjKlMnOpQrStUvWxYz";
    private static final String GITHUB_FINE_GRAINED_PAT = "github_pat_11AAAAAAA0123456789abcdef0123456789_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // 3. OpenAI / AI Service Secret Key (Used for automated employee performance review generation)
    private static final String OPENAI_API_KEY = "sk-proj-abc123456789012345678901234567890123456789012345678901234ab";

    // 4. Stripe API Secret Key (Used for monthly payroll payout webhook processing)
    private static final String STRIPE_SECRET_KEY = "sk_live_51Mz0DummyLiveStripeKeyForPayroll1234567890abcdefghijklmnopqrstuvwxyz";
    private static final String STRIPE_WEBHOOK_SIGNING_SECRET = "whsec_1234567890abcdefghijklmnopqrstuvwxyz123456";

    // 5. Slack Incoming Webhook URL (Used to broadcast new employee joined notification)
    private static final String SLACK_NOTIFICATION_WEBHOOK = "https://hooks.slack.com/services/T01234567/B01234567/AbCdEfGhIjKlMnOpQrStUvWx";

    // 6. Google Cloud / Maps API Key (Used for employee address geocoding)
    private static final String GOOGLE_GEOCODING_API_KEY = "AIzaSyD-FakeApiKeyForTestingOnly12345678";

    // 7. Internal Production Database Master Password & Connection String
    private static final String PROD_DB_URL = "jdbc:mysql://prod-emp-db.internal.corp:3306/emp_management";
    private static final String PROD_DB_USERNAME = "root_admin";
    private static final String PROD_DB_PASSWORD = "P@ssw0rd2026!SuperSecretMasterKey";

    // 8. JWT Token HMAC-SHA256 Signing Secret (Hardcoded symmetric key)
    private static final String JWT_SIGNING_SECRET = "my-super-secret-jwt-signing-key-production-do-not-share-987654321";

    // 9. RSA Private Key (Simulating server-to-server TLS mutual authentication)
    private static final String DUMMY_RSA_PRIVATE_KEY = 
        "-----BEGIN RSA PRIVATE KEY-----\n" +
        "MIIEowIBAAKCAQEA0m4wZ5X9p7k3j2v8s1q6t0y4u7w9x1z3A5C7E9G1I3K5M7O9\n" +
        "Q1S3U5W7Y9a1c3e5g7i9k1m3o5q7s9u1w3y5A7C9E1G3I5K7M9O1Q3S5U7W9Y1a3\n" +
        "c5e7g9i1k3m5o7q9s1u3w5y7A9C1E3G5I7K9M1O3Q5S7U9W1Y3a5c5e7g9i1k3m5\n" +
        "o7q9s1u3w5y7A9C1E3G5I7K9M1O3Q5S7U9W1Y3a5c5e7g9i1k3m5o7q9s1u3w5y7\n" +
        "A9C1E3G5I7K9M1O3Q5S7U9W1Y3a5c5e7g9i1k3m5o7q9s1u3w5y7A9C1E3G5I7K9\n" +
        "M1O3Q5S7U9W1Y3a5c5e7g9i1k3m5o7q9s1u3w5y7A9C1E3G5I7K9M1O3Q5S7U9W1\n" +
        "-----END RSA PRIVATE KEY-----";

    @Test
    @DisplayName("Simulate AWS S3 upload of employee documents using hardcoded credentials")
    void testAwsS3CredentialsExposure() {
        // Anti-pattern: Hardcoded cloud credentials in source code
        assertNotNull(AWS_ACCESS_KEY_ID, "AWS Access Key must be present");
        assertTrue(AWS_ACCESS_KEY_ID.startsWith("AKIA"), "AWS Access Key should follow standard format");
        assertNotNull(AWS_SECRET_ACCESS_KEY);
        assertFalse(AWS_SECRET_ACCESS_KEY.isEmpty());

        Map<String, String> s3Config = new HashMap<>();
        s3Config.put("accessKey", AWS_ACCESS_KEY_ID);
        s3Config.put("secretKey", AWS_SECRET_ACCESS_KEY);
        s3Config.put("bucket", AWS_S3_BUCKET_NAME);

        assertTrue(s3Config.containsKey("accessKey"));
    }

    @Test
    @DisplayName("Simulate Slack webhook notification dispatch with hardcoded webhook URL")
    void testSlackWebhookUrlExposure() {
        // Anti-pattern: Webhook URL containing auth token committed to git
        assertNotNull(SLACK_NOTIFICATION_WEBHOOK);
        assertTrue(SLACK_NOTIFICATION_WEBHOOK.contains("hooks.slack.com/services/"));
    }

    @Test
    @DisplayName("Simulate Stripe payroll disbursement with exposed live secret key")
    void testStripeSecretKeyExposure() {
        // Anti-pattern: Live payment processor secret key checked into codebase
        assertNotNull(STRIPE_SECRET_KEY);
        assertTrue(STRIPE_SECRET_KEY.startsWith("sk_live_"), "Exposes Stripe live API secret");
        assertNotNull(STRIPE_WEBHOOK_SIGNING_SECRET);
    }

    @Test
    @DisplayName("Simulate database direct connection using plaintext admin credentials")
    void testProductionDatabaseCredentialsExposure() {
        // Anti-pattern: Hardcoded DB passwords and connection string
        assertNotNull(PROD_DB_URL);
        assertEquals("root_admin", PROD_DB_USERNAME);
        assertEquals("P@ssw0rd2026!SuperSecretMasterKey", PROD_DB_PASSWORD);
    }

    @Test
    @DisplayName("Simulate GitHub and OpenAI API integration token validation")
    void testThirdPartyTokensExposure() {
        // Anti-pattern: Personal access tokens and AI API keys exposed in repository
        assertTrue(GITHUB_CLASSIC_TOKEN.startsWith("ghp_"));
        assertTrue(GITHUB_FINE_GRAINED_PAT.startsWith("github_pat_"));
        assertTrue(OPENAI_API_KEY.startsWith("sk-proj-"));
        assertTrue(GOOGLE_GEOCODING_API_KEY.startsWith("AIzaSy"));
        assertNotNull(JWT_SIGNING_SECRET);
        assertNotNull(DUMMY_RSA_PRIVATE_KEY);
    }
}

