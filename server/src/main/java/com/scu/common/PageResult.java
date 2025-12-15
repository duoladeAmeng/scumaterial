package com.scu.common;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private long total; //总记录数

    private List<T> records; //当前页数据集合

}