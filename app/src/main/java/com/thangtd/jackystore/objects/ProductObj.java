package com.thangtd.jackystore.objects;

/**
 * Created by thangtd2016 on 02/03/2018.
 */

public class ProductObj {
    public Integer ordinal;
    public Integer code;
    public Integer type;

    public ProductObj() {
    }

    public ProductObj(Integer ordinal, Integer code, Integer type) {
        this.ordinal = ordinal;
        this.code = code;
        this.type = type;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
