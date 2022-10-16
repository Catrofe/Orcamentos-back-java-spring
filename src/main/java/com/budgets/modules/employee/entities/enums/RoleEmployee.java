package com.budgets.modules.employee.entities.enums;

public enum RoleEmployee {
    ADMIN(1),
    MANAGER(2),
    EMPLOYEE(3);

    private int code;

    private RoleEmployee(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RoleEmployee valueOf(int code) {
        for (RoleEmployee value : RoleEmployee.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid RoleEmployee code");
    }
}
