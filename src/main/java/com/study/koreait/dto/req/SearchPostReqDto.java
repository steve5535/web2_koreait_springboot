package com.study.koreait.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchPostReqDto {
    private String titleKeyword;
    private String contentKeyword;
}
