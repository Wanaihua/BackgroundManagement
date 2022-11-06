package com.yuhua.backgroundmanagement.config.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTException;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.yuhua.backgroundmanagement.common.Constants;
import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.exception.ServiceException;
import com.yuhua.backgroundmanagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.auth0.jwt.JWT;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token,请重新登陆");
        }
        //获取token中的user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败,请重新登陆");
        }
        //获取token中的user id
        User user = userService.getById(userId);
        if(user == null){
            throw new ServiceException(Constants.CODE_401,"用户不存在,请重新登陆");
        }
        //用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);  //验证token
        } catch (JWTException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败,请重新登陆");
        }

        return true;
    }
}
