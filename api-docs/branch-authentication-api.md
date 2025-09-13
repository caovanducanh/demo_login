# Branch Authentication API

## Overview
Branch-aware authentication system allows users to select a specific branch/campus before logging in with Google OAuth2. The system validates that the user's email is allowed for the selected branch.

## Base URL
```
http://localhost:8080/api/branches
```

---

## 📋 Branch Selection & Management

### 1. Get Available Branches
**GET** `/api/branches`

Retrieves list of all active branches for frontend to display selection UI.

#### Request
```bash
curl -X GET "http://localhost:8080/api/branches" \
  -H "Content-Type: application/json"
```

#### Response
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
      "admin.hcm@fpt.edu.vn",
      "student1@fe.edu.vn",
      "teacher1@fe.edu.vn"
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
      "admin.hn@fpt.edu.vn",
      "student.hn@fe.edu.vn",
      "teacher.hn@fe.edu.vn"
    ],
    "active": true,
    "createdAt": "2025-09-13T10:30:00"
  }
]
```

---

### 2. Select Branch
**POST** `/api/branches/select`

Stores the selected branch in session for OAuth2 flow.

#### Request
```bash
curl -X POST "http://localhost:8080/api/branches/select?branchCode=HCM" \
  -H "Content-Type: application/json"
```

#### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| branchCode | string | Yes | Branch code (HCM, HN) |

#### Response
```json
{
  "id": 1,
  "name": "Ho Chi Minh Campus",
  "code": "HCM",
  "address": "590 Cach Mang Thang Tam, District 3, Ho Chi Minh City",
  "allowedEmails": ["anhcvdse182894@fpt.edu.vn", "..."],
  "active": true,
  "createdAt": "2025-09-13T10:30:00"
}
```

---

### 3. Validate Email for Branch
**GET** `/api/branches/validate-email`

Checks if an email is allowed for a specific branch.

#### Request
```bash
curl -X GET "http://localhost:8080/api/branches/validate-email?email=anhcvdse182894@fpt.edu.vn&branchCode=HCM"
```

#### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| email | string | Yes | Email to validate |
| branchCode | string | Yes | Branch code |

#### Response
```json
true
```

---

### 4. Generate OAuth2 URL
**GET** `/api/branches/oauth2-url`

Generates OAuth2 login URL with branch parameter.

#### Request
```bash
curl -X GET "http://localhost:8080/api/branches/oauth2-url?branchCode=HCM"
```

#### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| branchCode | string | Yes | Branch code |

#### Response
```json
"http://localhost:8080/oauth2/authorization/google?branch=HCM"
```

---

## 🔐 OAuth2 Authentication Flow

### 1. Google OAuth2 Login
**GET** `/oauth2/authorization/google`

Initiates Google OAuth2 login with branch context.

#### Request
```bash
# Direct browser access
http://localhost:8080/oauth2/authorization/google?branch=HCM
```

#### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| branch | string | Yes | Branch code (HCM, HN) |

#### Flow
1. User selects branch on frontend
2. Frontend calls `/api/branches/select?branchCode=HCM`
3. Frontend redirects to `/oauth2/authorization/google?branch=HCM`
4. Google OAuth2 flow
5. Success handler validates email against branch
6. Returns JWT token or error

---

## 🎯 Complete Frontend Integration Flow

### Step 1: Load Branches
```javascript
// Get available branches for selection UI
const response = await fetch('/api/branches');
const branches = await response.json();
// Display branches in UI for user selection
```

### Step 2: User Selects Branch
```javascript
// User clicks on HCM branch
const selectedBranch = 'HCM';

// Store selection in session
await fetch(`/api/branches/select?branchCode=${selectedBranch}`, {
  method: 'POST'
});
```

### Step 3: Initiate OAuth2 Login
```javascript
// Get OAuth2 URL with branch parameter
const response = await fetch(`/api/branches/oauth2-url?branchCode=${selectedBranch}`);
const oauth2Url = await response.json();

// Redirect to Google OAuth2
window.location.href = oauth2Url;
```

### Step 4: Handle OAuth2 Callback
After successful Google login, the system will:
1. Validate email against selected branch
2. Create/update user with branch assignment
3. Generate JWT token
4. Redirect to frontend with token

---

## 📧 Allowed Email Management (Admin Only)

### 1. Add Allowed Email
**POST** `/api/branches/{branchId}/allowed-emails`

#### Request
```bash
curl -X POST "http://localhost:8080/api/branches/1/allowed-emails?email=newstudent@fpt.edu.vn&description=New Student" \
  -H "Authorization: Bearer {jwt_token}"
```

#### Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| branchId | number | Yes | Branch ID |
| email | string | Yes | Email to add |
| description | string | No | Description for the email |

---

### 2. Remove Allowed Email
**DELETE** `/api/branches/{branchId}/allowed-emails`

#### Request
```bash
curl -X DELETE "http://localhost:8080/api/branches/1/allowed-emails?email=oldstudent@fpt.edu.vn" \
  -H "Authorization: Bearer {jwt_token}"
```

---

### 3. Get Allowed Emails
**GET** `/api/branches/{branchId}/allowed-emails`

#### Request
```bash
curl -X GET "http://localhost:8080/api/branches/1/allowed-emails" \
  -H "Authorization: Bearer {jwt_token}"
```

#### Response
```json
[
  "anhcvdse182894@fpt.edu.vn",
  "teacher1@fpt.edu.vn",
  "admin.hcm@fpt.edu.vn",
  "student1@fe.edu.vn",
  "teacher1@fe.edu.vn"
]
```

---

## 🧪 Testing

### Test Data
- **HCM Branch**: `anhcvdse182894@fpt.edu.vn` (Test Student)
- **HN Branch**: `student.hn@fpt.edu.vn`

### Test Scenarios

1. **Valid Email for Branch**
```bash
# Should return true
curl "http://localhost:8080/api/branches/validate-email?email=anhcvdse182894@fpt.edu.vn&branchCode=HCM"
```

2. **Invalid Email for Branch**
```bash
# Should return false
curl "http://localhost:8080/api/branches/validate-email?email=anhcvdse182894@fpt.edu.vn&branchCode=HN"
```

3. **Complete Login Flow**
```bash
# 1. Select branch
curl -X POST "http://localhost:8080/api/branches/select?branchCode=HCM"

# 2. Login via browser
# Navigate to: http://localhost:8080/oauth2/authorization/google?branch=HCM
# Login with: anhcvdse182894@fpt.edu.vn
```

---

## ⚠️ Error Handling

### Common Errors

#### Email Not Allowed
```json
{
  "timestamp": "2025-09-13T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email not allowed for selected branch",
  "path": "/oauth2/login/oauth2/code/google"
}
```

#### Branch Not Found
```json
{
  "timestamp": "2025-09-13T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Branch not found with code: INVALID",
  "path": "/api/branches/select"
}
```

#### Missing Branch Parameter
```json
{
  "timestamp": "2025-09-13T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Branch parameter is required for OAuth2 login",
  "path": "/oauth2/authorization/google"
}
```
