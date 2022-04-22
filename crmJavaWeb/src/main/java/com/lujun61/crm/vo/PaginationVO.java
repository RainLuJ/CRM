package com.lujun61.crm.vo;

import java.util.List;

/**
 * @description 将来，每个模块都会有分页查询，所以我们选择创建一个通用vo（使用泛型），封装数据比较方便
 * @author Jun Lu
 * @date 2022-04-21 19:56:06
 */
public class PaginationVO<T> {
    private int total;
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
