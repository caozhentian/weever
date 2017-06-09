package cn.people.weever.model;

/**
 * Created by Administrator on 2017/6/9.
 */

public class QueryModel {

    public static final int FIRST_PAGE_NUM = 1 ;
    public static final int DEFAULT_PAGE_SIZE = 10 ;

    protected  int mPage         =  FIRST_PAGE_NUM    ;

    protected  int mPageSize   = DEFAULT_PAGE_SIZE   ;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public boolean isFirstPage(){
        return mPage == FIRST_PAGE_NUM ;
    }

    public void resetPage(){
        mPage = FIRST_PAGE_NUM ;
    }

    public void addPage(int pageNum){
        mPage += pageNum ;
    }
}
