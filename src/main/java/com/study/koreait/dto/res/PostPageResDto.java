package com.study.koreait.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostPageResDto {
    public List<FindPostResDto> items;
    private int page;
    private int size;
    private long total;
    private int totalPage;
    private boolean hasNext;
    private boolean hasPrev;
}
