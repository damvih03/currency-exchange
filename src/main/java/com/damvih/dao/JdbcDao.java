package com.damvih.dao;

import com.damvih.utils.ConnectionProvider;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

abstract public class JdbcDao<T> {

    protected final DataSource dataSource = ConnectionProvider.getInstance();

    abstract public T create(T entity);
    abstract public List<T> findAll();

    protected boolean isUniqueError(SQLException exception) {
        int extendedResultCode = ((SQLiteException) exception).getResultCode().code;
        return extendedResultCode == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code;
    }

}
