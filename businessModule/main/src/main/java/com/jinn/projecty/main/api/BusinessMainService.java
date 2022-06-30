package com.jinn.projecty.main.api;

import com.jinn.projecty.frameapi.service.IBusinessMainService;

/**
 * 暴露给其它module的接口
 * Created by jinnlee on 2021/2/24.
 */
public class BusinessMainService implements IBusinessMainService {

    @Override
    public void init() {

    }

    @Override
    public String getBusinessId(){
        return "BusinessMain";
    }
}
