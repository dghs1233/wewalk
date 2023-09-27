package com.spring.boot.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.boot.dto.Pay;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long>{
	
	Pay findByUserId(int userId);
    
}