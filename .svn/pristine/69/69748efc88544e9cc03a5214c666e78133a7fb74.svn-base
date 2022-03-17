package org.uftwf.account.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xyang on 3/2/17.
 */
public class MySqlConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlConnectionFactory.class);

    public MySqlConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {

        LOGGER.info("getConnection(): return the connection of mysql database" + "\r\n");
        Connection c = null;

        InitialContext ctx;
        DataSource ds;
        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:jboss/datasources/mysql-wfservices");
            c = ds.getConnection();
        } catch (NamingException var9) {
            System.out.println("[ERROR] Cannot establish connection to mysql datasource: " + var9.getMessage());
        } catch (SQLException var10) {
            String socketErr = "COMMUNICATION LINK FAILURE. (READ TIMED OUT)";
            if (var10.toString().toUpperCase().indexOf(socketErr) == -1) {
                SQLException se = new SQLException("Unable to get connection. - " + var10.toString());
                se.initCause(var10);
                throw se;
            }

            try {
                ctx = new InitialContext();
                ds = (DataSource) ctx.lookup("java:jboss/datasources/mysql-wfservices");
                c = ds.getConnection();
            } catch (SQLException var7) {
                SQLException se1 = new SQLException("Unable to get connection after 2nd try. - " + var10.toString());
                se1.initCause(var10);
                throw se1;
            } catch (NamingException var8) {
                System.out.println("mysql ConnectionFactory error:" + var8.getMessage());
            }
        }

        return c;
    }

    public static boolean canConnect() {
        Connection connection = null;
        String dbStatus = System.getProperty("mysqlStatus");
        if (dbStatus != null && dbStatus.equalsIgnoreCase("failure")) {
            return false;
        }else {
            boolean rc;

            try {
                connection = getConnection();
            } catch (Exception var4) {
                rc = false;
            }

            if (connection == null) {
                rc = false;
            } else {
                rc = true;

                try {
                    connection.close();
                } catch (Exception var3) {

                }
            }

            return rc;
        }
    }

    public static int test() {
        return 4;
    }
}
