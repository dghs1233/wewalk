package com.spring.boot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.boot.config.DataNotFoundException;
import com.spring.boot.dao.UserRepository;
import com.spring.boot.model.SiteUser;
import com.spring.boot.model.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	//private final BaseAuthUserRepository baseAuthUserRepository;
	
	private final UserRepository userRepository;
	
	//BCrypt해시 함수 호출
	private final PasswordEncoder passwordEncoder;
	
	//id 생성 메소드
	public SiteUser create(UserRole role, String email, String password, String userName,  
							String name, LocalDate birthDate, String postcode, String address, 
							String detailAddress, String tel) {
		
		SiteUser user = new SiteUser();
		
		user.setRole(role);
		user.setEmail(email);
		
		//암호화 처리(이미 Bcrypt를 통해 salt 적용됨)
		user.setPassword(passwordEncoder.encode(password));
		
		user.setUserName(userName);
		user.setName(name);
		user.setCreatedDate(LocalDateTime.now());
		user.setBirthDate(birthDate);
		user.setPostcode(postcode);
		user.setAddress(address);
		user.setDetailAddress(detailAddress);
		user.setTel(tel);
		
		//회원정보 db에 저장
		userRepository.save(user);
		
		return user;
		
	}
	
	//이메일로 불러오기
	public SiteUser getUserByEmail(String email) {
		
		Optional<SiteUser> siteUser = 
				userRepository.findByEmail(email);
		
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("User not found!");
		}
		
	}
	

	public Optional<SiteUser> getUserById(Long id) {
		
        return userRepository.findById(id);
    }
	

	//userName으로 불러오기
	public SiteUser getUserByUserName(String userName) {
			
		Optional<SiteUser> siteUser = 
				userRepository.findByUserName(userName);
			
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("User not found!");
		}
			
	}
		
	
	//비밀번호 업데이트
	public void updatePassword(SiteUser user, String newPassword) {
		
        // 새로운 비밀번호를 암호화하여 설정합니다.
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        // 사용자 정보를 업데이트합니다.
        userRepository.save(user);
    }
	
	//회원 탈퇴
	 public void deleteUserById(Long userId) {
	        userRepository.deleteById(userId);
	    }

}