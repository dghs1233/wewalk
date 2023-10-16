package com.spring.boot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.boot.dao.CartItemRepository;
import com.spring.boot.dao.CartRepository;
import com.spring.boot.dao.ProductRepository;
import com.spring.boot.dao.UserRepository;
import com.spring.boot.model.Cart;
import com.spring.boot.model.CartItem;
import com.spring.boot.model.Product;
import com.spring.boot.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	
	
	public List<Product> getProductList(String userName){
		
		Long user_id = userRepository.findByUserName(userName).get().getId();
		
		Cart userCart = cartRepository.findByUserId(user_id);
		
		List<CartItem> cartItemList = userCart.getCartItemList();
		
		List<Product> productList = new ArrayList<Product>();
		
		for(int i=0; i<cartItemList.size(); i++) {
			Product product = new Product();
			
			product = cartItemList.get(i).getProduct();
			
			productList.add(product);
		}
		
		
		return productList;
		
	}
	
	public List<CartItem> getCartItemList(String userName){
		
		SiteUser user = userRepository.findByUserName(userName).get();
		
		Cart cart = cartRepository.findById(user.getId()).get();
		
		return cart.getCartItemList();
		
	}
	
}