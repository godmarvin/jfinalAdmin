package com.ware.controller.cargo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.ware.commonUtils.ExcelExportUtil;
import com.ware.model.CargoInfo;
import com.ware.model.CargoUnitInfo;

import java.io.File;
import java.util.Date;
import java.util.List;

public class CargoController extends Controller {


    public CargoService cs = CargoService.me;

    public void index() {

        findCargo();

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
            ret.set("code", "0");
            ret.set("msg", "添加成功");
            renderJson(ret);
        }

    }

    /**
     * todo 更新操作会放到另外一个页面中去
     */
    public void renderEditCargo() {

        getUnit();
        Integer cargoId = getParaToInt("cargoId");

        CargoInfo cargoInfo = cs.editCargo(cargoId);

        setAttr("cargoInfo",cargoInfo);
        render("editCargo.html");
    }

    public void editCargo(){

        CargoInfo cargoInfo = new CargoInfo();
        cargoInfo.setId(getParaToInt("id"));
        cargoInfo.setSuppliesOrderNo(getPara("goods_code"));
        cargoInfo.setName(getPara("goods_name"));
        cargoInfo.setType(getPara("goods_type"));
        cargoInfo.setUnit(getParaToInt("unitId"));
        cargoInfo.setSpec(getPara("goods_spec"));
        //cargoInfo.setCreateTime(new Date());
        cargoInfo.setUpdateTime(new Date());
        cargoInfo.setSuppliesStatus(getParaToInt("status"));
        cargoInfo.setSuppliesRemark(getPara("goods_remark"));
        cargoInfo.setSuppliesDelStatus(1);//删除状态1:正常状态;0已经删除
        if (cargoInfo.update()){
            renderJson(Ret.ok());
        }else {
            renderJson(Ret.fail());
        }

    }
    /**
     * 删除
     */
    public void delCargo() {

        String[] idList = getParaValues("idList[]");

        Ret ret = cs.delCargo(idList);
        if (ret.isOk()) {
            ret.set("msg", "操作成功");
            renderJson(ret);
        } else {
            ret.set("msg", "操作失败");
            renderJson(ret);
        }

    }

    /**
     * 查询:会有多种查询方式
     */
    public void findCargo() {

        getUnit();
        Page<CargoInfo> cargoInfoPage = new Page<CargoInfo>();
        Integer pageNumber = getParaToInt("p", 1);

        if(getPara("input")==null){
            cargoInfoPage = cs.paginate(pageNumber);
        }else {
            Integer type = getParaToInt("type");
            String inputName = getPara("input");
            cargoInfoPage =  cs.searchCargo(type,inputName,pageNumber);
        }

        setAttr("sharePage", cargoInfoPage);
        setAttr("cargoList", cargoInfoPage.getList());
        render("index.html");
    }

    /**
     * 获取规格
     */
    public void getUnit() {


        Ret ret = cs.findUnits();

        List<CargoUnitInfo> cargoUnitInfos = (List<CargoUnitInfo>) ret.get("data");
        setAttr("cargoUnitInfos", cargoUnitInfos);
    }

    /**
     * 导出数据
     */
    public void excelExport() {
        renderFile(cs.excelExport());
    }

    /**
     * 导出模板
     */
    public void modelExcel() {
        renderFile(new File(ExcelExportUtil.getModleFiles()));
    }

    /**
     * 导入数据
     */
    public void excelImport() {
        UploadFile file = getFile("file");
        Ret ret = cs.excelImport(file);

        ret.put("result", "success");
        renderJson(ret);
    }

    /**
     * 激活操作
     */
    public void activeCargo() {

        String[] idList = getParaValues("idList[]");
        renderJson(cs.activeCargo(idList));
    }

    /**
     * 禁用操作
     */
    public void forbidCargo() {
        String[] idList = getParaValues("idList[]");
        renderJson(cs.forbidCargo(idList));
    }

    /**
     * 删除回收站数据
     */
    public void delTrash() {

    }

}
