package com.golu.electronic.store.helper;

import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.dtos.UserDto;
import com.golu.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
        List<U> users = page.getContent();
        List<V> userDtoList = users.stream().map(user -> new ModelMapper().map(user,type)).collect(Collectors.toList());

        PageableResponse<V> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(userDtoList);
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setTotalElements(page.getNumberOfElements());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setLastPage(page.isLast());
        return pageableResponse;
    }
}
