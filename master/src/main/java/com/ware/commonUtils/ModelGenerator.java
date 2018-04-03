package com.ware.commonUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

public class ModelGenerator {
	
	public static DataSource getDataSource() {
		Prop p = PropKit.use("sql/config_dev.txt");
		DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim());
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.ware.model.test.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getRootClassPath() + "/../../src/main/java/com/ware/model/test/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.ware.test.model";


		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = PathKit.getRootClassPath() + "/../../src/main/java/com/ware/test/model";
//		String modelOutputDir = baseModelOutputDir + modelPackageName;
		
		// 生成器
		Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);

		//只生成base ，不生成modal
//		Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir);

		// 设置数据库方言
		generator.setDialect(new MysqlDialect());
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(false);
		// 添加不需要生成的表名
		//generator.addExcludedTable("adv");

		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(true);
		// 设置是否生成字典文件
		generator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		//generator.setRemovedTableNamePrefixes("t_");


		// 生成
		generator.generate();

	}

}
