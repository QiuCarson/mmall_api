package com.carson.mmall.repository;


import com.carson.mmall.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
     User findTopByUsername(String username);
     User findTopByEmail(String username);
}
