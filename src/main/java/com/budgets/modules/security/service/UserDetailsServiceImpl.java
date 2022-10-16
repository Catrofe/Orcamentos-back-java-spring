package com.budgets.modules.security.service;

import com.budgets.modules.employee.entities.Employee;
import com.budgets.modules.employee.repository.EmployeeRepository;
import com.budgets.modules.user.entities.User;
import com.budgets.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String data){
        String[] newData = data.split(",");
        if (newData[1].equals("USER")){
            User user = repository.findById(Long.parseLong(newData[0])).orElseThrow(()-> new UsernameNotFoundException("Email Not Found "));
            return Optional.ofNullable(repository.findByEmail(user.getEmail()))
                    .orElseThrow(() -> new UsernameNotFoundException("Email Not Found "));

        } else{
            Employee employee = employeeRepository.findById(Long.parseLong(newData[0])).orElseThrow(()-> new UsernameNotFoundException("Email Not Found "));
            return Optional.ofNullable(employeeRepository.findByEmail(employee.getEmail()))
                    .orElseThrow(() -> new UsernameNotFoundException("Email Not Found "));
        }
    }
}
