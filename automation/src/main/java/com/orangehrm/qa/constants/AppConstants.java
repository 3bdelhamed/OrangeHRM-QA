package com.orangehrm.qa.constants;

/**
 * Central repository for all application-level constants.
 * Eliminates magic strings throughout the codebase.
 */
public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("Constants class — do not instantiate.");
    }

    // ── Page Titles ───────────────────────────────────────────────────────────
    public static final String LOGIN_PAGE_TITLE       = "OrangeHRM";
    public static final String DASHBOARD_PAGE_TITLE   = "OrangeHRM";
    public static final String PIM_PAGE_TITLE         = "OrangeHRM";

    // ── URL Paths (relative to base URL) ─────────────────────────────────────
    public static final String DASHBOARD_URL_FRAGMENT = "dashboard";
    public static final String PIM_URL_FRAGMENT       = "pim/viewEmployeeList";
    public static final String LEAVE_URL_FRAGMENT     = "leave/viewLeaveList";
    public static final String ADMIN_URL_FRAGMENT     = "admin/viewSystemUsers";

    // ── Expected Text / Headings ──────────────────────────────────────────────
    public static final String INVALID_CREDENTIALS_MSG = "Invalid credentials";
    public static final String REQUIRED_FIELD_MSG      = "Required";
    public static final String DASHBOARD_HEADING       = "Dashboard";
    public static final String PIM_HEADING             = "Employee Information";
    public static final String LEAVE_HEADING           = "Leave List";
    public static final String ADMIN_HEADING           = "System Users";
    public static final String SUCCESS_TOAST_MSG       = "Successfully Saved";
    public static final String DELETE_SUCCESS_MSG      = "Successfully Deleted";

    // ── Browser Names ─────────────────────────────────────────────────────────
    public static final String CHROME  = "chrome";
    public static final String FIREFOX = "firefox";

    // ── Group Tags ────────────────────────────────────────────────────────────
    public static final String SMOKE      = "smoke";
    public static final String REGRESSION = "regression";
    public static final String LOGIN      = "login";
    public static final String EMPLOYEE   = "employee";
    public static final String LEAVE      = "leave";
    public static final String ADMIN      = "admin";

    // ── Timeouts (fallback — prefer ConfigReader values) ─────────────────────
    public static final int DEFAULT_EXPLICIT_WAIT  = 15;
    public static final int DEFAULT_IMPLICIT_WAIT  = 10;
    public static final int SHORT_WAIT             = 5;
    public static final int LONG_WAIT              = 30;
}
