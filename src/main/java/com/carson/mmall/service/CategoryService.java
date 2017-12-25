package com.carson.mmall.service;

import com.carson.mmall.dataobject.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
   List<Category> getCategory(Integer categoryId);

   Category addCategory(Integer parentId,String categoryName);

   Category setCategoryName(Integer categoryId,String categoryName);

   List<Integer> getDeepCategory(Integer categoryId);
}
