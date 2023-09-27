package com.spring.boot.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Shipping {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ano")
	    private Integer ano;
	    
	    @ManyToOne
	    @JoinColumn(name = "id") // 이 부분이 FK로 User 테이블의 id와 연결됩니다.
	    private User user;

	    @Column(name = "type", length = 30, nullable = false)
	    private String type;

	    @Column(name = "receivername", length = 30, nullable = false)
	    private String receiverName;

	    @Column(name = "phone", length = 50, nullable = false)
	    private String phone;

	    @Column(name = "address01", length = 100, nullable = false)
	    private String address01;

	    @Column(name = "address02", length = 200, nullable = false)
	    private String address02;

	    @Column(name = "zipcode", length = 30, nullable = false)
	    private String zipcode;


}
