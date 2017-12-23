package com.carson.mmall.utils;

import com.carson.mmall.VO.PageVO;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import org.springframework.data.domain.Page;

public class PageUtil {
    public static <T extends PageVO> T getPage(Class<T> pageClass, Page page) {
        T result = null;
        try {
            T clazz = pageClass.newInstance();
            //分页处理
            Integer totalPage = page.getTotalPages();
            //是否有上一页
            clazz.setHasPreviousPage(page.hasPrevious());
            //上一页
            Integer prePage = 0;
            if (page.hasPrevious()) {
                prePage = page.previousPageable().getPageNumber();
            }
            clazz.setPrePage(prePage + 1);
            //是否有下一页
            clazz.setHasNextPage(page.hasNext());
            //下一页
            Integer nextPage = 0;
            if (page.hasNext()) {
                nextPage = page.nextPageable().getPageNumber();
            }
            clazz.setNextPage(nextPage + 1);
            //总的页码数
            clazz.setPages(page.getTotalPages());
            //当期页码
            clazz.setPageNum(page.getNumber() + 1);


            return clazz;

        } catch (InstantiationException e) {
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);

        } catch (IllegalAccessException e) {
            throw new MmallException(ResultEnum.UNKNOWN_ERROR);
        }

    }
}
