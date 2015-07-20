package com.unicorn.csp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.bugsnag.android.Bugsnag;
import com.unicorn.csp.greendao.DaoMaster;
import com.unicorn.csp.greendao.DaoSession;
import com.unicorn.csp.greendao.MenuDao;
import com.unicorn.csp.greendao.SearchHistoryDao;
import com.unicorn.csp.volley.MyVolley;


public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        Bugsnag.init(instance);
        MyVolley.init(instance);
        initGreenDao();
    }






    private static DaoSession daoSession;


    public static MenuDao getMenuDao(){

        return  daoSession.getMenuDao();
    }

    public static SearchHistoryDao getSearchHistoryDao(){

        return daoSession.getSearchHistoryDao();
    }


    private void initGreenDao(){

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"csp-db",null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

}
