package org.uftwf.account.model;

public class ObjectDB {
    boolean dbStatus;
    Object dbObject;

    public boolean isDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(boolean dbStatus) {
        this.dbStatus = dbStatus;
    }

    public Object getDbObject() {
        return dbObject;
    }

    public void setDbObject(Object dbObject) {
        this.dbObject = dbObject;
    }
}
