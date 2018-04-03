package com.ware.controller.cargo;

import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.ware.commonUtils.ExcelExportUtil;
import com.ware.model.Supplies;
import com.ware.model.SuppliesUnit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ware.config.CommonConfig.pageSize;

public class CargoService {

    public static final CargoService me = new CargoService();
    private final Supplies cargoDao = new Supplies().dao();
    private final SuppliesUnit cargoUnitInfo = new SuppliesUnit().dao();


    public Ret addCargo(Supplies _cargoDao) {


        if (_cargoDao.save()) {

            return Ret.ok("msg", "添加成功");
        } else {
            return Ret.fail("msg", "添加失败");
        }

    }

    public Ret findUnits() {

        List<SuppliesUnit> unitInfos = cargoUnitInfo.find("SELECT * FROM warehouse.supplies_unit;");

        if (unitInfos.isEmpty()) {
            return Ret.fail("msg", "没有找到数据");
        }

        return Ret.ok("data", unitInfos);
    }

    public Ret delCargo(String[] stringsId) {

        Ret ret = new Ret();


        int count = Db.update("update `warehouse`.`supplies` set `del_status`='0' WHERE `id` IN (" + StrKit.join(stringsId, ",") + ");");

        if (count > 0) {

            ret.setOk();
            return ret;
        } else {
            ret.setFail();
            return ret;
        }

    }

    public File excelExport() {
        String sql = "SELECT s.* , u.unit_name FROM warehouse.supplies s,warehouse.supplies_unit u WHERE s.supplies_unit = u.id;";
        Map<String, String> titleData = new HashMap<String, String>();//标题，后面用到
//# id, supplies_order_no, supplies_name, supplies_type, supplies_unit, supplies_spec, supplies_create_time, supplies_update_time, supplies_status, supplies_remark, del_status, unit_name
        titleData.put("supplies_order_no", "货物号");
        titleData.put("supplies_name", "货物名称");
        titleData.put("supplies_type", "货物类型");
        titleData.put("unit_name", "单位");
        titleData.put("supplies_status", "货物状态");
        titleData.put("supplies_unit", "单位id");
        titleData.put("supplies_spec", "货物描述");
        titleData.put("supplies_create_time", "创建时间");
        titleData.put("supplies_update_time", "更新时间");
        titleData.put("supplies_remark", "备注");
        titleData.put("del_status", "删除状态");

        File file = new File(ExcelExportUtil.getTitle());
        file = ExcelExportUtil.saveFile(titleData, sql, file);
        return file;

    }

    /**
     * excel导入
     */
    public Ret excelImport(UploadFile file) {

        String path = file.getUploadPath() + File.separator + file.getFileName();
        // 处理导入数据
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        List<Supplies> cargoInfoList = new ArrayList<Supplies>();
        HSSFWorkbook hwb = null;
        try {
            hwb = new HSSFWorkbook(new FileInputStream(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {// 第二行开始取值，第一行为标题行
            HSSFRow row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                HSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                map.put(j, cell.getStringCellValue());
            }
            if (i != 0) {
                list.add(map);
            }
        }


        for (Map<Integer, String> map : list) { // 遍历取出的数据，并保存
            Supplies cargoInfo = new Supplies();
            cargoInfo.setSuppliesSpec(map.get(1));
            try {
                cargoInfo.setSuppliesCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(2)));
                cargoInfo.setSuppliesOrderNo(map.get(3));
                cargoInfo.setSuppliesType(map.get(4));
                cargoInfo.setSuppliesRemark(map.get(5));
                cargoInfo.setSuppliesUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(6)));
                cargoInfo.setSuppliesName(map.get(7));
                cargoInfo.setDelStatus(Integer.valueOf(map.get(8)));
                cargoInfo.setSuppliesStatus(Integer.valueOf(map.get(9)));
                cargoInfo.setSuppliesUnit(Integer.valueOf(map.get(10)));
                // cargoInfo.save();
                cargoInfoList.add(cargoInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int[] values = Db.batchSave(cargoInfoList, 5);

        Ret ret = Ret.ok();
        ret.put("total", list.size());
        ret.put("available", values.length);
        return ret;

    }

    /**
     * 激活操作
     */
    public Ret activeCargo(String[] stringsId) {

        Ret ret = new Ret();

        int count = Db.update("update `warehouse`.`supplies` set `supplies_status`='1' WHERE `id` IN (" + StrKit.join(stringsId, ",") + ");");

        ret.set("validCount", count);
        ret.set("count", stringsId.length);
        if (count > 0) {

            ret.setOk();
            return ret;
        } else {
            ret.setFail();
            return ret;
        }

    }

    /**
     * 禁用操作
     */
    public Ret forbidCargo(String[] stringsId) {

        Ret ret = new Ret();

        int count = Db.update("update `warehouse`.`supplies` set `supplies_status`='0' WHERE `id` IN (" + StrKit.join(stringsId, ",") + ");");

        ret.set("validCount", count);
        ret.set("count", stringsId.length);
        if (count > 0) {

            ret.setOk();
            return ret;
        } else {
            ret.setFail();
            return ret;
        }
    }

    /**
     * 删除回收站数据
     */
    public Ret delTrash(String[] stringsId) {
        Ret ret = new Ret();
        int count = Db.update("DELETE FROM `warehouse`.`supplies` WHERE `id` IN (" + StrKit.join(stringsId, ",") + ");");
        //   int count = Db.update("update `warehouse`.`supplies` set `supplies_status`='0' WHERE `id` IN (" + StrKit.join(stringsId, ",") + ");");
        ret.set("validCount", count);
        ret.set("count", stringsId.length);
        if (count > 0) {

            ret.setOk();
            return ret;
        } else {
            ret.setFail();
            return ret;
        }

    }

    // ("SELECT s.* , u.unit_name FROM warehouse.supplies s,warehouse.supplies_unit u WHERE s.supplies_unit = u.id and `del_status`='1';");
    public Page<Supplies> paginate(int pageNumber) {
        Page<Supplies> cargoInfoPage = cargoDao.paginate(pageNumber, pageSize, "SELECT s.* , u.unit_name", "FROM warehouse.supplies s,warehouse.supplies_unit u WHERE `del_status`='1' order by s.supplies_create_time DESC");

        return cargoInfoPage;
    }

    public Page<Supplies> searchCargo(Integer type, String inputName, Integer pageNumber) {

        String sqlType = new String();

        switch (type) {
            case 0:
                sqlType = "supplies_order_no";
                break;
            case 1:
                sqlType = "supplies_name";
                break;
            case 2:
                sqlType = "supplies_name";//todo 查询全部需要对数据的字段先进行拼接组合再进行查询
                break;
        }

        Page<Supplies> cargoInfoPage = cargoDao.paginate(pageNumber, pageSize, "SELECT s.* , u.unit_name ",
                "FROM warehouse.supplies s,warehouse.supplies_unit u " +
                        "WHERE s.supplies_unit = u.id " +
                        "and  `del_status`='1' " +
                        "and `" + sqlType + "` LIKE '%" + inputName + "%'");

        return cargoInfoPage;
    }

    public Supplies editCargo(Integer cargoId) {

        Supplies cargoInfo = cargoDao.findFirst("SELECT s.* , u.unit_name FROM warehouse.supplies s,warehouse.supplies_unit u WHERE s.supplies_unit = u.id and  `del_status`='1' AND s.id = '" + cargoId + "'");


        return cargoInfo;

    }
}
