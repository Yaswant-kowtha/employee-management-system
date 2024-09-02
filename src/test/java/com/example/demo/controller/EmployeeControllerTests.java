package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    // Test method with mocked user
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user with "ADMIN" role
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = new Employee(1L, "John Doe", "john.doe@example.com", "Engineering");
        Employee employee2 = new Employee(2L, "Jane Smith", "jane.smith@example.com", "Marketing");

        Mockito.when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    // Test method with mocked user
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user with "ADMIN" role
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee(1L, "John Doe", "john.doe@example.com", "Engineering");

        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    // Test method with mocked user
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user with "ADMIN" role
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee(null, "John Doe", "john.doe@example.com", "Engineering");

        Mockito.when(employeeService.addEmployee(Mockito.any(Employee.class))).thenReturn(employee);

        String employeeJson = "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"department\": \"Engineering\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    // Test method with mocked user
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user with "ADMIN" role
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee(1L, "John Doe", "john.doe@example.com", "Engineering");

        Mockito.when(employeeService.updateEmployee(Mockito.eq(1L), Mockito.any(Employee.class))).thenReturn(employee);

        String employeeJson = "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"department\": \"Engineering\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    // Test method with mocked user
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user with "ADMIN" role
    public void testDeleteEmployee() throws Exception {
        Mockito.doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}