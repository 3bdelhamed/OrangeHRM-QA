package com.orangehrm.qa.data;

import com.github.javafaker.Faker;
import java.util.Locale;

/**
 * Data factory utilizing Java Faker to generate randomized, realistic test data models.
 * Replaces inline randomness inside test classes, providing clean inputs.
 */
public final class TestDataFactory {

    private static final Faker faker = new Faker(Locale.ENGLISH);

    private TestDataFactory() {
        throw new UnsupportedOperationException("TestDataFactory is a utility class.");
    }

    // Java 25 Records representing immutable test data payloads
    public record EmployeeData(String firstName, String lastName, String employeeId, String username, String password) {}
    public record AdminUserData(String username, String password, String employeeName, String status, String role) {}
    public record LeaveRequestData(String fromDate, String toDate, String leaveType, String comment) {}
    public record LoginCredentials(String username, String password) {}

    public static EmployeeData generateEmployee() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String employeeId = String.valueOf(faker.number().numberBetween(10000, 99999));
        String base = faker.name().username().replaceAll("[^a-zA-Z0-9]", "");
        String username = "qa_" + base.substring(0, Math.min(base.length(), 12)) + faker.number().digits(4);
        String password = "Test@" + faker.number().digits(4) + "Aa!";
        return new EmployeeData(firstName, lastName, employeeId, username, password);
    }

    public static AdminUserData generateAdminUser(String employeeName) {
        String base = faker.name().username().replaceAll("[^a-zA-Z0-9]", "");
        String username = "qa_" + base.substring(0, Math.min(base.length(), 12)) + faker.number().digits(4);
        String password = "Test@" + faker.number().digits(4) + "Aa!";
        return new AdminUserData(username, password, employeeName, "Enabled", "ESS");
    }

    public static LeaveRequestData generateLeaveRequest() {
        // Generate a random future date in the current year (2026) to ensure sufficient leave balance,
        // while randomizing the starting day to minimize overlapping request collisions.
        // OrangeHRM expects yyyy-dd-MM format
        int randomStartDays = faker.number().numberBetween(15, 200);
        java.time.LocalDate from = java.time.LocalDate.now().plusDays(randomStartDays);
        java.time.LocalDate to = from.plusDays(faker.number().numberBetween(1, 3));
        
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-dd-MM");
        return new LeaveRequestData(from.format(dtf), to.format(dtf), "CAN - Personal", "Faker leave application");
    }

    public static LoginCredentials generateInvalidCredentials() {
        String base = faker.name().username().replaceAll("[^a-zA-Z0-9]", "");
        String username = "invalid_" + base.substring(0, Math.min(base.length(), 10));
        String password = "InvalidPass@" + faker.number().digits(4);
        return new LoginCredentials(username, password);
    }

    public static String generateRandomDigits(int count) {
        return faker.number().digits(count);
    }
}
