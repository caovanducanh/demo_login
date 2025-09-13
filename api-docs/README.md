# 📚 API Documentation

Tài liệu API đầy đủ cho hệ thống School Management với OAuth2 Branch-Aware Authentication.

## 📋 Available APIs

### 🔐 Authentication & Authorization
- **[Authentication API](./authentication-api.md)** - Đăng nhập, đăng ký, OAuth2 Google/Facebook
- **[Branch Authentication API](./branch-authentication-api.md)** - ⭐ **NEW** - Xác thực theo chi nhánh

### 👥 User Management  
- **[Role API](./role-api.md)** - Quản lý vai trò người dùng
- **[Permission API](./permission-api.md)** - Quản lý quyền hạn hệ thống

### 🛡️ Security & Session
- **[Security Management API](./security-management-api.md)** - Quản lý bảo mật
- **[Session Management API](./session-management-api.md)** - Quản lý phiên đăng nhập

### 📝 Error Handling
- **[Error Response](./error-response.md)** - Cấu trúc response lỗi

---

## 🏢 Branch-Aware Authentication System

### Overview
Hệ thống xác thực theo chi nhánh cho phép:
- Người dùng chọn chi nhánh trước khi đăng nhập
- Kiểm soát email được phép truy cập cho từng chi nhánh
- OAuth2 Google integration với branch context

### Quick Start for Frontend

#### 1. Load Available Branches
```javascript
const response = await fetch('/api/branches');
const branches = await response.json();
```

#### 2. User Selects Branch  
```javascript
await fetch(`/api/branches/select?branchCode=HCM`, { method: 'POST' });
```

#### 3. Initiate OAuth2 Login
```javascript
const response = await fetch(`/api/branches/oauth2-url?branchCode=HCM`);
const oauth2Url = await response.json();
window.location.href = oauth2Url;
```

### Test Data
- **HCM Branch**: `anhcvdse182894@fpt.edu.vn` 
- **HN Branch**: `student.hn@fpt.edu.vn`

---

## 🛠️ Development Setup

### Base URL
```
http://localhost:8080
```

### Common Headers
```bash
Content-Type: application/json
Authorization: Bearer {jwt_token}  # For protected endpoints
```

### Public Endpoints (No Auth Required)
- `GET /api/branches` - Get available branches
- `POST /api/branches/select` - Select branch
- `GET /api/branches/validate-email` - Validate email for branch
- `GET /api/branches/oauth2-url` - Get OAuth2 URL
- `GET /oauth2/authorization/google` - Google OAuth2 login

### Protected Endpoints
- Most other APIs require JWT token
- Admin-only endpoints require ADMIN role

---

## 📖 API Documentation Standards

### Request Format
```json
{
  "field": "value",
  "required_field": "string",
  "optional_field": "string | null"
}
```

### Response Format
```json
{
  "statusCode": 200,
  "message": "Success message",
  "data": { /* response data */ }
}
```

### Error Format
```json
{
  "timestamp": "2025-09-13T10:30:00",
  "status": 400,
  "error": "Bad Request", 
  "message": "Detailed error message",
  "path": "/api/endpoint"
}
```

---

## 🧪 Testing

### Postman Collection
Import the API endpoints into Postman for easy testing.

### cURL Examples
Each API documentation includes cURL examples for quick testing.

### Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔄 API Updates

### v2.0 - Branch Authentication System (Current)
- ✅ Branch-aware OAuth2 authentication
- ✅ Email-based access control per branch
- ✅ Branch selection UI support
- ✅ Test data initialization

### v1.0 - Basic Authentication (Legacy)
- Traditional username/password login
- Basic OAuth2 Google/Facebook
- Role-based access control

---

## 📞 Support

For API questions or issues:
1. Check the specific API documentation
2. Review error response format
3. Test with provided cURL examples
4. Check Swagger UI for interactive testing
