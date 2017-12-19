package com.carson.mmall.VO;

import com.carson.mmall.dataobject.Shipping;
import lombok.Data;

import java.util.List;

@Data
public class ShippingVO {
    private Integer pageNum;

    private Integer pageSize;

    private Integer size;

    private String orderBy;

    private Integer startRow;

    private Integer endRow;

    private Integer total;

    private Integer pages;

    private Integer firstPage;

    private Integer prePage;

    private Integer nextPage;

    private Integer lastPage;

    private Boolean isFirstPage;

    private Boolean isLastPage;

    private Boolean hasPreviousPage;

    private Boolean hasNextPage;

    private Integer navigatePages;

    private List<Integer> navigatepageNums;

    private List<Shipping> list;
}
