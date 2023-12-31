package com.spring.boot.dto;

import java.util.List;

import javax.validation.constraints.PositiveOrZero;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

public class ProductForm {
	
	private String pname; 

    private String content;

    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Integer price; // 필드 이름 수정
    
    @PositiveOrZero(message = "재고는 0개 이상이어야 합니다.")
    private Integer stock;
    
    private List<MultipartFile> images;
    
    private String category;

   

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}

	public Integer getStock() {
	    return stock;
	}

	public void setStock(Integer stock) {
	    this.stock = stock;
	}

}
