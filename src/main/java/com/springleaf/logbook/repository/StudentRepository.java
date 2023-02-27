package com.springleaf.logbook.repository;

import com.springleaf.logbook.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Modifying
    @Query(value = "update Student set Address = ?1 where Id = ?2")
    public Integer updateAddressById(String address, Integer Id);
}
