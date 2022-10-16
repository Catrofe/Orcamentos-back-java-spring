package com.budgets.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDTO implements EditUserInterface {
    private String name;
    private String email;
    private String password;
    private String phone;
}
