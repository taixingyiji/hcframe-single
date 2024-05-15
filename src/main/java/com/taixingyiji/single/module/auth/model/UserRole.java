package com.taixingyiji.single.module.auth.model;

/**
 * @author zyf
 */
public class UserRole {

    private String creditCode;
    private int roleType;

    public UserRole() {

    }

    public UserRole(String creditCode, int roleType) {
        this.creditCode = creditCode;
        this.roleType = roleType;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

}
