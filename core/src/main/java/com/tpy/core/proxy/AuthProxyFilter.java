package com.tpy.core.proxy;


import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class AuthProxyFilter implements CallbackFilter {


    @Override
    public int accept(Method method) {
        return 0;
    }
}
