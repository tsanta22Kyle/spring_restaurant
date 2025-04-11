package org.accdatabase.stockmanager_spring.DAO;


import org.accdatabase.stockmanager_spring.Service.exception.ServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgresNextReference {
    public String nextID(String tableName, Connection connection,String columnName) throws SQLException {
       // final String columnName = tableName+"_id";
        if (tableName == null || columnName == null) {
            throw new ServerException("Table name or column name cannot be null");
        }

        String sequenceQuery = "SELECT pg_get_serial_sequence('" + tableName + "', '" + columnName + "')";
        System.out.println("Running: SELECT pg_get_serial_sequence('" + tableName + "', '" + columnName + "')");

        try (PreparedStatement stmt = connection.prepareStatement(sequenceQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String sequenceName = rs.getString(1);
                System.out.println("sequenceName: " + sequenceName);
                String nextvalQuery = "SELECT nextval('" + sequenceName + "')";
                System.out.println("Running: SELECT nextval('" + sequenceName + "')");
                try (PreparedStatement nextValStmt = connection.prepareStatement(nextvalQuery);
                     ResultSet nextValRs = nextValStmt.executeQuery()) {
                    if (nextValRs.next()) {
                        return nextValRs.getString(1);
                    }
                }
            }
        }
        throw new ServerException("Unable to find sequence for table " + tableName);
    }
    public String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
