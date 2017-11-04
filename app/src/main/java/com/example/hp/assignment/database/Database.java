package com.example.hp.assignment.database;

import java.util.ArrayList;

/**
 * Created by hp on 10/20/2017.
 */

public interface Database<T> {
    String DATABASE_NAME = "evens.db";
    int DATABASE_VERSION = 1;

    boolean save(T obj);

    boolean update(T obj);

    boolean delete(T obj);

    ArrayList<T> getAllData();
}
