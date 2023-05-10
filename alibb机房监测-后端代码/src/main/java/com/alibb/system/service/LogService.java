package com.alibb.system.service;

import com.alibb.system.dto.PageParam;

import java.util.Map;

public interface LogService {

    Map<String, Object> getLogVoList(PageParam pageParam);

    Long getLogCount();

}
