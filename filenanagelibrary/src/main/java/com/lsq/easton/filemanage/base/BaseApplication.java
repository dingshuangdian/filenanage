package com.lsq.easton.filemanage.base;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.lsq.easton.filemanage.bean.DaoMaster;
import com.lsq.easton.filemanage.bean.DaoSession;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by CWJ on 2017/3/20.
 */

public class BaseApplication extends Application {
  private static DaoSession daoSession;
  //运用list来保存们每一个activity是关键
  private List<Activity> mList = new LinkedList<Activity>();
  //为了实现每次使用该类时不创建新的对象而创建的静态对象
  private static BaseApplication instance;
  //构造方法
  //实例化一次
  public synchronized static BaseApplication getInstance(){
    if (null == instance) {
      instance = new BaseApplication();
    }
    return instance;
  }
  @Override
  public void onCreate() {
    super.onCreate();
    //配置数据库
    setupDatabase();
  }
  // add Activity
  public void addActivity(Activity activity) {
    mList.add(activity);
  }
  //关闭每一个list内的activity
  public void exit() {

      for (Activity activity:mList) {
        if (activity != null)
          activity.finish();

    }
  }
  //杀进程


    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "file.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
