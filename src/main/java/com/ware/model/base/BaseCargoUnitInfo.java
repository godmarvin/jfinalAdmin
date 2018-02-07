package com.ware.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCargoUnitInfo<M extends BaseCargoUnitInfo<M>> extends Model<M> implements IBean {

    public Integer getId() {
        return get("id");
    }

    public void setId(Integer id) {
        set("id",id);
    }

    public String getUnitName() {
        return getStr("unit_name");
    }

    public void setUnitName(String unitName) {
        set("unit_name",unitName);
    }
}
