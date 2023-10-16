package com.spring.boot.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.boot.dto.OAuthUserCreateForm;
import com.spring.boot.dto.PrincipalDetails;
import com.spring.boot.dto.UserCreateForm;
import com.spring.boot.model.SiteUser;
import com.spring.boot.model.UserRole;
import com.spring.boot.service.UserService;

import lombok.RequiredArgsConstructor;

/*
	세션 활용법 : 
	1. controller에서 mapping할 때 매개변수에 @AuthenticationPrincipal PrincipalDetails principalDetails 추가
	2. principalDetails.getUser를 하면 각 소셜 로그인 시 받아온 attributes 활용 가능
	3. principalDetails.getEmail(), getUsername() 등과 같은 데이터 받아올 수 있음(PrincipalDetails 클래스 참조)
	
 */

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

	private final HttpSession httpSession;
	private final UserService userService;
	
	
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		
		return "signup_form";
		
	}
	
	@GetMapping("/oauthSignup")
	public String oauthSignup(OAuthUserCreateForm oAuthUserCreateForm, 
			@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		if(principalDetails != null && principalDetails.getUser() != null && principalDetails.getUser().getProvider() != null && !principalDetails.getUser().getProvider().isEmpty()) {
			
			oAuthUserCreateForm.setUserName(principalDetails.getUsername());
			oAuthUserCreateForm.setEmail(principalDetails.getEmail());
			
			SecurityContextHolder.clearContext(); // 로그아웃
			model.addAttribute("oAuthUserCreateForm", oAuthUserCreateForm);
			
			return "oauth_signup_form";
		}else {
			return "redirect:/";
		}
		
	}
	
	@PostMapping("/signup")
	//@PreAuthorize("isAnonymous()") // 로그인되지 않은 사용자에게만 허용
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindResult) {
		
		//입력값 검증
		if(bindResult.hasErrors()) {
			bindResult.reject("signupFailed", "유효성 검증에 실패했습니다");
			return "signup_form";
			
		}
		
		//패스워드1과 2가 일치하는지 검증
		if(!userCreateForm.getPassword1()
				.equals(userCreateForm.getPassword2()) ) {
			bindResult.rejectValue("password2", "passwordInCorrect",
					"2개의 패스워드가 일치하지 않습니다!");
			
			return "signup_form";
		}
		
		//입력값 DB에 넣으면서 검증(서버사이드)
		try {
			
			//날짜 검증 및 날짜 형식으로 변환
			int year = Integer.parseInt(userCreateForm.getBirthYear());
		    int month = Integer.parseInt(userCreateForm.getBirthMonth());
		    int day = Integer.parseInt(userCreateForm.getBirthDay());

		    LocalDate birthDate = LocalDate.of(year, month, day);
		    
		    UserRole role;
		    boolean seller;
		    //static에 있는 이미지 사용하기 위한 코드(사진 기본값)
		    Resource resource = new ClassPathResource("static/images/flower-8173829_640.jpg");
		    String picture = "/images/flower-8173829_640.jpg";
		    
		    //이메일 주소가 admin@wewalkpay.com이면 role에 ADMIN을 주고 아니면 USER
			if(userCreateForm.getUserName() == "wewalkpay" || "wewalkpay".equals(userCreateForm.getUserName())) {
				role = UserRole.ADMIN;
				seller = true;
			}else {
				role = UserRole.USER;
				seller = false;
			}
			
		    //UserRole을 지정해서 넣어줘야 하고 거기에 추가로 UserCreateForm과 UserService, SiteUser에서의 데이터 입력 순서를 맞춰줘야 함
			userService.create(role, userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getUserName(), 
						userCreateForm.getName(), birthDate, userCreateForm.getPostcode(),
						userCreateForm.getAddress(), userCreateForm.getDetailAddress(), userCreateForm.getTel(), picture, seller);
		
		} catch (DateTimeException e) {
		    // 유효하지 않은 날짜
			e.printStackTrace();
			bindResult.reject("signupFailed", "유효하지 않은 날짜입니다");
			return "signup_form";
			
		} catch (DataIntegrityViolationException e) {
			
			e.printStackTrace();
			bindResult.reject("signupFailed", "이미 등록된 사용자입니다");
			
			return "signup_form";
			
		} catch (Exception e) {
			
			e.printStackTrace();
			bindResult.reject("signupFailed", e.getMessage());
			
			return "signup_form";
			
		}
		return "redirect:/auth/login";
	}
	
	@PostMapping("/oauthSignup")
	//@PreAuthorize("isAnonymous()") // 로그인되지 않은 사용자에게만 허용
	public String oauthSignup(@Valid OAuthUserCreateForm oAuthUserCreateForm, BindingResult bindResult) {
		
		//입력값 검증
		if(bindResult.hasErrors()) {
			bindResult.reject("signupFailed", "유효성 검증에 실패했습니다");
			return "oauth_signup_form";
			
		}
		
		//입력값 DB에 넣으면서 검증(서버사이드)
		try {
			
			SiteUser oauthUser = userService.getUserByUserName(oAuthUserCreateForm.getUserName());
			
			UserRole role = UserRole.USER;
			String name = oAuthUserCreateForm.getName();

			//날짜 검증 및 날짜 형식으로 변환
			int year = Integer.parseInt(oAuthUserCreateForm.getBirthYear());
		    int month = Integer.parseInt(oAuthUserCreateForm.getBirthMonth());
		    int day = Integer.parseInt(oAuthUserCreateForm.getBirthDay());

		    LocalDate birthDate = LocalDate.of(year, month, day);    		    		    
		    LocalDateTime createdDate = LocalDateTime.now();
		    String postcode = oAuthUserCreateForm.getPostcode();
		    String address = oAuthUserCreateForm.getAddress();
		    String detailAddress = oAuthUserCreateForm.getDetailAddress();
		    String tel = oAuthUserCreateForm.getTel();
		    
		    userService.oauthSignup(oauthUser, role, name, 
		    		birthDate, createdDate, postcode, address, detailAddress, tel);		    
			
		} catch (DateTimeException e) {
		    // 유효하지 않은 날짜
			e.printStackTrace();
			bindResult.reject("signupFailed", "유효하지 않은 날짜입니다");
			return "oauth_signup_form";
			
		} catch (DataIntegrityViolationException e) {
			
			e.printStackTrace();
			bindResult.reject("signupFailed", "이미 등록된 사용자입니다");
			
			return "oauth_signup_form";
			
		} catch (Exception e) {
			
			e.printStackTrace();
			bindResult.reject("signupFailed", e.getMessage());
			
			return "oauth_signup_form";
			
		}
		return "redirect:/auth/login";
	}
	
	@GetMapping("/checkUserName")
	public ResponseEntity<Map<String, Object>> checkUserName(@RequestParam String userName) {
	    boolean exists = userService.existsByUserName(userName);
	    Map<String, Object> response = new HashMap<>();

	    if (exists) {
	        response.put("status", "exists");
	        response.put("message", "Username already exists");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        response.put("status", "notExists");
	        response.put("message", "Username is available");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	}
//	@GetMapping("/checkEmail")
//    public Map<String, String> checkEmail(@RequestParam String email) {
//
//        SiteUser user = userService.getUserByEmail(email);
//        
//        Map<String, String> result = new HashMap<>();
//        
//        if (user != null) {
//            result.put("overlap", "fail");
//        } else {
//            result.put("overlap", "success");
//        }
//        return result;
//    }
//	
//	@GetMapping("/checkUserName")
//    public Map<String, String> checkUserName(@RequestParam String userName) {
//        SiteUser user = userService.getUserByUserName(userName);
//        Map<String, String> result = new HashMap<>();
//        if (user != null) {
//            result.put("overlap", "fail");
//        } else {
//            result.put("overlap", "success");
//        }
//        return result;
//    }

	@GetMapping("/verify")
    public String verify(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // PrincipalDetails에서 현재 로그인한 사용자 정보에 접근할 수 있습니다.
        SiteUser oauthUser = userService.getUserByUserName(principalDetails.getUsername());

        // db에 정보가 부족한 경우 회원가입 페이지로 리다이렉트
        if (oauthUser.getAddress() == null || oauthUser.getAddress().isEmpty() || oauthUser.getBirthDate() == null || oauthUser.getBirthDate().equals("") ||
        		oauthUser.getDetailAddress() == null || oauthUser.getDetailAddress().isEmpty() || oauthUser.getName() == null || oauthUser.getName().isEmpty() ||
        		oauthUser.getPostcode() == null || oauthUser.getPostcode().isEmpty() || oauthUser.getTel() == null || oauthUser.getTel().isEmpty()) {
            
            return "redirect:/auth/oauthSignup"; // 회원가입 페이지로 이동
            
        } else {
            // 정보가 충분한 경우 홈페이지로 리다이렉트
            return "redirect:/"; // 홈페이지로 이동
        }
	}
	//login은 security가 처리하므로 post방식의 로그인 처리 메소드는 없어도 됨
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
}