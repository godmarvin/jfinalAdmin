package com.ware.controller.cargo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.ware.commonUtils.DateUtils;
import com.ware.commonUtils.ExcelExportUtil;
import com.ware.model.Supplies;
import com.ware.model.SuppliesUnit;

import java.io.File;
import java.util.Date;
import java.util.List;

public class CargoController extends Controller {


    public CargoService cs = CargoService.me;

    public void index() {

        findCargo();

    }

    public void addCargo() {

        
        Supplies cargoInfo = new Supplies();
        cargoInfo.setSuppliesOrderNo(getPara("goods_code"));
        cargoInfo.setSuppliesName(getPara("goods_name"));
        cargoInfo.setSuppliesType(getPara("goods_type"));
        cargoInfo.setSuppliesUnit(getParaToInt("unitId"));
        cargoInfo.setSuppliesSpec(getPara("goods_spec"));
        cargoInfo.setSuppliesCreateTime(new Date());
        cargoInfo.setSuppliesUpdateTime(new Date());
        cargoInfo.setSuppliesStatus(getParaToInt("status"));
        cargoInfo.setSuppliesRemark(getPara("goods_remark"));
        cargoInfo.setDelStatus(1);//删除状态;1:正常状态;0已经删除

        Ret ret = cs.addCargo(cargoInfo);

        if (ret.isOk()) {
            ret.set("code", "0");
            ret.set("msg", "添加成功");
            renderJson(ret);
        }

    }

    public void renderEditCargo() {

        getUnit();
        Integer cargoId = getParaToInt("cargoId");

        Supplies cargoInfo = cs.editCargo(cargoId);

        setAttr("cargoInfo", cargoInfo);
        render("editCargo.html");
    }

    /**
     * 编辑信息
     */
    public void editCargo() {

        Supplies cargoInfo = new Supplies();
        cargoInfo.setId(getParaToInt("id"));
        cargoInfo.setSuppliesOrderNo(getPara("goods_code"));
        cargoInfo.setSuppliesName(getPara("goods_name"));
        cargoInfo.setSuppliesType(getPara("goods_type"));
        cargoInfo.setSuppliesUnit(getParaToInt("unitId"));
        cargoInfo.setSuppliesSpec(getPara("goods_spec"));
        cargoInfo.setSuppliesUpdateTime(DateUtils.normalDate());
        cargoInfo.setSuppliesStatus(getParaToInt("status"));
        cargoInfo.setSuppliesRemark(getPara("goods_remark"));
        cargoInfo.setDelStatus(1);//删除状态1:正常状态;0已经删除
        if (cargoInfo.update()) {
            renderJson(Ret.ok());
        } else {
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
        Page<Supplies> cargoInfoPage = new Page<Supplies>();
        Integer pageNumber = getParaToInt("p", 1);

        if (getPara("input")==null || getPara("input").isEmpty()) {
            cargoInfoPage = cs.paginate(pageNumber);
        } else {
            Integer type = getParaToInt("type");
            String inputName = getPara("input");
            cargoInfoPage = cs.searchCargo(type, inputName, pageNumber);
        }
        while (cargoInfoPage.getList().size() == 0) {
            cargoInfoPage = cs.paginate(pageNumber - 1);
        }


        for(Supplies cargoInfo:cargoInfoPage.getList()){

              //  cargoInfo.setUpdateTime(new Date());

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

        List<SuppliesUnit> cargoUnitInfos = (List<SuppliesUnit>) ret.get("data");
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
