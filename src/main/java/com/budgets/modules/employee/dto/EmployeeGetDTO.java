package com.budgets.modules.employee.dto;

import com.budgets.modules.employee.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeGetDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String Authorities;

}
