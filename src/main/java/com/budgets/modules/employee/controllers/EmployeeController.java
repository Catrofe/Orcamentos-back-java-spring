package com.budgets.modules.employee.controllers;

import com.budgets.modules.employee.dto.EmployeeLoginDTO;
import com.budgets.modules.employee.dto.EmployeeRegisterDTO;
import com.budgets.modules.employee.entities.Employee;
import com.budgets.modules.employee.entities.enums.RoleEmployee;
import com.budgets.modules.employee.services.EmployeeService;
import com.budgets.modules.security.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<EmployeeRegisterDTO> create(@RequestBody Employee employee) {
        try {
            EmployeeRegisterDTO newEmployee = service.insert(employee);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<EmployeeRegisterDTO> login(@RequestBody EmployeeLoginDTO employee, HttpServletResponse request) {
        try {
            EmployeeRegisterDTO byEmployee = service.findByEmployee(employee);
            String token = jwtUtil.generationToken(byEmployee.getId(), "EMPLOYEE");
            request.addHeader("Authorization", "Bearer "+token);
            return new ResponseEntity<>(byEmployee, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
