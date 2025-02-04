package com.seong.portfolio.quiz_quest.utils.pagination.service;

import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import org.springframework.ui.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public interface PaginationService{
    int getLastPageItemBySortType(PaginationVO<?, ?> vo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    List<?> getValueOfListBySortType(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    void setModelItemValue(PaginationVO<?, ?> vo, Model model);
}
