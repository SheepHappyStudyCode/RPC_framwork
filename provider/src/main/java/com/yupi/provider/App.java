package com.yupi.provider;

import com.yupi.common.service.UserService;
import com.yupi.yurpc.bootstrap.ProviderBootstrap;
import com.yupi.yurpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo();
        serviceRegisterInfo.setServiceName(UserService.class.getName());
        serviceRegisterInfo.setImplClass(UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        ProviderBootstrap.init(serviceRegisterInfoList);


    }
}
