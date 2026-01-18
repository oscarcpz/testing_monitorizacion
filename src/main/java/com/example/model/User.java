package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private int age;

    public boolean isValid() {
        return name != null && !name.isEmpty() && 
               email != null && email.contains("@") && 
               age > 0 && age < 150;
    }
}
