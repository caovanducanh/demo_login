package com.example.demologin.dto.request.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBranchRequest {
    
    @NotBlank(message = "Branch name is required")
    private String name;
    
    @NotBlank(message = "Branch code is required")
    private String code;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotNull(message = "Active status is required")
    private Boolean active = true;
}
