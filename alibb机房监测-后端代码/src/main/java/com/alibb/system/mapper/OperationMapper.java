package com.alibb.system.mapper;

import com.alibb.system.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

}
