package com.ware.controller.cargo;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.ware.model.CargoInfo;
import com.ware.model.CargoUnitInfo;

import java.util.List;

public class CargoService {

    public static final CargoService me = new CargoService();
    private final CargoInfo cargoDao = new CargoInfo().dao();
    private final CargoUnitInfo cargoUnitInfo = new CargoUnitInfo().dao();




    public Ret addCargo(CargoInfo _cargoDao) {


        if (_cargoDao.save()) {

            return Ret.ok("msg", "添加成功");
        } else {
            return Ret.fail("msg", "添加失败");
        }

    }

    public Ret findCargo() {


        List<CargoInfo> cargoInfoList = cargoDao.find("SELECT s.* , u.unit_name FROM warehouse.supplies s,warehouse.supplies_unit u WHERE s.supplies_unit = u.id;");

        if (cargoInfoList.isEmpty()) {

            Ret.fail("msg", "没有找到数据");
        }

        return Ret.ok("data", cargoInfoList);
    }

    public Ret findUnits(){


      List<CargoUnitInfo> unitInfos =   cargoUnitInfo.find("SELECT * FROM warehouse.supplies_unit;");

      if(unitInfos.isEmpty()){
          return Ret.fail("msg","没有找到数据");
      }

        return Ret.ok("data",unitInfos);
    }

    public Ret delCargo(String[] stringsId){


       int count =  Db.update("DELETE FROM `warehouse`.`supplies` WHERE `id` IN ("+StrKit.join(stringsId,",")+");");


        if(count>0){

        return Ret.ok();
        }else {
            return Ret.fail();
        }

    }
}
