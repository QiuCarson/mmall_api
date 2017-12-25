package com.carson.mmall.VO;

public interface PageVO {



    void setHasPreviousPage(Boolean hasPreviousPage);

    void setPrePage(Integer prePage);

    void setHasNextPage(Boolean hasNextPage);

    void setNextPage(Integer nextPage);

    void setPages(Integer page);

    void setPageNum(Integer pageNum);

    void setTotal(Long total);
}
