package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Product;
import com.carson.mmall.form.ProductForm;
import org.springframework.beans.BeanUtils;

public class ProductForm2ProductConvert {

    public static Product convert(ProductForm productForm){
        Product product=new Product();
        BeanUtils.copyProperties(productForm,product);
        return product;
    }
}
