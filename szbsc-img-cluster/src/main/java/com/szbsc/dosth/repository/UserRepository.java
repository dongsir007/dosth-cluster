package com.szbsc.dosth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.szbsc.dosth.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}