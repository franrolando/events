package com.transactionlogger.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class SearchQueryDto {

    private String app;
    private int page;
    private int pageSize;
    private List<QueryFilterDto> filter;
    private String type;
    private String action;
    private Date startDate;
    private Date endDate;

}
