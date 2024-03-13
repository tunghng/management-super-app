package com.im.sso.service;

import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.request.UserPlanRequest;

public interface UserPlanService {

    String save(UserPlanRequest userPlanRequest, AppUserDto currentUser);

}
