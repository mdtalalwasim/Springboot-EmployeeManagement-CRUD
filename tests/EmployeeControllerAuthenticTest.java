package tests;

import com.wasim.springboot.controller.EmpController;
import com.wasim.springboot.entity.Employee;
import com.wasim.springboot.service.EmpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Authentic Unit/Web-Layer Tests for {@link EmpController}.
 * 
 * Verifies view name resolution, model attributes injection,
 * session attribute management, and service delegations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Controller - Authentic Unit Tests")
public class EmployeeControllerAuthenticTest {

    @Mock
    private EmpService empService;

    @InjectMocks
    private EmpController empController;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(101, "John Doe", "john.doe@example.com", "789 Pine St", "5551234567", 60000);
    }

    @Test
    @DisplayName("GET '/' should route to initial pagination index view")
    void home_ShouldDelegateToPaginationAndReturnIndexView() {
        // Arrange
        Model model = new ConcurrentModel();
        Page<Employee> mockPage = new PageImpl<>(Collections.singletonList(testEmployee), PageRequest.of(0, 3), 1);
        when(empService.getEmpByPagination(0, 3)).thenReturn(mockPage);

        // Act
        String viewName = empController.home(model);

        // Assert
        assertEquals("index", viewName);
        assertNotNull(model.getAttribute("emp"));
        assertEquals(0, model.getAttribute("currentPage"));
        assertEquals(1, model.getAttribute("totalPages"));
        assertEquals(1L, model.getAttribute("totalItems"));
        verify(empService, times(1)).getEmpByPagination(0, 3);
    }

    @Test
    @DisplayName("GET '/addEmployee' should return the addEmployee template name")
    void addEmp_ShouldReturnAddEmployeeView() {
        // Act
        String viewName = empController.addEmp();

        // Assert
        assertEquals("addEmployee.html", viewName);
    }

    @Test
    @DisplayName("GET '/editEmployee/{empId}' should populate model with employee and return edit view")
    void editEmp_WhenEmployeeFound_ShouldPopulateModelAndReturnEditView() {
        // Arrange
        Model model = new ConcurrentModel();
        int empId = 101;
        when(empService.getEmpById(empId)).thenReturn(testEmployee);

        // Act
        String viewName = empController.editEmp(empId, model);

        // Assert
        assertEquals("edit.html", viewName);
        assertEquals(testEmployee, model.getAttribute("employee"));
        verify(empService, times(1)).getEmpById(empId);
    }

    @Test
    @DisplayName("POST '/registerEmployee' should save employee, set session flash message, and redirect to root")
    void empRegister_ShouldSaveEmployeeAndRedirectWithSuccessMessage() {
        // Arrange
        MockHttpSession session = new MockHttpSession();

        // Act
        String redirectUrl = empController.empRegister(testEmployee, session);

        // Assert
        assertEquals("redirect:/", redirectUrl);
        assertEquals("Employee Added Successfully...", session.getAttribute("msg"));
        verify(empService, times(1)).addEmp(testEmployee);
    }

    @Test
    @DisplayName("POST '/updateEmployee' should update employee, set session message, and redirect to root")
    void updateEmp_ShouldUpdateEmployeeAndRedirectWithSuccessMessage() {
        // Arrange
        MockHttpSession session = new MockHttpSession();

        // Act
        String redirectUrl = empController.updateEmp(testEmployee, session);

        // Assert
        assertEquals("redirect:/", redirectUrl);
        assertEquals("Employee Updated Successfully.", session.getAttribute("msg"));
        verify(empService, times(1)).addEmp(testEmployee);
    }

    @Test
    @DisplayName("GET '/deleteEmployee/{empId}' should delete employee by id, set session message, and redirect to root")
    void deleteEmp_ShouldDeleteEmployeeAndRedirectWithSuccessMessage() {
        // Arrange
        int empIdToDelete = 101;
        MockHttpSession session = new MockHttpSession();

        // Act
        String redirectUrl = empController.deleteEmp(empIdToDelete, session);

        // Assert
        assertEquals("redirect:/", redirectUrl);
        assertEquals("Employee Deleted Successfully.", session.getAttribute("msg"));
        verify(empService, times(1)).deleteEmpById(empIdToDelete);
    }

    @Test
    @DisplayName("GET '/page/{pageNo}' should set pagination attributes and return index view")
    void findPagination_ShouldCalculatePaginationAttributesAndReturnIndex() {
        // Arrange
        int pageNo = 1;
        Model model = new ConcurrentModel();
        List<Employee> employeeList = Collections.singletonList(testEmployee);
        Page<Employee> mockPage = new PageImpl<>(employeeList, PageRequest.of(pageNo, 3), 10);
        when(empService.getEmpByPagination(pageNo, 3)).thenReturn(mockPage);

        // Act
        String viewName = empController.findPagination(pageNo, model);

        // Assert
        assertEquals("index", viewName);
        assertEquals(mockPage, model.getAttribute("emp"));
        assertEquals(1, model.getAttribute("currentPage"));
        assertEquals(4, model.getAttribute("totalPages")); // 10 items / 3 items per page = 4 pages
        assertEquals(10L, model.getAttribute("totalItems"));
        verify(empService, times(1)).getEmpByPagination(pageNo, 3);
    }
}

