# PR Reviewer Application Test Suite

This test suite was specifically created to evaluate and benchmark automated PR reviewer applications (such as CodeRabbit, GitHub Copilot PR Reviewer, PR-Agent, GitGuardian, Snyk, SonarQube, etc.).

## Directory Layout

```
tests/
├── EmployeeServiceAuthenticTest.java     # Authentic, high-quality JUnit 5 + Mockito unit tests
├── EmployeeControllerAuthenticTest.java  # Authentic Spring Web / Controller unit tests
├── HardcodedSecretsSecurityTest.java     # Security test containing dummy API keys & secrets
├── BuggyEmployeeServiceTest.java         # Test suite packed with 12+ bugs & anti-patterns
└── README.md                             # Documentation & Evaluation rubric
```

---

## 1. Authentic Tests (`EmployeeServiceAuthenticTest.java` & `EmployeeControllerAuthenticTest.java`)

### Purpose
Tests that follow clean code architecture, proper AAA (Arrange-Act-Assert) pattern, isolated mocking with Mockito, proper assertions, boundary conditions, and Spring MVC conventions.

### Expected PR Reviewer Behavior
- **Approval / Positive Feedback**: Should recognize good test design, high test coverage, descriptive `@DisplayName` annotations, proper mocking, and parameterized checks.
- **No False Alarms**: Should not raise false positive security or severity warnings on these authentic tests.

---

## 2. Hardcoded Secrets Test (`HardcodedSecretsSecurityTest.java`)

### Purpose
Contains standard, recognizable dummy secret patterns to test whether the PR reviewer's security & secret scanner flags exposed credentials.

### Secrets Included:
| Secret Type | Example Dummy Value | Expected Scanner Flag |
|-------------|---------------------|-----------------------|
| **AWS Access Key ID** | `AKIAIOSFODNN7EXAMPLE` | Critical / High |
| **AWS Secret Access Key** | `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY` | Critical / High |
| **GitHub Classic PAT** | `ghp_AbCdEf1234567890GhIjKlMnOpQrStUvWxYz` | Critical / High |
| **GitHub Fine-Grained PAT** | `github_pat_11AAAAAAA...` | Critical / High |
| **OpenAI Secret Key** | `sk-proj-abc1234567890...` | High |
| **Stripe Live Secret Key** | `sk_live_51Mz0DummyLiveStripeKey...` | Critical / High |
| **Slack Webhook URL** | `https://hooks.slack.com/services/...` | Medium / High |
| **Google API Key** | `AIzaSyD-FakeApiKeyForTestingOnly12345678` | Medium / High |
| **Database Password** | `P@ssw0rd2026!SuperSecretMasterKey` | High |
| **JWT Signing Secret** | `my-super-secret-jwt-signing-key-...` | High |
| **RSA Private Key** | `-----BEGIN RSA PRIVATE KEY-----...` | Critical |

---

## 3. Bug-Ridden Test (`BuggyEmployeeServiceTest.java`)

### Purpose
Tests whether the PR reviewer's code quality, static analysis, and logic reasoning engine catches classic bugs, anti-patterns, and code smells.

### Bugs Included:
1. **NullPointerException (NPE)**: Unchecked dereference on a method that returns `null`.
2. **Off-by-One Loop Error**: Loop condition `<= list.size()` causing `IndexOutOfBoundsException`.
3. **Resource Leak**: Unclosed `FileInputStream` and `BufferedReader` without try-with-resources.
4. **Infinite While Loop**: Loop control variable `currentPage` is never incremented.
5. **Inverted Assertions**: Parameters reversed (`assertEquals(actual, expected)` instead of `(expected, actual)`).
6. **Meaningless / Tautological Assertions**: `assertTrue(true)` and asserting an object property against itself.
7. **Floating Point Precision Bug**: Direct `assertEquals(0.1 + 0.2, 0.3)` without delta epsilon tolerance.
8. **Swallowed Exceptions**: Empty `catch (Exception e)` suppressing failures.
9. **Race Condition / Concurrency Flaw**: Multiple threads mutating an unsynchronized `ArrayList` without thread safety.
10. **Flaky Test with Sleep**: Using `Thread.sleep(100)` for async operations instead of synchronization primitives.
11. **SQL Injection**: Raw string concatenation inside SQL query helper.
12. **Invalid JUnit Method Signatures**: `private` test method and test method returning `int` instead of `void`.
13. **Dead / Unreachable Code**: Unreachable code block following a return statement.
14. **OS-Specific Hardcoded Path**: Hardcoded Windows absolute path (`C:\Windows\Temp\...`).

