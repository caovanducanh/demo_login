# Tài khoản mặc định

Hệ thống khởi tạo sẵn 2 tài khoản mẫu:

- **Admin**
  - username: `admin`
  - password: `admin123`
  - role: `ADMIN`
- **Member**
  - username: `member`
  - password: `member123`
  - role: `MEMBER`

Bạn có thể dùng các tài khoản này để đăng nhập và test các API phân quyền.

# Authentication API

API xác thực với hỗ trợ đăng nhập theo chi nhánh (Branch-aware Authentication).

## 🏢 Branch-Aware Authentication

Hệ thống hỗ trợ đăng nhập Google OAuth2 theo chi nhánh.

### Quick Start OAuth2 Flow
1. **Lấy danh sách chi nhánh**: `GET /api/branches`
2. **Đăng nhập Google với branch**: `/oauth2/authorization/google?branch=HCM`

---

## 📋 Branch Management APIs

### 1. Get Available Branches
- **Endpoint:** `GET /api/branches`
- **Description:** Lấy danh sách tất cả chi nhánh active để hiển thị UI chọn chi nhánh
- **Response:**
```json
[
  {
    "id": 1,
    "name": "Ho Chi Minh Campus",
    "code": "HCM",
    "address": "590 Cach Mang Thang Tam, District 3, Ho Chi Minh City",
    "allowedEmails": [
      "anhcvdse182894@fpt.edu.vn",
      "teacher1@fpt.edu.vn",
      "admin.hcm@fpt.edu.vn"
    ],
    "active": true,
    "createdAt": "2025-09-13T10:30:00"
  },
  {
    "id": 2,
    "name": "Ha Noi Campus", 
    "code": "HN",
    "address": "Hoa Lac Hi-Tech Park, Km 29, Dai Mo, Thach That, Hanoi",
    "allowedEmails": [
      "student.hn@fpt.edu.vn",
      "teacher.hn@fpt.edu.vn",
      "admin.hn@fpt.edu.vn"
    ],
    "active": true,
    "createdAt": "2025-09-13T10:30:00"
  }
]
```

---

## 📋 Tài khoản mặc định

Hệ thống khởi tạo sẵn các tài khoản mẫu:

### Default Users
- **Admin**
  - username: `admin`
  - password: `admin123`
  - role: `ADMIN`
- **Member**
  - username: `member`
  - password: `member123`
  - role: `MEMBER`

### Test Users for Branches
- **HCM Branch**: `anhcvdse182894@fpt.edu.vn` (STUDENT role)
- **HN Branch**: Các email trong danh sách allowed

---

## 🌐 OAuth2 Google Authentication

### 1. Google OAuth2 Login với Branch
- **Endpoint:** `GET /oauth2/authorization/google?branch={branchCode}`
- **Description:** Khởi tạo Google OAuth2 flow với branch context
- **Parameters:**
  - `branch`: Mã chi nhánh (HCM, HN)
- **Example:** `http://localhost:8080/oauth2/authorization/google?branch=HCM`
- **Process:**
  1. User chọn chi nhánh trên frontend
  2. Frontend redirect trực tiếp tới `/oauth2/authorization/google?branch=HCM`
  3. Google OAuth2 flow
  4. Hệ thống validate email với branch trong URL parameter
  5. Tạo/cập nhật user với branch assignment
  6. Generate JWT token
  7. Redirect về frontend với token

### 2. OAuth2 Success Response
- **Endpoint:** `GET /login/oauth2/code/google` (Google callback)
- **Success:** Redirect về frontend với JWT token
- **Error:** Redirect về error page với message

### 3. Error Responses cho Frontend
Khi OAuth2 login thất bại, hệ thống sẽ redirect về frontend với error parameter:

#### Email không được phép cho chi nhánh
```
GET /login?error=Email_not_allowed_for_selected_branch
```

#### Chi nhánh không tồn tại
```
GET /login?error=Branch_not_found
```

#### Thiếu branch parameter
```  
GET /login?error=Branch_parameter_required
```

#### OAuth2 authentication failed
```
GET /login?error=OAuth2_authentication_failed
```

### 4. Test Accounts
- **HCM Branch**: `anhcvdse182894@fpt.edu.vn` (STUDENT role)
- **HN Branch**: Các email trong danh sách allowed của HN

---
