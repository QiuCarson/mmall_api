package com.carson.mmall.VO;

public interface PageVO {
    Boolean setHasPreviousPage(Boolean hasPreviousPage);

    Integer setPrePage(Integer prePage);

    Boolean setHasNextPage(Boolean hasNextPage);

    Integer setNextPage(Integer nextPage);

    Integer setPages(Integer page);

    Integer setPageNum(Integer pageNum);
}
