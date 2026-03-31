package com.elearning.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.backend.Entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByEmail(String email);

}
