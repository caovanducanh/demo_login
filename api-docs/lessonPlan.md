## Base URL
```http://localhost:8080```

### 1. Create Lesson Plan

#### Endpoint

```POST /lesson-plans```


**Request Body**

```json
{
    "teacherId": 1,
    "gradeId": 2,
    "title": "Lesson Plan for Math",
    "content": "This is the content"
}
```

**Response**

```json
{
    "id": 10,
    "teacherId": 1,
    "gradeId": 2,
    "title": "Lesson Plan for Math",
    "content": "This is the content",
    "filePath": "lessonplans/id/base.json",
    "createdAt": "2025-09-25T10:30:00",
    "updatedAt": "2025-09-25T10:30:00"
}
```

### 2. Real-Time Edits (via WebSocket)

#### Endpoint (WebSocket)

```ws://localhost:8080/ws/lesson-plans/{id}```


**Message (Server → Client)**

```json
{
    "lessonPlanId": 10,
    "operation": "insert",
    "position": 25,
    "value": "New sentence."
}
```


**Message (Server → Client)**

```json
{
    "lessonPlanId": 10,
    "operation": "insert",
    "position": 25,
    "value": "New sentence.",
    "timestamp": "2025-09-25T10:31:15"
}
```

### 3. Trigger Compaction (Manual Save by Teacher)

**Endpoint**

```POST /lesson-plans/{id}/save```


**Response (Success)**

<span style="color:green">200 OK</span>.
Lesson plan 10 compacted successfully.


**Response (Failure)**

<span style="color:red">**500 Internal Server
Failed to compact lesson plan 10: <error message>**</span>.



### 4. Get Lesson Plan by ID

**Endpoint**

```GET /lesson-plans/{id}```


**Response**

```
{
    "id": 10,
    "teacherId": 1,
    "gradeId": 2,
    "title": "Lesson Plan for Math",
    "content": "This is the content with merged edits",
    "filePath": "lessonplans/uuid-math-plan.txt",
    "createdAt": "2025-09-25T10:30:00",
    "updatedAt": "2025-09-25T10:35:00"
}
```