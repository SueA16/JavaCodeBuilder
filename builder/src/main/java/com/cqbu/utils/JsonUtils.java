package com.cqbu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {
    public static String convertObj2Json(Object o){
        if (o==null){
            return null;
        }
        //SerializerFeature.DisableCircularReferenceDetect
        //指定了禁用循环引用检测的序列化特性
        //禁用循环引用检测意味着当对象存在循环引用（即对象之间相互引用）时，
        // 序列化时不会抛出异常，而是会将循环引用的属性设置为 null 或忽略掉。
        return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
    }
}
