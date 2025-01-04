package com.GSCOMP230.Student_Managment.repository;
import com.GSCOMP230.Student_Managment.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    User findByPhoneAndPassword(String phone, String password);
    List<User> findByRole(String role);
    Optional<User> findByEmail(String email);
    List<User> findAll();
//    List<User> findAllByRole(String role); // Find all users by role>

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);




    Optional<User>findByPhone(String phone);

    @Query("SELECT DISTINCT u.regYear FROM User u")
    List<Integer> findDistinctRegYears();

}