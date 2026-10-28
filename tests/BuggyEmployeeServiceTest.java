package tests;

import com.wasim.springboot.entity.Employee;
import com.wasim.springboot.repository.EmpRepo;
import com.wasim.springboot.service.EmpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * BUG-RIDDEN TEST SUITE FOR PR REVIEWER TESTING
 * 
 * INTENDED PURPOSE:
 * This test file intentionally contains multiple code smells, severe bugs,
 * anti-patterns, resource leaks, concurrency flaws, and logic errors.
 * 
 * An effective automated PR Reviewer should identify and flag each issue below:
 *  1. NullPointerException (Dereferencing nullable returns without validation)
 *  2. Unbounded Infinite Loop (Loop variable is never incremented)
 *  3. Resource Leak (Unclosed streams and connections without try-with-resources)
 *  4. Off-by-one Error (<= size() causing IndexOutOfBoundsException)
 *  5. Inverted Assertions (Expected vs Actual parameters swapped)
 *  6. Tautological / No-Op Assertions (Asserting true == true or object == itself)
 *  7. Swallowed Exceptions (Silent catch blocks hiding real bugs)
 *  8. Floating Point Precision Bug (Direct float/double equality without delta)
 *  9. Race Conditions / Concurrency Flaws (Unsynchronized access to ArrayList)
 * 10. Flaky Test via Thread.sleep() (Non-deterministic timing)
 * 11. SQL Injection Risk in Test Helper (Raw string concatenation in SQL)
 * 12. OS-Specific Hardcoded File Paths (Breaks portability across Linux/macOS/Windows)
 * 13. Invalid JUnit Method Signatures (Private test method and non-void test method)
 * 14. Dead / Unreachable Code
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Buggy Test Suite - Designed for PR Reviewer Bug Detection")
public class BuggyEmployeeServiceTest {

    @Mock
    private EmpRepo empRepo;

    @InjectMocks
    private EmpService empService;

    private List<Employee> testEmployees;

    @BeforeEach
    void setup() {
        testEmployees = new ArrayList<>();
        testEmployees.add(new Employee(1, "John Smith", "john@test.com", "123 Elm St", "1112223333", 50000));
        testEmployees.add(new Employee(2, "Jane Doe", "jane@test.com", "456 Oak St", "4445556666", 60000));
    }

    // =========================================================================
    // BUG #1: Guaranteed NullPointerException (NPE)
    // =========================================================================
    @Test
    @DisplayName("BUG 1: Dereferences null return without null check")
    void testGetEmployeeById_NullPointerBug() {
        // When ID is not found, getEmpById returns null
        when(empRepo.findById(999)).thenReturn(Optional.empty());

        Employee nonExistentEmployee = empService.getEmpById(999);

        // BUG: Calling .getEmpName() directly on null returns NPE at runtime
        String employeeName = nonExistentEmployee.getEmpName().trim();
        assertEquals("Default", employeeName);
    }

    // =========================================================================
    // BUG #2: Off-By-One Error (IndexOutOfBoundsException)
    // =========================================================================
    @Test
    @DisplayName("BUG 2: Off-by-one loop boundary index out of bounds")
    void testIterateEmployees_OffByOneError() {
        when(empRepo.findAll()).thenReturn(testEmployees);
        List<Employee> list = empService.getAllEmp();

        // BUG: Condition uses '<=' instead of '<', list.get(2) throws IndexOutOfBoundsException
        int totalSalary = 0;
        for (int i = 0; i <= list.size(); i++) {
            totalSalary += list.get(i).getEmpSalary();
        }

        assertTrue(totalSalary > 0);
    }

    // =========================================================================
    // BUG #3: Severe Resource Leak (Streams unclosed)
    // =========================================================================
    @Test
    @DisplayName("BUG 3: Resource leak - FileInputStream and BufferedReader not closed")
    void testReadEmployeeImportFile_ResourceLeak() throws IOException {
        // BUG: Hardcoded OS-specific path (Windows only)
        File tempFile = new File("C:\\Windows\\Temp\\employee_import.csv");

        // BUG: Streams opened without try-with-resources and never closed
        if (tempFile.exists()) {
            FileInputStream fis = new FileInputStream(tempFile);
            BufferedReader reader = new BufferedReader(new FileReader(tempFile));
            String line = reader.readLine();
            assertNotNull(line);
            // Neither 'fis' nor 'reader' is closed in finally block!
        }
    }

    // =========================================================================
    // BUG #4: Infinite Loop (Will cause test runner to hang / timeout)
    // =========================================================================
    @Test
    @DisplayName("BUG 4: Infinite while loop due to missing counter increment")
    void testPagination_InfiniteLoopBug() {
        int currentPage = 0;
        int totalPages = 5;

        // BUG: 'currentPage' is never incremented inside the while loop body
        while (currentPage < totalPages) {
            System.out.println("Processing page: " + currentPage);
            // Missing: currentPage++;
            // This loop will run infinitely until thread timeout or OOM
            break; // Note: if comment was removed this hangs; even with break, logic is broken
        }

        assertEquals(0, currentPage);
    }

    // =========================================================================
    // BUG #5: Inverted & Tautological Assertions
    // =========================================================================
    @Test
    @DisplayName("BUG 5: Swapped expected/actual parameters and meaningless assertions")
    void testAssertions_InvertedAndMeaningless() {
        Employee emp = new Employee(10, "Mark", "mark@test.com", "City", "9999999999", 70000);

        // BUG: Swapped parameters - JUnit 5 assertEquals signature is assertEquals(expected, actual)
        // Here actual is first, expected is second
        assertEquals(emp.getEmpName(), "Mark");

        // BUG: Tautological assertion - asserts object equality against itself
        assertEquals(emp.getEmpId(), emp.getEmpId());

        // BUG: Useless assertion - always true, tests nothing
        assertTrue(true);

        // BUG: Instantiating a new object and asserting it is not null tests the JVM, not the app
        assertNotNull(new Employee());
    }

    // =========================================================================
    // BUG #6: Floating Point Math Precision Comparison Bug
    // =========================================================================
    @Test
    @DisplayName("BUG 6: Direct double comparison without delta epsilon")
    void testEmployeeBonusCalculation_FloatingPointPrecisionError() {
        double bonusBase = 0.1;
        double discretionaryBonus = 0.2;
        double totalBonusRate = bonusBase + discretionaryBonus;

        // BUG: 0.1 + 0.2 produces 0.30000000000000004 in IEEE 754 floating point!
        // Direct assertEquals(double, double) fails without delta parameter
        assertEquals(0.3, totalBonusRate);
    }

    // =========================================================================
    // BUG #7: Swallowed Exceptions (Silent Failure Anti-pattern)
    // =========================================================================
    @Test
    @DisplayName("BUG 7: Swallowing exceptions silently masks failures")
    void testDeleteEmployee_SwallowedException() {
        try {
            // Suppose an invalid state occurs
            empService.deleteEmpById(-1);
            throw new IllegalArgumentException("Negative employee IDs are not permissible");
        } catch (Exception e) {
            // BUG: Exception is caught and completely swallowed.
            // The test passes green even though an illegal operation occurred!
            System.err.println("Error ignored: " + e.getMessage());
        }
    }

    // =========================================================================
    // BUG #8: Concurrency Flaw / Race Condition (Flaky Test)
    // =========================================================================
    @Test
    @DisplayName("BUG 8: Race condition on unsynchronized ArrayList across threads")
    void testConcurrentEmployeeRegistration_RaceCondition() throws InterruptedException {
        List<Employee> concurrentList = new ArrayList<>(); // NOT thread-safe!

        Runnable task = () -> {
            for (int i = 0; i < 50; i++) {
                // BUG: Non-atomic insertion without locks into non-thread-safe list
                concurrentList.add(new Employee(i, "Worker-" + i, "w@test.com", "Office", "123", 40000));
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        // BUG: Flaky arbitrary sleep instead of join() or CountDownLatch
        Thread.sleep(100);

        // BUG: Will sporadically fail due to lost updates or ConcurrentModificationException
        assertEquals(100, concurrentList.size());
    }

    // =========================================================================
    // BUG #9: SQL Injection Vulnerability in Test Helper
    // =========================================================================
    @Test
    @DisplayName("BUG 9: SQL Injection in dynamic query helper")
    void testRawDatabaseQuery_SqlInjectionVulnerability() {
        String untrustedUserInput = "'; DROP TABLE EMP_SYSTEM; --";

        // BUG: Unsanitized SQL string concatenation allows SQL Injection
        String query = "SELECT * FROM EMP_SYSTEM WHERE emp_name = '" + untrustedUserInput + "'";

        assertNotNull(query);
        assertTrue(query.contains("DROP TABLE")); // Verifying vulnerability exists!
    }

    // =========================================================================
    // BUG #10: Invalid JUnit Method Signature (Private Method & Return Type)
    // =========================================================================
    // BUG: JUnit 5 test methods cannot be private; runner will ignore or report error
    @Test
    @DisplayName("BUG 10: Private visibility on test method")
    private void testPrivateMethodCannotBeExecutedByJUnit() {
        assertTrue(true);
    }

    // BUG: JUnit test methods must return void; returning int violates test framework contract
    @Test
    @DisplayName("BUG 11: Non-void return type on test method")
    public int testMethodReturningIntegerIsNotSupported() {
        return 42;
    }

    // =========================================================================
    // BUG #12: Dead / Unreachable Code
    // =========================================================================
    @Test
    @DisplayName("BUG 12: Dead and unreachable code block")
    void testDeadCodeBlock() {
        int status = 1;
        if (status == 1) {
            return;
        }

        // BUG: Dead code - this statement can never be reached
        int unreachableCalculation = 100 / 0;
        System.out.println("Unreachable: " + unreachableCalculation);
    }
}

