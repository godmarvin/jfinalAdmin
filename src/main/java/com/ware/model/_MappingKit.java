package com.ware.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class _MappingKit {

    public static void mapping(ActiveRecordPlugin arp){

        arp.addMapping("system_user",UserInfo.class);
        arp.addMapping("supplies",CargoInfo.class);
        arp.addMapping("supplies_unit",CargoUnitInfo.class);



    }
}
