package io.projectandroid.twlocator.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import io.projectandroid.twlocator.model.ModelPersistable;
import io.projectandroid.twlocator.model.db.DBHelper;

/**
 * Created by izabela on 07/05/16.
 */


public abstract class GenericDAO<T extends ModelPersistable, E> {



    public static final long INVALID_ID_DELETE_ALL_RECORDS = 0;
    protected DBHelper db;

    private WeakReference<Context> context;


    public GenericDAO() {
        db = DBHelper.getInstance();
    }

    //abstract methods:
    public abstract ContentValues getContentValues(T element);
    public abstract String getTableName();
    public abstract @NonNull T elementFromCursor(final @NonNull Cursor c);
    protected abstract E mapElementsInAgregate(List<T> list);


    public long insert(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Passing NULL notebook, imbecile");
        }

        if (context.get() == null) {
            throw new IllegalStateException("Context NULL");
        }

        // insert
        DBHelper db = DBHelper.getInstance();

        long id = db.getWritableDatabase().insert(getTableName(), null, this.getContentValues(element));
        element.setId(id);
        db.close();
        return id;
    }




    public int update(long id, T element) {
        if (element == null) {
            throw new IllegalArgumentException("Passing NULL notebook, imbecile");
        }
        if (id<1){
            throw new IllegalArgumentException("Passing id invalid, imbecile");
        }
        if (context.get() == null) {
            throw new IllegalStateException("Context NULL");
        }

        DBHelper db = DBHelper.getInstance();

        int numberOfRowsUpdated = db.getWritableDatabase().update(getTableName(), this.getContentValues(element),  "_id = ?", new String[]{"" + id});

        db.close();
        return numberOfRowsUpdated;
    }

    public void delete(long id) {
        DBHelper db = DBHelper.getInstance();

        if (id == INVALID_ID_DELETE_ALL_RECORDS) {
            db.getWritableDatabase().delete(getTableName(),  null, null);
        } else {
            db.getWritableDatabase().delete(getTableName(),  "_id = " + id, null);
        }
        db.close();
    }

    public void deleteAll() {
        delete(INVALID_ID_DELETE_ALL_RECORDS);
    }

    public Cursor queryCursor() {
        // select
        Cursor c = db.getReadableDatabase().query(getTableName(), getAllColumns(), null, null, null, null, getId());
        return c;
    }

    public abstract String[] getAllColumns();
    public abstract String getId();

    public E query() {
        List<T> list= new LinkedList<>();


        Cursor cursor = queryCursor();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                T element = elementFromCursor(cursor);
                list.add(element);
            } while (cursor.moveToNext());
        }
        E agregate = mapElementsInAgregate(list);
        return agregate;
    }

    //protected abstract E mapElementsInAgregate(List<T> list);



    public T query(long id) {
        T element = null;


        String where = getId() + "=" + id;
        Cursor c = db.getReadableDatabase().query(getTableName(), getAllColumns(), where, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                element = elementFromCursor(c);
            }
        }
        c.close();
        db.close();
        return element;
    }


}