package com.seong.portfolio.quiz_quest.utils.pagination.reflex;

import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OffsetPagRepoReflexImpl implements PaginationRepoReflex {



    @Override
    public List<?> findAll(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> repositoryClass = vo.getRepository().getClass().getInterfaces()[0];
        Method paramMethod = repositoryClass.getMethod("findAll", int.class);

        return (List<?>) paramMethod.invoke(vo.getRepository(), vo.getIndex());
    }

    @Override
    public List<?> findAllByColumnAndValue(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> repositoryClass = vo.getRepository().getClass().getInterfaces()[0];
        Method paramMethod = repositoryClass.getMethod("findAllByColumnAndValue", int.class, String.class, String.class);
        return (List<?>) paramMethod.invoke(vo.getRepository(), vo.getIndex(), vo.getColumn(), vo.getValue());
    }

    @Override
    public int count(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> repositoryClass = vo.getRepository().getClass().getInterfaces()[0];
        Method paramMethod = repositoryClass.getMethod("count", String.class, Object.class);
        int result;
        if(vo.getValue() != null )
        {
            result = (Integer) paramMethod.invoke(vo.getRepository(), vo.getColumn(), vo.getSortType()) / vo.getValueOfOnePage();
        }else{
            result = (Integer) paramMethod.invoke(vo.getRepository(), vo.getColumn(), -1) / vo.getValueOfOnePage();
        }

        return result;
    }
}



