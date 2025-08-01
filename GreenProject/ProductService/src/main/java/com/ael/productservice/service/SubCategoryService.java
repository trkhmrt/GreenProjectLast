package com.ael.productservice.service;

import com.ael.productservice.request.SubCategoryRequest;
import com.ael.productservice.response.SubCategoryResponse;
import com.ael.productservice.model.Category;
import com.ael.productservice.model.SubCategory;
import com.ael.productservice.repository.ICategoryRepository;
import com.ael.productservice.repository.ISubCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubCategoryService implements ISubCategoryService {


    ISubCategoryRepository subCategoryRepository;
    ICategoryRepository categoryRepository;


    @Override
    public void createSubCategory(SubCategoryRequest subCategoryRequest) {

        Category category = categoryRepository.findById(subCategoryRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));


        SubCategory newSubCategory = SubCategory.builder()
                .subCategoryName(subCategoryRequest.getSubCategoryName())
                .category(category)
                .build();

        subCategoryRepository.save(newSubCategory);

    }

    @Override
    public void deleteSubCategory(Integer subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }

    @Override
    public void updateSubCategoryName(Integer subCategoryId, String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        subCategory.setSubCategoryName(subCategoryName);
        subCategoryRepository.save(subCategory);
    }

    @Transactional
    @Override
    public List<SubCategoryResponse> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        return subCategories.stream()
                .map(subCategory -> new SubCategoryResponse(
                        subCategory.getSubCategoryId(),
                        subCategory.getSubCategoryName(),
                        subCategory.getCategory().getCategoryId(),
                        subCategory.getCategory().getCategoryName()))
                .toList();
    }


}
