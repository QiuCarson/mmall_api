package com.carson.mmall.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "mmall_category")
@DynamicUpdate
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer status;

    private String sortOrder;

    private Date createTime;

    private Date updateTime;
}
