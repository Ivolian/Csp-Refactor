package com.unicorn.csp.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig menuDaoConfig;

    private final MenuDao menuDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        menuDaoConfig = daoConfigMap.get(MenuDao.class).clone();
        menuDaoConfig.initIdentityScope(type);

        menuDao = new MenuDao(menuDaoConfig, this);

        registerDao(Menu.class, menuDao);
    }
    
    public void clear() {
        menuDaoConfig.getIdentityScope().clear();
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

}