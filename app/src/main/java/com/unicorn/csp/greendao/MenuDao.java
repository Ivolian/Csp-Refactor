package com.unicorn.csp.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MENU.
*/
public class MenuDao extends AbstractDao<Menu, String> {

    public static final String TABLENAME = "MENU";

    /**
     * Properties of entity Menu.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Type = new Property(2, String.class, "type", false, "TYPE");
        public final static Property OrderNo = new Property(3, int.class, "orderNo", false, "ORDER_NO");
        public final static Property ParentId = new Property(4, String.class, "parentId", false, "PARENT_ID");
    };

    private DaoSession daoSession;

    private Query<Menu> menu_ChildrenQuery;

    public MenuDao(DaoConfig config) {
        super(config);
    }
    
    public MenuDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MENU' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'TYPE' TEXT NOT NULL ," + // 2: type
                "'ORDER_NO' INTEGER NOT NULL ," + // 3: orderNo
                "'PARENT_ID' TEXT);"); // 4: parentId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MENU'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Menu entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getType());
        stmt.bindLong(4, entity.getOrderNo());
 
        String parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindString(5, parentId);
        }
    }

    @Override
    protected void attachEntity(Menu entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Menu readEntity(Cursor cursor, int offset) {
        Menu entity = new Menu( //
            cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // type
            cursor.getInt(offset + 3), // orderNo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // parentId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Menu entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setType(cursor.getString(offset + 2));
        entity.setOrderNo(cursor.getInt(offset + 3));
        entity.setParentId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Menu entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Menu entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "children" to-many relationship of Menu. */
    public List<Menu> _queryMenu_Children(String parentId) {
        synchronized (this) {
            if (menu_ChildrenQuery == null) {
                QueryBuilder<Menu> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ParentId.eq(null));
                queryBuilder.orderAsc(Properties.OrderNo);
                menu_ChildrenQuery = queryBuilder.build();
            }
        }
        Query<Menu> query = menu_ChildrenQuery.forCurrentThread();
        query.setParameter(0, parentId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMenuDao().getAllColumns());
            builder.append(" FROM MENU T");
            builder.append(" LEFT JOIN MENU T0 ON T.'PARENT_ID'=T0.'ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Menu loadCurrentDeep(Cursor cursor, boolean lock) {
        Menu entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Menu parent = loadCurrentOther(daoSession.getMenuDao(), cursor, offset);
        entity.setParent(parent);

        return entity;    
    }

    public Menu loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Menu> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Menu> list = new ArrayList<Menu>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Menu> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Menu> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
