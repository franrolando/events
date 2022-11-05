package com.transactionlogger.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SearchQueryDto {

    private String app;
    private int page;
    private int pageSize;
    private QueryFilterDto filter;
    private String type;
    private String action;
    private Date startDate;
    private Date endDate;

}
