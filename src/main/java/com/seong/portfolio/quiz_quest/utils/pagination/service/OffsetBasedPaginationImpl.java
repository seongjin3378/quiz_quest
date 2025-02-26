package com.seong.portfolio.quiz_quest.utils.pagination.service;

import com.seong.portfolio.quiz_quest.utils.pagination.reflex.PaginationRepoReflex;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OffsetBasedPaginationImpl implements PaginationService{
    private final PaginationRepoReflex paginationRepoReflex;

    @Override
    public int getLastPageItemBySortType(PaginationVO<?, ?> vo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return paginationRepoReflex.count(vo);
    }

    @Override
    public List<?> getValueOfListBySortType(PaginationVO<?, ?> vo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("sort Type: {}, Index: {}", vo.getSortType(), vo.getIndex());
        if (vo.getSortType() == 0 && vo.getIndex() >= 0) {
            return paginationRepoReflex.findAll(vo);
        }
        else if (vo.getIndex() < 0) {
            return null;
        }
        else {

            return paginationRepoReflex.findAllByColumnAndValue(vo);
        }
    }



    @Override
    public void setModelItemValue(PaginationVO<?, ?> vo, Model model) {
        int pageItemStart = (vo.getIndex()/ vo.getPageItemCount())* vo.getPageItemCount();
        int pageItemEnd = pageItemStart + vo.getPageItemCount() - 1;
        if(vo.getLastPageItem() / vo.getPageItemCount() == vo.getIndex() / vo.getPageItemCount())
        {
            pageItemEnd = vo.getLastPageItem();
            model.addAttribute("nextButtonDisable", true);
        }else if(vo.getIndex() / vo.getPageItemCount() == 0)
        {
            model.addAttribute("prevButtonDisable", true);
        }
        model.addAttribute("pageItemStart", pageItemStart);
        model.addAttribute("pageItemEnd", pageItemEnd);
        model.addAttribute("pageItemList", vo.getValueOfList());
        model.addAttribute("currentPage", vo.getIndex());
        model.addAttribute("sortType", vo.getSortType());
    }





}
