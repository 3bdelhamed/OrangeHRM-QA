# OrangeHRM Requirements Analysis Document

## Document Information

| Field            | Value                            |
| ---------------- | -------------------------------- |
| Project Name     | OrangeHRM QA Testing Project     |
| Application Type | Human Resource Management System |
| Version          | Demo Version                     |
| Author           | Abdelhamed Ahmed                 |
| Document Type    | Requirements Analysis            |
| Testing Type     | Manual & Automation Testing      |

---

# 1. Introduction

## 1.1 Purpose

This document defines the business, functional, validation, and security requirements identified during the analysis of the OrangeHRM application.

The requirements serve as the foundation for:

* Test Planning
* Test Case Design
* Requirement Traceability Matrix (RTM)
* Defect Validation
* Automation Scope Definition

---

# 2. Application Overview

OrangeHRM is a Human Resource Management System used to manage:

* Employees
* User Accounts
* Leave Requests
* Recruitment Activities
* Timesheets
* Employee Information
* Organizational Directory

The system provides role-based access control and workflow management for HR operations.

---

# 3. Requirement Classification

| Prefix | Description                |
| ------ | -------------------------- |
| FR     | Functional Requirement     |
| VR     | Validation Requirement     |
| SR     | Security Requirement       |
| BR     | Business Rule              |
| UI     | User Interface Requirement |

---

# 4. Authentication Module Requirements

## Functional Requirements

### FR-LOGIN-001

User shall be able to login using valid username and password.

### FR-LOGIN-002

User shall be redirected to Dashboard after successful login.

### FR-LOGIN-003

User shall be able to logout from the system.

### FR-LOGIN-004

System shall maintain authenticated session after login.

### FR-LOGIN-005

System shall terminate session after logout.

---

## Validation Requirements

### VR-LOGIN-001

Username field shall be mandatory.

### VR-LOGIN-002

Password field shall be mandatory.

### VR-LOGIN-003

System shall display error message for invalid credentials.

### VR-LOGIN-004

System shall prevent login with empty credentials.

---

## Security Requirements

### SR-LOGIN-001

Unauthorized users shall not access protected pages.

### SR-LOGIN-002

Protected URLs shall require authentication.

### SR-LOGIN-003

User session shall expire after logout.

---

# 5. Dashboard Module Requirements

## Functional Requirements

### FR-DASH-001

Dashboard shall display after successful login.

### FR-DASH-002

Dashboard shall display available widgets.

### FR-DASH-003

Dashboard shall provide access to system modules.

### FR-DASH-004

User shall navigate between modules from dashboard.

### FR-DASH-005

Dashboard data shall refresh correctly.

---

## UI Requirements

### UI-DASH-001

Dashboard layout shall be properly aligned.

### UI-DASH-002

Dashboard widgets shall be visible.

### UI-DASH-003

Navigation menu shall remain accessible.

---

# 6. Admin Module Requirements

## Functional Requirements

### FR-ADMIN-001

Administrator shall create user accounts.

### FR-ADMIN-002

Administrator shall edit user accounts.

### FR-ADMIN-003

Administrator shall delete user accounts.

### FR-ADMIN-004

Administrator shall search users.

### FR-ADMIN-005

Administrator shall filter users.

### FR-ADMIN-006

Administrator shall assign user roles.

### FR-ADMIN-007

Administrator shall enable user accounts.

### FR-ADMIN-008

Administrator shall disable user accounts.

---

## Validation Requirements

### VR-ADMIN-001

Username shall be unique.

### VR-ADMIN-002

Mandatory fields shall not be empty.

### VR-ADMIN-003

System shall validate role selection.

---

## Security Requirements

### SR-ADMIN-001

Only administrators may access user management.

### SR-ADMIN-002

Unauthorized users shall be denied access.

---

# 7. PIM Module Requirements

## Functional Requirements

### FR-PIM-001

User shall add employee records.

### FR-PIM-002

User shall view employee information.

### FR-PIM-003

User shall update employee information.

### FR-PIM-004

User shall delete employee records.

### FR-PIM-005

User shall search employees.

### FR-PIM-006

User shall filter employee records.

### FR-PIM-007

User shall upload employee photographs.

### FR-PIM-008

User shall view employee profiles.

### FR-PIM-009

System shall store employee details.

### FR-PIM-010

System shall generate unique employee IDs.

---

## Validation Requirements

### VR-PIM-001

First Name shall be mandatory.

### VR-PIM-002

Employee ID shall be unique.

### VR-PIM-003

Uploaded image shall follow supported formats.

### VR-PIM-004

Mandatory employee fields shall not be empty.

---

## Business Rules

### BR-PIM-001

Each employee shall have a unique identifier.

### BR-PIM-002

Employee records shall remain searchable after creation.

---

# 8. Leave Module Requirements

## Functional Requirements

### FR-LEAVE-001

Employee shall apply for leave.

### FR-LEAVE-002

Employee shall cancel leave request.

### FR-LEAVE-003

Manager shall approve leave request.

### FR-LEAVE-004

Manager shall reject leave request.

### FR-LEAVE-005

User shall view leave balance.

### FR-LEAVE-006

User shall view leave history.

### FR-LEAVE-007

System shall update leave balances.

### FR-LEAVE-008

System shall maintain leave records.

---

## Validation Requirements

### VR-LEAVE-001

Leave type shall be mandatory.

### VR-LEAVE-002

Leave date shall be mandatory.

### VR-LEAVE-003

System shall validate leave balance.

### VR-LEAVE-004

Leave request shall require valid dates.

---

## Business Rules

### BR-LEAVE-001

Approved leave shall reduce available balance.

### BR-LEAVE-002

Rejected leave shall not affect balance.

### BR-LEAVE-003

Cancelled leave shall restore balance where applicable.

---

# 9. Recruitment Module Requirements

## Functional Requirements

### FR-REC-001

User shall create vacancies.

### FR-REC-002

User shall edit vacancies.

### FR-REC-003

User shall delete vacancies.

### FR-REC-004

User shall add candidates.

### FR-REC-005

User shall update candidate information.

### FR-REC-006

User shall upload candidate resumes.

### FR-REC-007

User shall search candidates.

### FR-REC-008

User shall track candidate status.

---

## Validation Requirements

### VR-REC-001

Candidate name shall be mandatory.

### VR-REC-002

Vacancy shall be selected.

### VR-REC-003

Resume uploads shall follow supported formats.

---

# 10. Time Module Requirements

## Functional Requirements

### FR-TIME-001

User shall create timesheets.

### FR-TIME-002

User shall edit timesheets.

### FR-TIME-003

User shall submit timesheets.

### FR-TIME-004

Manager shall approve timesheets.

### FR-TIME-005

Manager shall reject timesheets.

### FR-TIME-006

User shall view timesheet history.

---

## Validation Requirements

### VR-TIME-001

Date fields shall be mandatory.

### VR-TIME-002

Worked hours shall be numeric values.

---

# 11. My Info Module Requirements

## Functional Requirements

### FR-MYINFO-001

User shall view personal information.

### FR-MYINFO-002

User shall update personal information.

### FR-MYINFO-003

User shall manage contact details.

### FR-MYINFO-004

User shall manage emergency contacts.

### FR-MYINFO-005

User shall manage dependent information.

---

## Validation Requirements

### VR-MYINFO-001

Mandatory personal information shall not be empty.

### VR-MYINFO-002

Email addresses shall follow valid format.

### VR-MYINFO-003

Phone numbers shall accept valid values.

---

# 12. Directory Module Requirements

## Functional Requirements

### FR-DIR-001

User shall search employees.

### FR-DIR-002

User shall filter employees.

### FR-DIR-003

User shall view employee profiles.

### FR-DIR-004

System shall display matching search results.

---

## Validation Requirements

### VR-DIR-001

Search shall accept partial matches.

### VR-DIR-002

System shall handle empty search results.

---

# 13. Global System Requirements

## Security Requirements

### SR-SYS-001

System shall require authentication before accessing protected modules.

### SR-SYS-002

Role permissions shall restrict unauthorized actions.

### SR-SYS-003

Direct URL access shall follow authorization rules.

### SR-SYS-004

User sessions shall be securely maintained.

---

## UI Requirements

### UI-SYS-001

Pages shall load without visual defects.

### UI-SYS-002

Buttons shall be visible and clickable.

### UI-SYS-003

Tables shall display data correctly.

### UI-SYS-004

Forms shall display validation messages.

### UI-SYS-005

Application shall be usable on supported browsers.

---

# 14. Risk Assessment

| Module         | Risk Level |
| -------------- | ---------- |
| Authentication | High       |
| Admin          | High       |
| PIM            | High       |
| Leave          | High       |
| Recruitment    | Medium     |
| Time           | Medium     |
| Dashboard      | Medium     |
| Directory      | Low        |
| My Info        | Medium     |

---

# 15. Requirements Summary

| Category                | Count |
| ----------------------- | ----- |
| Functional Requirements | 55+   |
| Validation Requirements | 20+   |
| Security Requirements   | 9+    |
| Business Rules          | 5+    |
| UI Requirements         | 8+    |

Total Identified Requirements: 95+
