package com.cxg.qrcode.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回值处理
 */

public class ResponseUtil extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ResponseUtil() {
        put("code", 0);
        put("msg", "success");
    }

    public static ResponseUtil error() {
        return error(1, "error");
    }

    public static ResponseUtil error(String msg) {
        return error(500, msg);
    }

    public static ResponseUtil error(int code, String msg) {
        ResponseUtil r = new ResponseUtil();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ResponseUtil success(String msg) {
        ResponseUtil r = new ResponseUtil();
        r.put("msg", msg);
        return r;
    }

    public static ResponseUtil success(Map<String, Object> map) {
        ResponseUtil r = new ResponseUtil();
        r.putAll(map);
        return r;
    }

    public static ResponseUtil success() {
        return new ResponseUtil();
    }

    @Override
    public ResponseUtil put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
