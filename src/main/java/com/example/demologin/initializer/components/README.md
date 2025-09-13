# 🧩 Initialization Components

This folder contains modular initialization components that are orchestrated by the main `MainDataInitializer`.

## 📁 Components

### 🔑 PermissionRoleInitializer
- **Purpose**: Creates system permissions and roles
- **Dependencies**: None (runs first)
- **Output**: 28 permissions, 2 roles (ADMIN, MEMBER)

### 👥 DefaultUserInitializer  
- **Purpose**: Creates default system users
- **Dependencies**: PermissionRoleInitializer (needs roles)
- **Output**: admin and member users with assigned roles

### 🏢 BranchDataInitializer
- **Purpose**: Creates branch system data
- **Dependencies**: PermissionRoleInitializer (needs roles for test user)
- **Output**: HCM & HN branches, allowed emails, test user

## 🔄 Execution Order

1. **PermissionRoleInitializer** - Creates permissions and roles
2. **DefaultUserInitializer** - Creates users with roles
3. **BranchDataInitializer** - Creates branches and test user

## ➕ Adding New Components

1. Create your initializer in this folder
2. Add it to `MainDataInitializer` 
3. Ensure proper dependency order

Example:
```java
@Component
@RequiredArgsConstructor
@Slf4j
public class YourFeatureInitializer {
    
    @Transactional
    public void initializeYourFeature() {
        // Your initialization logic
    }
}
```

## 📋 Guidelines

- ✅ Use `@Component` for Spring dependency injection
- ✅ Use `@Transactional` for database operations
- ✅ Make operations idempotent (check if data exists)
- ✅ Provide detailed logging
- ✅ Handle dependencies properly
