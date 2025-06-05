package com.seong.portfolio.quiz_quest.utils.pagination.utils;

import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.utils.pagination.service.PaginationService;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PaginationUtil {

    public static void handlePagination(HttpServletResponse response, Model model, PaginationService paginationService, PaginationVO<?, ?> vo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        int lastPageItem = paginationService.getLastPageItemBySortType(vo);

        List<?> valueOfList = List.of();
        if(vo.getIndex() <= lastPageItem) {
            valueOfList = paginationService.getValueOfListBySortType(vo);
        }
        redirectToPage(valueOfList, vo.getIndex(), vo.getUrl(), lastPageItem, vo.getSortType(), response);

        vo.setLastPageItem(lastPageItem);
        vo.setValueOfList(valueOfList);
        paginationService.setModelItemValue(vo, model);
    }
    public static void redirectToPage(List<?> valueOfList, int index, String url,  int lastPageItem, int sortType, HttpServletResponse response) throws IOException {
        String[] parts = url.split("/");
        StringBuilder remainingUrl = new StringBuilder();


            for (int i = 3; i < parts.length; i++) {
                // 각 요소를 추가
                remainingUrl.append("/").append(parts[i]);
                // 마지막 요소가 아닐 경우 "/"를 추가
                if (i < parts.length - 1) {
                    remainingUrl.append("/");
                }
            }

        if (index >= 0 && valueOfList.isEmpty()) {
                response.sendRedirect("/"+parts[1]+"/"+lastPageItem+"/"+parts[2]+"/"+0+remainingUrl.toString());
        }
        else if(index <0){
            response.sendRedirect("/"+parts[1]+"/"+0+"/"+parts[2]+"/"+sortType+remainingUrl.toString());
        }
    }
}
