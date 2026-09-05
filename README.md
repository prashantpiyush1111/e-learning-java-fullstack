# E-Learning Platform

A full-stack e-learning platform built with Spring Boot and React, supporting Student, Instructor, and Admin workflows.

## Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security + JWT access/refresh tokens
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Lombok
- Bean Validation

### Frontend
- React + Vite
- React Router DOM
- Axios
- Context API
- Plain CSS

## Roles

- **STUDENT** — browse approved courses, enroll, watch lectures, track progress, attempt quizzes, submit assignments.
- **INSTRUCTOR** — create and manage courses, sections, lectures, quizzes, and assignments.
- **ADMIN** — review courses, approve/reject content, view platform statistics, and manage user status.

## Backend Modules

1. Authentication & User Management
2. Course Management
3. Sections & Lectures
4. Enrollment & Video Progress
5. Quizzes & Assignments
6. Admin Management

## Frontend Modules

1. Authentication and JWT session handling
2. Student dashboard, catalog, learning and progress
3. Instructor dashboard and course management
4. Admin dashboard and user management
5. Profile management
6. Role-aware navigation and logout

## Running the Backend

1. Create a MySQL database.
2. Configure the datasource and JWT settings in `backend/src/main/resources/application.properties`.
3. Start the application:

```bash
cd backend
mvn spring-boot:run
```

The API is expected at `http://localhost:8080/api` unless configured otherwise.

## Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

Optional API configuration:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Authentication

The frontend stores the access token and refresh token locally and automatically attempts token refresh after an unauthorized API response. Role-protected routes prevent users from entering another role's dashboard.

## Main API Areas

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/change-password
GET  /api/users/me
PUT  /api/users/me

/api/courses
/api/courses/{courseId}/sections
/api/sections/{sectionId}/lectures
/api/enrollments
/api/progress
/api/sections/{sectionId}/quizzes
/api/quizzes
/api/sections/{sectionId}/assignments
/api/assignments
/api/admin
```

## Project Structure

```text
e-learning-java-fullstack/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/elearning/backend/
└── frontend/
    ├── package.json
    ├── index.html
    └── src/
        ├── api/
        ├── components/
        ├── context/
        ├── pages/
        └── styles/
```

## Security Notes

- Passwords are handled using BCrypt on the backend.
- JWT authentication is required for protected APIs.
- Method-level role authorization is enabled.
- Students cannot access instructor/admin routes through the frontend or backend authorization rules.
- Instructors manage their own courses and course content.
- Admin operations are restricted to the ADMIN role.

## Example Flow

A student registers/logs in, opens the course catalog, enrolls in an approved course, watches lectures, and marks lectures complete. The backend recalculates course progress and the dashboard can show the updated completion percentage.

## Status

Core backend and role-based frontend modules are implemented. Before production deployment, configure secrets through environment/secret management and run the backend/frontend test and build pipelines against the target MySQL environment.
