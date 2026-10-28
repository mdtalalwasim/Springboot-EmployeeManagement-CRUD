package tests;

import com.wasim.springboot.entity.Employee;
import com.wasim.springboot.repository.EmpRepo;
import com.wasim.springboot.service.EmpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Authentic Unit Tests for {@link EmpService}.
 * 
 * Follows clean coding standards, proper AAA (Arrange-Act-Assert) pattern,
 * thorough boundary checks, and isolated mock interactions.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service - Authentic Unit Tests")
public class EmployeeServiceAuthenticTest {

    @Mock
    private EmpRepo empRepo;

    @InjectMocks
    private EmpService empService;

    private Employee sampleEmployee1;
    private Employee sampleEmployee2;

    @BeforeEach
    void setUp() {
        sampleEmployee1 = new Employee(1, "Alice Johnson", "alice.johnson@example.com", "123 Main St, New York", "1234567890", 75000);
        sampleEmployee2 = new Employee(2, "Bob Smith", "bob.smith@example.com", "456 Market St, San Francisco", "9876543210", 85000);
    }

    @Nested
    @DisplayName("Create / Add Employee Operations")
    class AddEmployeeTests {

        @Test
        @DisplayName("Should successfully persist employee when valid employee object is provided")
        void addEmp_WithValidEmployee_ShouldSaveSuccessfully() {
            // Arrange
            when(empRepo.save(any(Employee.class))).thenReturn(sampleEmployee1);

            // Act
            empService.addEmp(sampleEmployee1);

            // Assert
            verify(empRepo, times(1)).save(sampleEmployee1);
        }

        @Test
        @DisplayName("Should persist employee with updated details during edit flow")
        void addEmp_WithUpdatedDetails_ShouldCallRepositorySave() {
            // Arrange
            sampleEmployee1.setEmpSalary(90000);
            sampleEmployee1.setEmpAddress("789 Broadway Ave, New York");
            when(empRepo.save(sampleEmployee1)).thenReturn(sampleEmployee1);

            // Act
            empService.addEmp(sampleEmployee1);

            // Assert
            verify(empRepo, times(1)).save(sampleEmployee1);
            assertEquals(90000, sampleEmployee1.getEmpSalary());
            assertEquals("789 Broadway Ave, New York", sampleEmployee1.getEmpAddress());
        }
    }

    @Nested
    @DisplayName("Fetch Employee Operations")
    class FetchEmployeeTests {

        @Test
        @DisplayName("Should return complete list of employees when records exist in database")
        void getAllEmp_WhenRecordsExist_ShouldReturnAllEmployees() {
            // Arrange
            List<Employee> expectedEmployees = Arrays.asList(sampleEmployee1, sampleEmployee2);
            when(empRepo.findAll()).thenReturn(expectedEmployees);

            // Act
            List<Employee> actualEmployees = empService.getAllEmp();

            // Assert
            assertNotNull(actualEmployees, "Returned list should not be null");
            assertEquals(2, actualEmployees.size(), "List size should match mock data");
            assertEquals("Alice Johnson", actualEmployees.get(0).getEmpName());
            assertEquals("Bob Smith", actualEmployees.get(1).getEmpName());
            verify(empRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no employee records exist")
        void getAllEmp_WhenNoRecordsExist_ShouldReturnEmptyList() {
            // Arrange
            when(empRepo.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Employee> actualEmployees = empService.getAllEmp();

            // Assert
            assertNotNull(actualEmployees, "List should not be null even when empty");
            assertTrue(actualEmployees.isEmpty(), "Returned list should be empty");
            verify(empRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return employee when matching ID is found")
        void getEmpById_WhenIdExists_ShouldReturnEmployee() {
            // Arrange
            int targetId = 1;
            when(empRepo.findById(targetId)).thenReturn(Optional.of(sampleEmployee1));

            // Act
            Employee actualEmployee = empService.getEmpById(targetId);

            // Assert
            assertNotNull(actualEmployee, "Employee should be found");
            assertAll("Employee Properties Verification",
                () -> assertEquals(1, actualEmployee.getEmpId()),
                () -> assertEquals("Alice Johnson", actualEmployee.getEmpName()),
                () -> assertEquals("alice.johnson@example.com", actualEmployee.getEmpEmail()),
                () -> assertEquals("123 Main St, New York", actualEmployee.getEmpAddress()),
                () -> assertEquals("1234567890", actualEmployee.getEmpPhone()),
                () -> assertEquals(75000, actualEmployee.getEmpSalary())
            );
            verify(empRepo, times(1)).findById(targetId);
        }

        @Test
        @DisplayName("Should return null when employee with given ID does not exist")
        void getEmpById_WhenIdDoesNotExist_ShouldReturnNull() {
            // Arrange
            int nonExistentId = 999;
            when(empRepo.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act
            Employee result = empService.getEmpById(nonExistentId);

            // Assert
            assertNull(result, "Service should return null for non-existent employee ID");
            verify(empRepo, times(1)).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("Delete Employee Operations")
    class DeleteEmployeeTests {

        @Test
        @DisplayName("Should trigger repository deleteById with correct ID")
        void deleteEmpById_WithValidId_ShouldInvokeRepositoryDelete() {
            // Arrange
            int empIdToDelete = 1;

            // Act
            empService.deleteEmpById(empIdToDelete);

            // Assert
            verify(empRepo, times(1)).deleteById(empIdToDelete);
        }
    }

    @Nested
    @DisplayName("Pagination Operations")
    class PaginationTests {

        @Test
        @DisplayName("Should return paged employee results with correct page size and index")
        void getEmpByPagination_ShouldReturnPaginatedResults() {
            // Arrange
            int currentPage = 0;
            int pageSize = 3;
            Pageable expectedPageable = PageRequest.of(currentPage, pageSize);
            List<Employee> employeeSubList = Collections.singletonList(sampleEmployee1);
            Page<Employee> mockPage = new PageImpl<>(employeeSubList, expectedPageable, 1);

            when(empRepo.findAll(expectedPageable)).thenReturn(mockPage);

            // Act
            Page<Employee> resultPage = empService.getEmpByPagination(currentPage, pageSize);

            // Assert
            assertNotNull(resultPage, "Page result should not be null");
            assertEquals(1, resultPage.getTotalElements(), "Total elements count should be 1");
            assertEquals(1, resultPage.getTotalPages(), "Total pages count should be 1");
            assertEquals(1, resultPage.getContent().size(), "Content size should match");
            assertEquals("Alice Johnson", resultPage.getContent().get(0).getEmpName());
            verify(empRepo, times(1)).findAll(expectedPageable);
        }

        @Test
        @DisplayName("Should handle empty page when requested page exceeds total elements")
        void getEmpByPagination_BeyondAvailableData_ShouldReturnEmptyPage() {
            // Arrange
            int outOfBoundsPage = 10;
            int pageSize = 3;
            Pageable pageable = PageRequest.of(outOfBoundsPage, pageSize);
            Page<Employee> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(empRepo.findAll(pageable)).thenReturn(emptyPage);

            // Act
            Page<Employee> resultPage = empService.getEmpByPagination(outOfBoundsPage, pageSize);

            // Assert
            assertNotNull(resultPage);
            assertTrue(resultPage.getContent().isEmpty());
            assertEquals(0, resultPage.getTotalElements());
            verify(empRepo, times(1)).findAll(pageable);
        }
    }
}

