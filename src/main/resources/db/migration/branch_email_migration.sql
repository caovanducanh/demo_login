-- Migration to update branch system from email domains to specific allowed emails
-- This script should be run when updating from domain-based to email-based validation

-- 1. Create the branch_allowed_emails table
CREATE TABLE IF NOT EXISTS branch_allowed_emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE,
    UNIQUE KEY unique_branch_email (branch_id, email)
);

-- 2. Remove the email_domains column from branches table (optional - for cleanup)
-- ALTER TABLE branches DROP COLUMN email_domains;

-- 3. Insert sample allowed emails for existing branches
INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'anhcvdse182894@fpt.edu.vn', 'Test Student', TRUE
FROM branches b WHERE b.code = 'HCM';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'teacher1@fpt.edu.vn', 'Teacher Account', TRUE
FROM branches b WHERE b.code = 'HCM';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'admin.hcm@fpt.edu.vn', 'Admin Account', TRUE
FROM branches b WHERE b.code = 'HCM';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'student1@fe.edu.vn', 'Student Account', TRUE
FROM branches b WHERE b.code = 'HCM';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'teacher1@fe.edu.vn', 'Teacher Account', TRUE
FROM branches b WHERE b.code = 'HCM';

-- For HN branch
INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'student.hn@fpt.edu.vn', 'Student Account', TRUE
FROM branches b WHERE b.code = 'HN';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'teacher.hn@fpt.edu.vn', 'Teacher Account', TRUE
FROM branches b WHERE b.code = 'HN';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'admin.hn@fpt.edu.vn', 'Admin Account', TRUE
FROM branches b WHERE b.code = 'HN';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'student.hn@fe.edu.vn', 'Student Account', TRUE
FROM branches b WHERE b.code = 'HN';

INSERT IGNORE INTO branch_allowed_emails (branch_id, email, description, active) 
SELECT b.id, 'teacher.hn@fe.edu.vn', 'Teacher Account', TRUE
FROM branches b WHERE b.code = 'HN';
