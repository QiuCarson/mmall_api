package com.carson.mmall.controller.admin;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dataobject.Category;
import com.carson.mmall.service.CategoryService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manage/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get_category.do")
    public ResultVO getCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        List<Category> categoryList = categoryService.getCategory(categoryId);
        return ResultVOUtil.success(categoryList);
    }

    @GetMapping("/add_category.do")
    public ResultVO addCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId,
                                @RequestParam("categoryName") String categoryName){
        Category category=categoryService.addCategory(parentId,categoryName);
        return ResultVOUtil.success();
    }

    @GetMapping("/set_category_name.do")
    public ResultVO setCategoryName(@RequestParam(value = "categoryId") Integer categoryId,
                                @RequestParam("categoryName") String categoryName){
        Category category=categoryService.setCategoryName(categoryId,categoryName);
        return ResultVOUtil.success();
    }

    @GetMapping("/get_deep_category.do")
    public ResultVO getDeepCategory(@RequestParam(value = "categoryId") Integer categoryId){
        List<Integer> list=categoryService.getDeepCategory(categoryId);
        return ResultVOUtil.success(list);
    }
}
