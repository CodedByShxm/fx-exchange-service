# 💱 FX Exchange Service

A **production-style Spring Boot FX microservice** that provides real-time currency conversion using external exchange rate providers, with caching, scheduled snapshot ingestion, and historical rate tracking.

---

## 🧠 Architecture Overview

This service uses a **multi-layer rate resolution strategy**:

Cache (Caffeine)

↓

Database (PostgreSQL snapshot)

↓

External FX API (fallback only)


A scheduled job maintains a **daily FX snapshot system** for consistency, performance, and reliability.

---

##  Key Features

### Currency Conversion
- Convert between any supported currencies
- Financial-grade precision using BigDecimal
- Deterministic daily exchange rates

---

###  Caching Layer
- Caffeine in-memory cache
- Reduces database load
- Sub-millisecond lookup performance

---

###  Persistence Layer
- PostgreSQL-backed storage
- Stores latest FX snapshot per currency pair
- Supports historical tracking and auditing

---

### Historical Rate Tracking
- Daily FX snapshots stored
- Audit trail of rate changes
- Enables time-based FX analysis

---

###  Scheduled FX Refresh
- Midnight daily refresh job
- Fetches latest rates from external API
- Updates DB + cache automatically

---

### Self-Healing Fallback System
- Cache → DB → API fallback chain
- Automatically persists missing rates
- Prevents repeated external API calls
- Ensures system resilience

---

##  Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Caffeine Cache
- Spring Scheduling
- REST APIs

---

## System Flow

###  Conversion Flow
Client
↓
Controller
↓
Service Layer
↓
Cache → DB → API (fallback)

---

###  Daily Snapshot Flow
Scheduler (00:00)
↓
External FX API
↓
Database (snapshot storage)
↓
Cache (warm-up)
↓
History Table (audit trail)

---

##  API Reference

###  Convert Currency

```http
POST /api/v1/conversions

Request
{
  "fromCurrency": "USD",
  "toCurrency": "JMD",
  "amount": 100
}

Response

{
  "fromCurrency": "USD",
  "toCurrency": "JMD",
  "amount": 100,
  "exchangeRate": 158.237664,
  "convertedAmount": 15823.7664,
  "timestamp": "2026-06-09T21:53:42"
}
```

#  Design Highlights

✔ Cache-first architecture for performance
✔ External API isolation
✔ Scheduled batch ingestion
✔ Historical audit trail
✔ Self-healing missing data recovery
✔ Thread-safe fallback handling
✔ Financial-grade precision handling

##  What This Project Demonstrates

This project showcases:

Backend system design (layered architecture)
Caching strategies and performance optimization
External API integration patterns
Scheduling and batch processing
Time-series financial data modeling
Fault-tolerant service design
Production-grade fallback strategies

## 🚀 Future Improvements
Redis distributed cache for scaling
Circuit breaker (Resilience4j)
Actuator metrics + Prometheus monitoring
Docker + Kubernetes deployment
Multi-provider FX aggregation
Authentication + rate limiting
CI/CD pipeline (GitHub Actions) 
Local Development (Optional)

### Run PostgreSQL
docker-compose up -d

### Run Spring Boot app
./mvnw spring-boot:run
📌 Summary

This service is designed as a real-world financial-grade backend system, demonstrating:

Reliable currency conversion
High-performance caching
Scheduled data ingestion
Historical FX tracking
Fault-tolerant architecture
