# FX Exchange Service

A Spring Boot service that provides real-time currency conversion using external FX APIs with caching and scheduled rate updates.

## Features
- Currency conversion API
- External FX provider integration
- Daily rate caching
- PostgreSQL persistence
- Scheduled rate refresh
- Clean layered architecture

## Tech Stack
- Java 21
- Spring Boot 3
- PostgreSQL
- Caffeine Cache
- REST API

## API Example

POST /api/v1/conversions
