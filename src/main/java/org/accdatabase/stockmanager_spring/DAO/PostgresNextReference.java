package org.accdatabase.stockmanager_spring.DAO;


import org.accdatabase.stockmanager_spring.Service.exception.ServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresNextReference {
    public String nextID(String tableName, Connection connection) throws SQLException {
        final String columnName = "ingredient_id";

        String sequenceQuery = "SELECT pg_get_serial_sequence('" + tableName + "', '" + columnName + "')";
        try (PreparedStatement stmt = connection.prepareStatement(sequenceQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String sequenceName = rs.getString(1);
                String nextvalQuery = "SELECT nextval('" + sequenceName + "')";
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
}
