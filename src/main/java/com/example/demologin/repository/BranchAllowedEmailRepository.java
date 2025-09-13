package com.example.demologin.repository;

import com.example.demologin.entity.Branch;
import com.example.demologin.entity.BranchAllowedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchAllowedEmailRepository extends JpaRepository<BranchAllowedEmail, Long> {
    
    List<BranchAllowedEmail> findByBranchAndActiveTrue(Branch branch);
    
    List<BranchAllowedEmail> findByBranchIdAndActiveTrue(Long branchId);
    
    Optional<BranchAllowedEmail> findByBranchAndEmail(Branch branch, String email);
    
    @Query("SELECT bae FROM BranchAllowedEmail bae WHERE bae.email = :email AND bae.active = true")
    List<BranchAllowedEmail> findByEmailAndActiveTrue(@Param("email") String email);
    
    @Query("SELECT bae.branch FROM BranchAllowedEmail bae WHERE bae.email = :email AND bae.active = true")
    List<Branch> findBranchesByEmailAndActiveTrue(@Param("email") String email);
    
    boolean existsByBranchAndEmailAndActiveTrue(Branch branch, String email);
}
