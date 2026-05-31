# OrangeHRM QA Automation Framework

> **Enterprise-grade Selenium WebDriver + TestNG automation framework**  
> Built with Java 25, Maven, Allure Reports, Log4j2, and Java Faker.  
> Designed as a portfolio-quality project demonstrating real-world QA engineering practices.

---

## Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Framework Architecture](#framework-architecture)
- [Design Decisions](#design-decisions)
- [Prerequisites](#prerequisites)
- [Setup & Run](#setup--run)
- [Allure Reports](#allure-reports)
- [Test Coverage](#test-coverage)
- [CI/CD Integration](#cicd-integration)

---

## Overview

This framework automates functional test scenarios for the [OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/), covering four key business modules: **Login**, **Dashboard**, **Employee Management (PIM)**, **Leave Management**, and **Admin**.

The design goal is not maximum test count — it is to demonstrate:
- Clean, maintainable, scalable architecture
- Proper separation of concerns (Page Objects vs. Test Logic)
- Enterprise patterns (ThreadLocal WebDriver, Singleton ConfigReader, Listener-based reporting)
- Real-world practices (Allure attachments, Log4j2 logging, Java Faker test data)

---

## Tech Stack

| Tool | Purpose | Version |
|------|---------|---------|
| Java | Language | 25 |
| Maven | Build & Dependency Management | 3.9+ |
| Selenium WebDriver | Browser Automation | 4.35.0 |
| TestNG | Test Framework | 7.11.0 |
| WebDriverManager | Automatic Driver Binaries | 6.3.2 |
| Allure | HTML Test Reports | 2.35.1 |
| Log4j2 | Structured Logging | 2.25.1 |
| Java Faker | Test Data Generation | 1.0.2 |
| Lombok | Boilerplate Reduction | 1.18.42 |

---

## Framework Architecture

```
src/
├── main/java/com/orangehrm/qa/
│   ├── config/
│   │   └── ConfigReader.java          # Singleton; loads config.properties
│   ├── constants/
│   │   └── AppConstants.java          # All magic strings in one place
│   ├── drivers/
│   │   └── DriverFactory.java         # ThreadLocal WebDriver (parallel-safe)
│   ├── pages/
│   │   ├── BasePage.java              # Abstract parent; PageFactory init
│   │   ├── LoginPage.java
│   │   ├── DashboardPage.java
│   │   ├── PIMPage.java
│   │   ├── LeavePage.java
│   │   └── AdminPage.java
│   └── utils/
│       ├── WaitUtil.java              # All explicit wait strategies
│       ├── ElementActions.java        # Wrapped Selenium interactions
│       └── ScreenshotUtil.java        # Allure + disk screenshot capture
│
└── test/
    ├── java/com/orangehrm/qa/
    │   ├── base/
    │   │   └── BaseTest.java          # WebDriver lifecycle; @Before/@After
    │   ├── data/
    │   │   └── TestDataFactory.java   # Java Faker test data factory (Java 25 Records)
    │   ├── listeners/
    │   │   ├── AllureListener.java    # Screenshot on failure + Allure attachment
    │   │   ├── RetryAnalyzer.java     # Retry logic for flaky tests (1 retry)
    │   │   └── RetryTransformer.java  # Programmatic retry analyzer registration
    │   └── tests/
    │       ├── LoginTests.java        # 7 test cases
    │       ├── DashboardTests.java    # 4 test cases
    │       ├── EmployeeTests.java     # 5 test cases
    │       ├── LeaveTests.java        # 5 test cases
    │       └── AdminTests.java        # 5 test cases
    └── resources/
        ├── config.properties          # Environment config (URL, browser, creds)
        ├── log4j2.xml                 # Log4j2 appender/logger configuration
        └── testng.xml                 # Smoke + Regression suite definitions
```

---

## Design Decisions

### 1. ThreadLocal WebDriver (Parallel Safety)
`DriverFactory` stores one `WebDriver` instance per thread via `ThreadLocal`. This allows `parallel="methods"` in TestNG without threads sharing browser state.

### 2. Assertions Only in Test Classes
Page Object classes **never** contain `assert` statements. They return state for tests to verify. This is a strict separation of concerns — pages model the UI, tests encode business expectations.

### 3. Singleton ConfigReader
All configuration is centralized in `ConfigReader` with a thread-safe double-checked locking singleton. No magic strings or hardcoded values appear in production code.

### 4. Java Faker for Test Data
All dynamic data (employee names, usernames, passwords) is generated fresh per test run, preventing test coupling and enabling true independence.

### 5. Listener-Based Screenshot Capture
`AllureListener` implements `ITestListener.onTestFailure()` and calls `ScreenshotUtil`. Screenshots are attached directly to the Allure HTML report AND saved to disk — without requiring test classes to do anything.

### 6. Constants Over Strings
`AppConstants` stores every repeated string: URL fragments, error messages, group tags. Changes to the application require updating one file, not a dozen.

---

## Prerequisites

- Java 25 (JDK)
- Maven 3.9+
- Chrome or Firefox installed
- Network access to `https://opensource-demo.orangehrmlive.com/`

---

## Setup & Run

### Clone the repository
```bash
git clone <repo-url>
cd orangehrm-qa-framework
```

### Run all tests (default: Chrome, headed)
```bash
mvn clean test
```

### Run with headless Chrome (CI mode)
```bash
mvn clean test -Dheadless=true
```

### Run only Smoke suite
Edit `testng.xml` or pass group:
```bash
mvn clean test -Dgroups=smoke
```

### Run only Regression suite
```bash
mvn clean test -Dgroups=regression
```

### Run with Firefox
Set `browser=firefox` in `config.properties`, or:
```bash
mvn clean test -Dbrowser=firefox
```

---

## Allure Reports

### Generate and open report
```bash
mvn allure:serve
```

### Generate report to `target/allure-report/`
```bash
mvn allure:report
```

Allure report features used in this framework:
- `@Epic` / `@Feature` / `@Story` for BDD-style grouping
- `@Severity` for test importance classification
- `@Description` for plain-English test documentation
- `@Step` for named steps in BaseTest
- Automatic screenshot attachment on failure
- Failure cause text attachment

---

## Test Coverage

| Module | Test ID | Scenario | Type | Priority |
|--------|---------|----------|------|----------|
| Login | TC-L01 | Valid admin credentials login | Smoke | Blocker |
| Login | TC-L02 | Invalid username + password | Regression | Critical |
| Login | TC-L03 | Wrong password with valid username | Regression | Critical |
| Login | TC-L04 | Empty credentials validation | Regression | Normal |
| Login | TC-L05 | Password-only submission | Regression | Normal |
| Login | TC-L06 | Logout workflow | Regression | Critical |
| Login | TC-L07 | Case-sensitive username | Regression | Normal |
| Dashboard | TC-D01 | Dashboard loads post-login | Smoke | Blocker |
| Dashboard | TC-D02 | Navigate to PIM | Smoke | Critical |
| Dashboard | TC-D03 | Navigate to Admin | Smoke | Critical |
| Dashboard | TC-D04 | Navigate to Leave | Smoke | Critical |
| Employee | TC-E01 | Employee list page loads | Regression | Critical |
| Employee | TC-E02 | Employee list has records | Regression | Normal |
| Employee | TC-E03 | Add Employee form opens | Regression | Critical |
| Employee | TC-E04 | Add new employee (happy path) | Regression | Blocker |
| Employee | TC-E05 | Search employee by name | Regression | Normal |
| Leave | TC-LV01 | Leave list page loads | Regression | Critical |
| Leave | TC-LV02 | Apply Leave form accessible | Regression | Critical |
| Leave | TC-LV03 | Apply Leave page context | Regression | Normal |
| Leave | TC-LV04 | Leave Entitlement navigation | Regression | Normal |
| Leave | TC-LV05 | Leave list search with no filters | Regression | Normal |
| Admin | TC-A01 | Admin page loads | Regression | Critical |
| Admin | TC-A02 | User table has records | Regression | Normal |
| Admin | TC-A03 | Add User form accessible | Regression | Critical |
| Admin | TC-A04 | Search for existing user | Regression | Normal |
| Admin | TC-A05 | Search for non-existent user | Regression | Normal |

**Total: 26 test cases across 5 modules**

---

## CI/CD Integration

This framework is CI-ready. Example GitHub Actions config:

```yaml
name: QA Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '25', distribution: 'temurin' }
      - run: mvn clean test -Dheadless=true
      - uses: simple-elf/allure-report-action@v1
        with:
          allure_results: target/allure-results
```

---

## Project Author

Designed and implemented as a portfolio-quality QA Automation framework demonstrating enterprise engineering practices. Suitable for Junior QA Automation Engineer or QA Internship roles.
