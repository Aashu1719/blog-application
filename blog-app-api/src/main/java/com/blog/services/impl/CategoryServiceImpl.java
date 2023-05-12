package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDto;
import com.blog.repositories.CategoryRepo;
import com.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper modelmapper;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = modelmapper.map(categoryDto, Category.class);
		Category addedCat = categoryRepo.save(category);
		return modelmapper.map(addedCat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category ", "category id", categoryId));
		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());
		Category updatedCat = categoryRepo.save(cat);
		return modelmapper.map(updatedCat, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category ", "category id", categoryId));
		categoryRepo.delete(category);
	}

	@Override
	public CategoryDto getSingleCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category ","category id", categoryId));
		
		return modelmapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> allCats = categoryRepo.findAll();
//		List<CategoryDto> dtoList=new ArrayList<>();
//		allCats.forEach(cat->{
//			CategoryDto dto = modelmapper.map(cat, CategoryDto.class);
//			dtoList.add(dto);
//		});
//		return dtoList;
		
		List<CategoryDto> dtoList = allCats.stream().map(cat->modelmapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		return dtoList;
	}
}
