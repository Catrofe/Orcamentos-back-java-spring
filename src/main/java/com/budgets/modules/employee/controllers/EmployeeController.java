package com.budgets.modules.employee.controllers;

import com.budgets.modules.employee.dto.ChangeRoleDTO;
import com.budgets.modules.employee.dto.EmployeeGetDTO;
import com.budgets.modules.employee.dto.EmployeeLoginDTO;
import com.budgets.modules.employee.dto.EmployeeRegisterDTO;
import com.budgets.modules.employee.entities.Employee;
import com.budgets.modules.employee.entities.enums.RoleEmployee;
import com.budgets.modules.employee.services.EmployeeService;
import com.budgets.modules.security.config.JwtUtil;
import com.budgets.modules.security.util.Extration;
import com.budgets.modules.user.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/new-employee")
    public ResponseEntity<List<EmployeeGetDTO>> requestsNewEmployee() {
        try {
            List<EmployeeGetDTO> requests = service.requestsNewEmployee();
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/authorize/{id}")
    public ResponseEntity<EmployeeGetDTO> authorizeNewEmployee(@PathVariable Long id) {
        try {
            EmployeeGetDTO employee = service.authorizeEmployee(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/change-role/{id}")
    public ResponseEntity<EmployeeGetDTO> changeRole(@PathVariable Long id, @RequestBody ChangeRoleDTO role) {
        try {
            if (role.getRole().equals(RoleEmployee.valueOf(1).toString())) {
                EmployeeGetDTO employee = service.changeRole(id, role.getRole());
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
            else if (role.getRole().equals(RoleEmployee.valueOf(2).toString())) {
                EmployeeGetDTO employee = service.changeRole(id, role.getRole());
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<EmployeeGetDTO> findById(@RequestHeader("Authorization") String token) {
        try {
            TokenDTO tokenDTO = decodeToken(token);
            EmployeeGetDTO employees = service.findById(tokenDTO.getId());
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenDTO decodeToken(String token) throws JsonProcessingException {
        Claims claims = jwtUtil.getClaims(token.replace("Bearer ", ""));
        Extration extration = new Extration();
        Long id = extration.extrationId(claims);
        return new TokenDTO(id);
    }

}
