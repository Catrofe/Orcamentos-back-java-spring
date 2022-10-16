package com.budgets.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
}
