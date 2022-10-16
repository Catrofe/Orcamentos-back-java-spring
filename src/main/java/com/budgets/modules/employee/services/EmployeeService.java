package com.budgets.modules.employee.services;

import com.budgets.modules.employee.dto.EmployeeLoginDTO;
import com.budgets.modules.employee.dto.EmployeeRegisterDTO;
import com.budgets.modules.employee.entities.Employee;
import com.budgets.modules.employee.repository.EmployeeRepository;
import com.budgets.modules.user.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PasswordEncoder encoder;

    public EmployeeRegisterDTO insert(Employee employee) {
        employee.setPassword(encoder.encode(employee.getPassword()));
        employee.setAuthorities("EMPLOYEE");
        employee = repository.save(employee);
        if (employee.getId() == 1) {
            employee.setActive(true);
            employee.setAuthorities("ADMIN");
            employee = repository.save(employee);
        }
        return new EmployeeRegisterDTO(employee.getId(), employee.getEmail());
    }

    public EmployeeRegisterDTO findByEmployee(EmployeeLoginDTO employee) {
        Employee findByEmail = repository.findByEmailOrCpf(employee.getLogin(), employee.getLogin());
        if (encoder.matches(employee.getPassword(), findByEmail.getPassword())) {
            return new EmployeeRegisterDTO(findByEmail.getId(), findByEmail.getEmail());
        }
        else {
            throw new UsernameNotFoundException("Email or Password Not Found");
        }
    }
}
