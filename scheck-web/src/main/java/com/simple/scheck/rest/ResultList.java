package com.simple.scheck.rest;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by dell on 2017/5/31.
 */
public class ResultList<T> extends AbstractResult{

    private int pageIndex; //当前页

    private int pageSize; //当前页条数

    private int totalPages; //总页数

    private long totalNumber; //总条数

    private List<T> dataList; //列表数据

    public ResultList(int code, String message) {
        super(code, message);
    }

    public static <T> Result<T> ok() {return new Result<>(StatusCode.SUCCESS, null);}

    public static <T> Result<T> ok(int code, String message) {return new Result<>(code, message);}

    public static <T> Result<T> error() {return new Result<>(StatusCode.ERROR, null);}

    public static <T> Result<T> error(int code, String message) {return new Result<>(code, message);}

    public static <T> ResultList<T> list(PageInfo<T> obj) {
        ResultList<T> result = new ResultList(StatusCode.SUCCESS, null);
        result.dataList = obj.getList();
        result.pageIndex = obj.getPageNum();
        result.pageSize = obj.getPageSize();
        result.totalNumber = obj.getTotal();
        result.totalPages = obj.getPages();
        return result;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
