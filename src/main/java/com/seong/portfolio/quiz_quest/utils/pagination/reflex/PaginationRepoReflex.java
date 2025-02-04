package com.seong.portfolio.quiz_quest.utils.pagination.reflex;

import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


//K = Repository, V = VO
public interface PaginationRepoReflex {
    List<?> findAll(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    List<?> findAllByColumnAndValue(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    int count(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
