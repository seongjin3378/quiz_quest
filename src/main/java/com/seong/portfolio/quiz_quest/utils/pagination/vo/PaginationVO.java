package com.seong.portfolio.quiz_quest.utils.pagination.vo;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationVO<K, V> {
    private K repository;
    private String column;
    private int index;
    private int sortType;
    private V value;
    private List<?> valueOfList;
    private int valueOfOnePage;
    private String url; //PathVariable 제외하고 입력
    private int lastPageItem;
    private int pageItemCount;
    private List<?> resultList;

    // 빌더 클래스
    public static class Builder<K, V> {
        private K repository;
        private String column;
        private int index;
        private int sortType;
        private V value;
        private List<?> valueOfList;
        private int valueOfOnePage;
        private String url;
        private int lastPageItem;
        private int pageItemCount;
        private List<?> resultList;
        private HttpServletResponse response;
        public Builder<K, V> repository(K repository) {
            this.repository = repository;
            return this;
        }

        public Builder<K, V> column(String column) {
            this.column = column;
            return this;
        }

        public Builder<K, V> index(int index) {
            this.index = index;
            return this;
        }

        public Builder<K, V> sortType(int sortType) {
            this.sortType = sortType;
            return this;
        }

        public Builder<K, V> value(V value) {
            this.value = value;
            return this;
        }

        public Builder<K, V> valueOfList(List<?> valueOfList) {
            this.valueOfList = valueOfList;
            return this;
        }

        public Builder<K, V> valueOfOnePage(int valueOfOnePage) {
            this.valueOfOnePage = valueOfOnePage;
            return this;
        }

        public Builder<K, V> url(String url) {
            this.url = url;
            return this;
        }



        public Builder<K, V> lastPageItem(int lastPageItem) {
            this.lastPageItem = lastPageItem;
            return this;
        }

        public Builder<K, V> pageItemCount(int pageItemCount) {
            this.pageItemCount = pageItemCount;
            return this;
        }

        public Builder<K, V> resultList(List<?> resultList) {
            this.resultList = resultList;
            return this;
        }

        public PaginationVO<K, V> build() {
            PaginationVO<K, V> paginationVO = new PaginationVO<>();
            paginationVO.repository = this.repository;
            paginationVO.column = this.column;
            paginationVO.index = this.index;
            paginationVO.sortType = this.sortType;
            paginationVO.value = this.value;
            paginationVO.valueOfList = this.valueOfList;
            paginationVO.valueOfOnePage = this.valueOfOnePage;
            paginationVO.url = this.url;
            paginationVO.lastPageItem = this.lastPageItem;
            paginationVO.pageItemCount = this.pageItemCount;
            paginationVO.resultList = this.resultList;
            return paginationVO;
        }
    }

    @Override
    public String toString() {
        return "PaginationVO{" +
                "repository=" + repository +
                ", column='" + column + '\'' +
                ", index=" + index +
                ", sortType=" + sortType +
                ", value=" + value +
                ", valueOfList=" + valueOfList +
                ", valueOfOnePage=" + valueOfOnePage +
                ", lastPageUrl='" + url + '\'' +
                ", lastPageItem=" + lastPageItem +
                ", pageItemCount=" + pageItemCount +
                ", resultList=" + resultList +
                '}';
    }
}
