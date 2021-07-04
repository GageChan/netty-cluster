package com.gagechan;

import cn.hutool.core.lang.Dict;

public class AppConfig {

    private static final Dict dict = Dict.create();

    public static void set(String key, Object value) {
        dict.set(key, value);
    }

    public static Dict getDict() {
        return dict;
    }

}
