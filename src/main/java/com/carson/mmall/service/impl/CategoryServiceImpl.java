package com.carson.mmall.service.impl;

import com.carson.mmall.dataobject.Category;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.CategoryRepository;
import com.carson.mmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getCategory(Integer categoryId) {
       List<Category> categoryList= categoryRepository.findByParentId(categoryId);
        return categoryList;
    }

    @Override
    public Category addCategory(Integer parentId, String categoryName) {
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        categoryRepository.save(category);
        return categoryRepository.save(category);
    }

    @Override
    public Category setCategoryName(Integer categoryId, String categoryName) {
        Category category=categoryRepository.findOne(categoryId);
        if(category==null){
            throw new MmallException(ResultEnum.CATEGORY_NOT_EXISTS);
        }
        category.setName(categoryName);
        return categoryRepository.save(category);
    }

    @Override
    public List<Integer> getDeepCategory(Integer categoryId) {
        List<Category> categoryList=categoryRepository.findByParentId(categoryId);
        if(categoryList==null){
            throw new MmallException(ResultEnum.CATEGORY_NOT_EXISTS);
        }
        List<Integer> list=categoryList.stream().map(e->e.getId()).collect(Collectors.toList());
        return list;
    }
}
