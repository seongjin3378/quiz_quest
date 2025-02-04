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

    public static void handlePagination(Object repository, HttpServletResponse response, Model model, PaginationService paginationService, PaginationVO<?, ?> vo) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        int lastPageItem = paginationService.getLastPageItemBySortType(new PaginationVO.Builder<Object, String>()
                .repository(repository).index(vo.getIndex())
                .column(vo.getColumn())
                .value(ProblemType.getDisplayNameByIndex(vo.getSortType()))
                .valueOfOnePage(vo.getValueOfOnePage())
                .build());

        List<?> valueOfList = List.of();
        if(vo.getIndex() <= lastPageItem) {
            valueOfList = paginationService.getValueOfListBySortType(new PaginationVO.Builder<Object, String>()
                    .column(vo.getColumn())
                    .repository(repository)
                    .value(ProblemType.getDisplayNameByIndex(vo.getSortType()))
                    .sortType(vo.getSortType())
                    .index(vo.getIndex())
                    .build());
        }
        redirectToPage(valueOfList, vo.getIndex(), vo.getUrl(), lastPageItem, vo.getSortType(), response);

        paginationService.setModelItemValue(new PaginationVO.Builder<ProblemRepository, String>()
                .pageItemCount(vo.getPageItemCount())
                .index(vo.getIndex())
                .lastPageItem(lastPageItem)
                .valueOfList(valueOfList)
                .build(), model);
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
