package com.mark.stylish.objects;

import java.io.Serializable;

public class Color implements Serializable {
    private String mName;
    private String mCode;

    public Color() {
        this.mName = "";
        this.mCode = "";
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
