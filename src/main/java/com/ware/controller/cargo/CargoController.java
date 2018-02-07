package com.ware.controller.cargo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.ware.model.CargoInfo;
import com.ware.model.CargoUnitInfo;

import java.util.Date;
import java.util.List;

public class CargoController extends Controller {


    public CargoService cs = CargoService.me;

    public void index() {

        // findCargo();
        Ret ret = cs.findCargo();
        setAttr("cargoList", ret.get("data"));

        getUnit();
        render("index.html");

    }

    public void addCargo() {

        CargoInfo cargoInfo = new CargoInfo();
        cargoInfo.setSuppliesOrderNo(getPara("goods_code"));
        cargoInfo.setName(getPara("goods_name"));
        cargoInfo.setType(getPara("goods_type"));
        cargoInfo.setUnit(getParaToInt("unitId"));
        cargoInfo.setSpec(getPara("goods_spec"));
        cargoInfo.setCreateTime(new Date());
        cargoInfo.setUpdateTime(new Date());
        cargoInfo.setSuppliesStatus(getParaToInt("status"));
        cargoInfo.setSuppliesRemark(getPara("goods_remark"));
        cargoInfo.setSuppliesDelStatus(1);//删除状态1:正常状态;0已经删除

        Ret ret = cs.addCargo(cargoInfo);

        if (ret.isOk()) {
            ret.set("code" ,"0");
            ret.set("msg","添加成功");
            renderJson(ret);
            // index();
           // redirect("/cargo");
        }

    }

    public void editCargo() {

    }

    public void delCargo() {

        String[] idList = getParaValues("idList[]");

       Ret ret =  cs.delCargo(idList);
        if(ret.isOk()){
            ret.set("msg","操作成功");
            //重新渲染数据
           // redirect("/");
          //  index();
            renderJson(ret);
        }else {
            ret.set("msg","操作失败");
            renderJson(ret);
        }

    }

    public void findCargo() {


        Ret ret = cs.findCargo();
        if (ret.isOk()) {
            renderJson(ret);
        } else {
            renderJson(ret);
        }
    }

    /**
     * 获取规格
     */
    public void getUnit() {


        Ret ret = cs.findUnits();

        List<CargoUnitInfo> cargoUnitInfos = (List<CargoUnitInfo>) ret.get("data");
        setAttr("cargoUnitInfos", cargoUnitInfos);
    }
}
