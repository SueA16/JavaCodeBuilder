package com.cqnu.controller;

import com.cqnu.entity.vo.ResponseVo;

import com.cqnu.enums.ResponseCodeEnum;

public class BaseController {
    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    protected <T> ResponseVo<T> getSuccessResponseVo(T t) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.setStatus(STATUS_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(t);
        return responseVo;
    }

    protected <T> ResponseVo<T> getSuccessResponseVo() {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.setStatus(STATUS_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(null);
        return responseVo;
    }
}
