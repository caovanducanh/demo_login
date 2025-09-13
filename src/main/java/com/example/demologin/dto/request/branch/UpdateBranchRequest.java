package com.example.demologin.dto.request.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBranchRequest {
    
    @NotBlank(message = "Branch name is required")
    private String name;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotNull(message = "Active status is required")
    private Boolean active;
}
