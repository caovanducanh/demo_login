## Base URL
http://localhost:8080

### 1. Create Grade

**Endpoint**

```POST /grades```


**Request Body**

```
{
    "name": "Grade 9",
    "description": "Secondary level"
}
```


**Response**

```json
{
    "id": 1,
    "name": "Grade 9",
    "description": "Secondary level"
}
```

2. Get All Grades

Endpoint

GET /grades


Response

```json
[
    {
        "id": 1,
        "name": "Grade 9",
        "description": "Secondary level"
    },
    {
        "id": 2,
        "name": "Grade 10",
        "description": "Higher secondary level"
    }
]
```

### 3. Get Grade by ID

**Endpoint**

```GET /grades/{id}```


**Response**

```json
{
    "id": 1,
    "name": "Grade 9",
    "description": "Secondary level"
}
```

### 4. Update Grade

**Endpoint**

```PUT /grades/{id}```


**Request Body**

```json
{
    "name": "Grade 9 Advanced",
    "description": "Updated description"
}
```


**Response**

```json
{
    "id": 1,
    "name": "Grade 9 Advanced",
    "description": "Updated description"
}
```
### 5. Delete Grade

**Endpoint**

```DELETE /grades/{id}```


**Response**

<span style="color:green">204 No Content</span>