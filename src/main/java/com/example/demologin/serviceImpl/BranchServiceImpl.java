package com.example.demologin.serviceImpl;

import com.example.demologin.dto.request.branch.CreateBranchRequest;
import com.example.demologin.dto.request.branch.UpdateBranchRequest;
import com.example.demologin.dto.response.BranchResponse;
import com.example.demologin.entity.Branch;
import com.example.demologin.entity.BranchAllowedEmail;
import com.example.demologin.exception.exceptions.BusinessException;
import com.example.demologin.exception.exceptions.NotFoundException;
import com.example.demologin.mapper.BranchMapper;
import com.example.demologin.repository.BranchRepository;
import com.example.demologin.repository.BranchAllowedEmailRepository;
import com.example.demologin.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {
    
    private final BranchRepository branchRepository;
    private final BranchAllowedEmailRepository branchAllowedEmailRepository;
    
    @Override
    public List<BranchResponse> getAllActiveBranches() {
        return branchRepository.findByActiveTrue()
                .stream()
                .map(BranchMapper::toBranchResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));
        return BranchMapper.toBranchResponse(branch);
    }
    
    @Override
    public BranchResponse getBranchByCode(String code) {
        Branch branch = branchRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Branch not found with code: " + code));
        return BranchMapper.toBranchResponse(branch);
    }
    
    @Override
    public BranchResponse createBranch(CreateBranchRequest request) {
        // Check if branch code already exists
        if (branchRepository.findByCode(request.getCode()).isPresent()) {
            throw new BusinessException("Branch code already exists: " + request.getCode());
        }
        
        // Check if branch name already exists
        if (branchRepository.findByName(request.getName()).isPresent()) {
            throw new BusinessException("Branch name already exists: " + request.getName());
        }
        
        Branch branch = BranchMapper.toEntity(request);
        branch = branchRepository.save(branch);
        
        log.info("Created new branch: {} with code: {}", branch.getName(), branch.getCode());
        return BranchMapper.toBranchResponse(branch);
    }
    
    @Override
    public BranchResponse updateBranch(Long id, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));
        
        // Check if the new name conflicts with another branch
        branchRepository.findByName(request.getName())
                .ifPresent(existingBranch -> {
                    if (!existingBranch.getId().equals(id)) {
                        throw new BusinessException("Branch name already exists: " + request.getName());
                    }
                });
        
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setActive(request.getActive());
        
        branch = branchRepository.save(branch);
        
        log.info("Updated branch: {} with id: {}", branch.getName(), branch.getId());
        return BranchMapper.toBranchResponse(branch);
    }
    
    @Override
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));
        
        // Set inactive instead of deleting to preserve data integrity
        branch.setActive(false);
        branchRepository.save(branch);
        
        log.info("Deactivated branch: {} with id: {}", branch.getName(), branch.getId());
    }
    
    @Override
    public boolean isEmailAllowedForBranch(String email, Long branchId) {
        return branchAllowedEmailRepository.existsByBranchAndEmailAndActiveTrue(
                branchRepository.findById(branchId).orElse(null), email);
    }
    
    @Override
    public Branch findBranchByAllowedEmail(String email) {
        return branchRepository.findByAllowedEmail(email)
                .orElse(null);
    }
    
    @Override
    public boolean validateEmailForBranch(String email, String branchCode) {
        Branch branch = branchRepository.findByCode(branchCode)
                .orElse(null);
        
        if (branch == null || !branch.getActive()) {
            log.warn("Branch not found or inactive: {}", branchCode);
            return false;
        }
        
        boolean isAllowed = branchAllowedEmailRepository.existsByBranchAndEmailAndActiveTrue(branch, email);
        log.debug("Email {} validation for branch {}: {}", email, branchCode, isAllowed);
        
        return isAllowed;
    }
    
    @Override
    public Branch getBranchEntityByCode(String code) {
        return branchRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Branch not found with code: " + code));
    }
    
    @Override
    public void addAllowedEmailToBranch(Long branchId, String email, String description) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + branchId));
        
        // Check if email already exists for this branch
        if (branchAllowedEmailRepository.findByBranchAndEmail(branch, email).isPresent()) {
            throw new BusinessException("Email already exists for this branch: " + email);
        }
        
        BranchAllowedEmail allowedEmail = BranchAllowedEmail.builder()
                .branch(branch)
                .email(email)
                .description(description)
                .active(true)
                .build();
        
        branchAllowedEmailRepository.save(allowedEmail);
        log.info("Added allowed email {} to branch {}", email, branch.getCode());
    }
    
    @Override
    public void removeAllowedEmailFromBranch(Long branchId, String email) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + branchId));
        
        BranchAllowedEmail allowedEmail = branchAllowedEmailRepository.findByBranchAndEmail(branch, email)
                .orElseThrow(() -> new NotFoundException("Allowed email not found: " + email));
        
        // Set inactive instead of deleting
        allowedEmail.setActive(false);
        branchAllowedEmailRepository.save(allowedEmail);
        
        log.info("Removed allowed email {} from branch {}", email, branch.getCode());
    }
    
    @Override
    public List<String> getAllowedEmailsForBranch(Long branchId) {
        return branchAllowedEmailRepository.findByBranchIdAndActiveTrue(branchId)
                .stream()
                .map(BranchAllowedEmail::getEmail)
                .collect(Collectors.toList());
    }
}
