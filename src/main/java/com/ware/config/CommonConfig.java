package com.ware.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.core.JFinal;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.ware.controller.cargo.CargoController;
import com.ware.controller.home.HomeController;
import com.ware.controller.login.LoginController;
import com.jfinal.config.*;
import com.jfinal.template.Engine;
import com.ware.model._MappingKit;

import java.sql.Connection;

public class CommonConfig extends JFinalConfig{

   private static Prop p = PropKit.use("sql/config_dev.txt");
   public static Integer pageSize =  5;


    private WallFilter wallFilter;

    public static void main(String[] args) {
        /**
         * 特别注意：Eclipse 之下建议的启动方式
         */
        //JFinal.start("src/main/webapp", 8090, "/", 5);

        /**
         * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
         */
        /**
         * 对于有module的项目需要添加module的名称
         *http://www.jfinal.com/share/674
         */

       JFinal.start("src/main/webapp", 8089, "/");

    }


    @Override
    public void configConstant(Constants me) {

        me.setDevMode(true);
        me.setJsonFactory(MixedJsonFactory.me());
    }

    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("view");
        me.add("/login",LoginController.class);
        me.add("/",HomeController.class,"/home");
        me.add("/cargo",CargoController.class,"/cargo");
    }

    @Override
    public void configEngine(Engine me) {
            me.setDevMode(p.getBoolean("engineDevMode"));
        me.addSharedFunction("/view/common/mainPage.html");
        me.addSharedFunction("/view/common/_paginate.html");
    }

    @Override
    public void configPlugin(Plugins me) {

        DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl").trim(),p.get("user").trim(),p.get("password").trim());

        wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");

        druidPlugin.addFilter(wallFilter);
        druidPlugin.addFilter(new StatFilter());

        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);

        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        _MappingKit.mapping(arp);


        arp.setDevMode(p.getBoolean("devMode"));
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());

        me.add(arp);
        me.add(new EhCachePlugin());
       // me.add(new Cron4jPlugin(p));



    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
