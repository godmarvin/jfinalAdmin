package com.ware.config;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.ware.controller.cargo.CargoService;
import com.ware.model.SuppliesUnit;
import com.ware.model._MappingKit;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库测试类
 */
public class debugDataBaseClass {

    private static Prop p = PropKit.use("sql/config_dev.txt");


    static {

        // 配置C3p0数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"),
                PropKit.get("user"), PropKit.get("password").trim());
        druidPlugin.start();

        DataSource dataSource = druidPlugin.getDataSource();

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);

        arp.start();
    }

    public static void main(String[] args) {


    }


    public void testFindUnit(){

        CargoService cs = CargoService.me;
        List<SuppliesUnit> cargoUnitInfos = (List<SuppliesUnit>) cs.findUnits().get("data");
        for(SuppliesUnit cargoUnitInfo:cargoUnitInfos){
            System.out.println(cargoUnitInfo.toString());
        }
    }
}
