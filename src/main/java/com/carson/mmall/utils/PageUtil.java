package com.carson.mmall.utils;

import com.carson.mmall.VO.PageVO;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PageUtil {
    public static Object getPage(Class pageClass, Page page)      {
        Object result=null;
        try {
            //是否有上一页
            Method setHasPreviousPage = pageClass.getDeclaredMethod("setHasPreviousPage", Boolean.class);
            setHasPreviousPage.invoke(pageClass.newInstance(), page.hasPrevious());
            //上一页
            Integer prePage = 0;
            Method setPrePage = pageClass.getDeclaredMethod("setPrePage", Integer.class);
            if (page.hasPrevious()) {
                prePage = page.previousPageable().getPageNumber();
            }
            setPrePage.invoke(pageClass.newInstance(), prePage + 1);

            //是否有下一页
            Method setHasNextPage = pageClass.getDeclaredMethod("setHasNextPage", Boolean.class);
            setHasNextPage.invoke(pageClass.newInstance(), page.hasPrevious());
            //下一页
            Integer nextPage = 0;
            Method setNextPage = pageClass.getDeclaredMethod("setNextPage", Integer.class);
            if (page.hasPrevious()) {
                nextPage = page.nextPageable().getPageNumber();
            }
            setNextPage.invoke(pageClass.newInstance(), nextPage + 1);
            //总的页码数
            Method setPages = pageClass.getDeclaredMethod("setPages", Integer.class);
            setPages.invoke(pageClass.newInstance(), page.getTotalPages());

            //当期页码
            Method setPageNum = pageClass.getDeclaredMethod("setPageNum", Integer.class);
            setPageNum.invoke(pageClass.newInstance(), page.getNumber() + 1);
        }catch (NoSuchMethodException e){
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);
        }catch (InstantiationException e){
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);
        }catch (InvocationTargetException e){
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);
        } catch (IllegalAccessException e) {
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);
        }
        return null;
    }
}
