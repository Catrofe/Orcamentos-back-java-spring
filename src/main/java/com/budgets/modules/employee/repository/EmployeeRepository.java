package com.budgets.modules.employee.repository;

import com.budgets.modules.employee.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    Employee findByCpf(String cpf);

    Employee findByEmailOrCpf(String email, String cpf);

}
