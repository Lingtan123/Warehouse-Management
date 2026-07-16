# Warehouse Management

This repository contains a warehouse management system with separate backend and frontend projects:

- `wms`: Spring Boot 3 + MyBatis-Plus backend
- `wms-web`: Vue 2 + Element UI frontend

## Project Structure

```text
WarehouseManagement/
|- wms/
`- wms-web/
```

## Requirements

- Java 17
- Maven 3.9+
- Node.js 16+ or 18+
- MySQL 8.x

## Backend Setup

1. Open the backend directory:

```bash
cd wms
```

2. Use the example file as a template for your local config:

```bash
copy src\\main\\resources\\application-local.properties.example src\\main\\resources\\application.properties
```

3. Edit `src/main/resources/application.properties` and set the values for your local database.

4. Start the backend:

```bash
mvn spring-boot:run
```

Default backend port: `8090`

## Frontend Setup

```bash
cd wms-web
npm install
npm run serve
```

## Before Pushing To GitHub

- Do not commit real database passwords or other secrets.
- Keep local-only settings in ignored files such as `application.properties`.
- Review changes before each commit:

```bash
git status
git diff
```

## Notes

If a real password has already been pushed to GitHub, change that password first. If the repository is public, consider cleaning the Git history as well.
