package com.schoolmanagement.utils;

import com.schoolmanagement.entity.abstracts.User;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;

public class CheckParameterUpdateMethod {
    //Bu method ; unique olan datalar update edilmiş mi check ediyor. Eğer edilmişse checkDuplicate methodunu kullanarak dbye gidip
    //kullanıcının değiştirdiği datalar appte kullanılmış mı onu kontrol edicek. Eğer hiç unique olan dataları update etmemişse bosu bosuna
    //checkDuplicate methodunu calıstırmaya gerek yok.
    public static  boolean checkParameter(User user, BaseUserRequest baseUserRequest) {

        return user.getSsn().equalsIgnoreCase(baseUserRequest.getSsn())
                || user.getPhoneNumber().equalsIgnoreCase(baseUserRequest.getPhoneNumber())
                || user.getUsername().equalsIgnoreCase(baseUserRequest.getUsername()); // kontrol

    }
}