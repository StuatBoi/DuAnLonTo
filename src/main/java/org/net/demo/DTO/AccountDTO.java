package org.net.demo.DTO;

public class AccountDTO {

    private Long id;

    private String username;

    private String email;

    private String role;
    private Double balance;
    
    public AccountDTO() {
    }

    public AccountDTO(Long id, String username, String email, String role, Double balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
