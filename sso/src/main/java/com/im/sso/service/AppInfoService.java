package com.im.sso.service;

import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.DataKvDto;

import java.util.List;

public interface AppInfoService {
    List<DataKvDto> findInfo();

    List<DataKvDto> save(List<DataKvDto> appInfoList, AppUserDto currentUser);
}
