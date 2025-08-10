# Phantom Mask API Documentation

## Overview

The Phantom Mask API provides endpoints for managing pharmacy data, user transactions, mask inventory, and analytics. All endpoints return JSON responses with a standardized format.

### Base URL
```
http://localhost:8080/api/1.0
```

### Response Format
All successful responses follow this format:
```json
{
  "data": [response_data]
}
```

Error responses follow this format:
```json
{
  "error": "Error message description"
}
```

---

## 1. Pharmacy APIs

### 1.1 Get Open Pharmacies

Retrieve pharmacies that are open at a specific time and optionally on a specific day.

**Endpoint:** `GET /pharmacy/open`

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `time` | string | **Required.** Time in HH:mm format (e.g., "14:30") |
| `dayOfWeek` | string | **Optional.** Day of the week (Mon, Tue, Wed, Thu, Fri, Sat, Sun). Defaults to current day if not provided |

**Example Request:**
```
GET /pharmacy/open?time=14:30&dayOfWeek=Mon
```

**Example Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "DFW Wellness",
      "todayHours": "08:00 - 12:00",
      "closesIn": "2 hours 30 minutes"
    }
  ]
}
```

### 1.2 Get Masks by Pharmacy

Retrieve all masks available in a specific pharmacy with sorting options.

**Endpoint:** `GET /pharmacy/{pharmacyId}/masks`

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `pharmacyId` | integer | **Required.** The ID of the pharmacy |

**Query Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `sortBy` | string | **Optional.** Field to sort by: "name" or "price". Default: "name" |
| `order` | string | **Optional.** Sort order: "asc" or "desc". Default: "desc" |

**Example Request:**
```
GET /pharmacy/1/masks?sortBy=price&order=asc
```

**Example Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "True Barrier (green) (3 per pack)",
      "price": 13.70
    },
    {
      "id": 2,
      "name": "MaskT (green) (10 per pack)",
      "price": 41.86
    }
  ]
}
```

### 1.3 Filter Pharmacies by Mask Count

Get pharmacies filtered by mask price range and count threshold.

**Endpoint:** `GET /pharmacy`

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `minPrice` | decimal | **Optional.** Minimum mask price. Default: 0 |
| `maxPrice` | decimal | **Optional.** Maximum mask price. No limit if not provided |
| `threshold` | integer | **Optional.** Mask count threshold. Default: 0 |
| `comparison` | string | **Optional.** Comparison operator: "more" or "less". Default: "more" |

**Example Request:**
```
GET /pharmacy?minPrice=10&maxPrice=50&threshold=5&comparison=more
```

**Example Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "DFW Wellness",
      "cashBalance": 328.41,
      "matchedMasksCount": 7
    }
  ]
}
```

---

## 2. User APIs

### 2.1 Get Top Users by Transaction Amount

Retrieve top users ranked by total transaction amount within a date range.

**Endpoint:** `GET /user/top-users`

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `startDate` | string | **Required.** Start date in ISO 8601 format (e.g., "2021-01-01") |
| `endDate` | string | **Required.** End date in ISO 8601 format (e.g., "2021-01-31") |
| `threshold` | integer | **Optional.** Maximum number of users to return. Default: 1 |

**Example Request:**
```
GET /user/top-users?startDate=2021-01-01&endDate=2021-01-31&threshold=10
```

**Example Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "Yvonne Guerrero",
      "cashBalance": 191.83,
      "totalSpent": 135.83
    },
    {
      "id": 2,
      "name": "Ada Larson",
      "cashBalance": 978.49,
      "totalSpent": 85.67
    }
  ]
}
```

---

## 3. Analytics APIs

### 3.1 Get Mask Transaction Statistics

Calculate total mask count and transaction value within a date range.

**Endpoint:** `GET /analytics/masks/transaction-stats`

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `startDate` | string | **Required.** Start date in ISO 8601 format (e.g., "2021-01-01") |
| `endDate` | string | **Required.** End date in ISO 8601 format (e.g., "2021-01-31") |

**Example Request:**
```
GET /analytics/masks/transaction-stats?startDate=2021-01-01&endDate=2021-01-31
```

**Example Response:**
```json
{
  "data": {
    "totalMaskCount": 1250,
    "totTransactionAmount": 15420.75
  }
}
```

---

## 4. Search APIs

### 4.1 Search Stores and Masks

Search for pharmacies or masks by name with relevance ranking.

**Endpoint:** `GET /search`

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `type` | string | **Optional.** Search type: "store" or "mask". Default: "mask" |
| `q` | string | **Required.** Search query string |
| `limit` | integer | **Optional.** Maximum results to return (max: 100). Default: 20 |
| `offset` | integer | **Optional.** Number of results to skip for pagination. Default: 0 |

**Example Request (Store Search):**
```
GET /search?type=store&q=health&limit=10&offset=0
```

**Example Response (Store Search):**
```json
{
  "data": [
    {
      "id": 6,
      "name": "Health Mart"
    },
    {
      "id": 7,
      "name": "Health Warehouse"
    }
  ]
}
```

**Example Request (Mask Search):**
```
GET /search?type=mask&q=cotton&limit=5
```

**Example Response (Mask Search):**
```json
{
  "data": [
    {
      "id": 15,
      "name": "Cotton Kiss (black) (10 per pack)",
      "price": 16.31,
      "storeId": 4,
      "storeName": "Welltrack"
    },
    {
      "id": 16,
      "name": "Cotton Kiss (green) (10 per pack)",
      "price": 46.07,
      "storeId": 5,
      "storeName": "Prescription Hope"
    }
  ]
}
```

---

## 5. Purchase APIs

### 5.1 Purchase Masks

Process a user's mask purchase from multiple pharmacies in an atomic transaction.

**Endpoint:** `POST /purchase/masks`

**Request Body:**

| Field | Type | Description |
|-------|------|-------------|
| `userId` | integer | **Required.** ID of the purchasing user |
| `items` | array | **Required.** Array of purchase items |
| `items[].maskId` | integer | **Required.** ID of the mask to purchase |
| `items[].quantity` | integer | **Required.** Quantity to purchase (must be > 0) |

**Example Request:**
```json
{
  "userId": 1,
  "items": [
    {
      "maskId": 1,
      "quantity": 2
    },
    {
      "maskId": 5,
      "quantity": 1
    }
  ]
}
```

**Example Response:**
```json
{
  "data": {
    "message": "Purchase completed successfully",
    "totalAmount": 46.80,
    "remainingBalance": 145.03,
    "purchaseDateTime": "2025-01-15T14:30:15.123456",
    "purchasedItems": [
      {
        "maskId": 1,
        "maskName": "True Barrier (green) (3 per pack)",
        "pharmacyName": "DFW Wellness",
        "quantity": 2,
        "unitPrice": 13.70,
        "totalPrice": 27.40
      },
      {
        "maskId": 5,
        "maskName": "Masquerade (green) (3 per pack)",
        "pharmacyName": "DFW Wellness",
        "quantity": 1,
        "unitPrice": 9.40,
        "totalPrice": 9.40
      }
    ]
  }
}
```

---

## Error Handling

### Common Error Responses

**400 Bad Request:**
```json
{
  "error": "Invalid date format. Please use ISO 8601 Date format (e.g., 2025-01-01)."
}
```

**400 Bad Request (Purchase):**
```json
{
  "error": "Insufficient funds. Required: 100.00, Available: 50.00"
}
```

**400 Bad Request (Validation):**
```json
{
  "error": "Invalid time format. Please use HH:mm."
}
```

### HTTP Status Codes

| Status Code | Description                                              |
| ----------- | -------------------------------------------------------- |
| 200         | Success                                                  |
| 400         | Bad Request - Invalid parameters or business logic error |