package com.spring.boot.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class SellerRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//신청자 id
	@ManyToOne
	private SiteUser siteUser;
	
	//판매자 소개
	private String intro;
	
	//신청일시
	private LocalDateTime requestTime;
	
	//처리여부
	private boolean isProcessed;
	
	//처리일시
	private LocalDateTime processedTime;
	
	//set하기 위한 builder
  	@Builder
    public SellerRequest(SiteUser siteUser, String intro, 
    		LocalDateTime requestTime, boolean isProcessed, 
    		LocalDateTime processedTime) {
  		
  		this.siteUser = siteUser;
  		this.intro = intro;
  		this.requestTime = requestTime;
        this.isProcessed = isProcessed;
        this.processedTime = processedTime;
        
    }
	
}