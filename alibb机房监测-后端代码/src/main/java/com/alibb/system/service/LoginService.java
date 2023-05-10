package com.alibb.system.service;

import com.alibb.system.dto.LoginParam;
import com.alibb.system.dto.RegisterParam;

import java.util.Map;

public interface LoginService {

    Map<String, Object> login(LoginParam loginParam);

    Integer register(RegisterParam registerParam);
}
