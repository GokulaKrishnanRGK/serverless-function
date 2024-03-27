package com.neu.csye6225.cloud.functions.dao;

import com.neu.csye6225.cloud.functions.entity.Verify;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifyDao {

  private static final Logger logger = LoggerFactory.getLogger(VerifyDao.class);

  private static Connection connection = null;
  private static final String DB_URL = System.getenv("CSYE6225_SQL_DB_CONNSTR");
  private static final String USERNAME = System.getenv("CSYE6225_SQL_DB_USER");
  private static final String PASSWORD = System.getenv("CSYE6225_SQL_DB_PWD");

  protected static Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    if(connection == null) {
      logger.info("DB_URL: {}, USERNAME: {}, PASSWORD: {}", DB_URL, USERNAME, PASSWORD);
      connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
    return connection;
  }

  public static Verify getVerify(String username, String token) throws SQLException, ClassNotFoundException {
    Connection conn = getConnection();
    String sql = "Select v.*, u.username FROM verify v"
        + " JOIN user u ON u.id = v.user_id"
        + " WHERE u.username=? AND v.token=?"
        + " group by v.id";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, token);
    ResultSet rs = preparedStatement.executeQuery();
    rs.next();
    Verify verify = new Verify();
    verify.setId(rs.getLong("id"));
    verify.setVerified(rs.getBoolean("is_verified"));
    verify.setToken(rs.getString("token"));
    verify.setGeneratedTime(rs.getTimestamp("generated_time"));
    verify.setUsername(rs.getString("username"));
    return verify;
  }

  public static boolean updateGeneratedTime(String token) throws SQLException, ClassNotFoundException {
    Connection conn = getConnection();
    String sql = "UPDATE verify SET generated_time=? WHERE token=?";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    preparedStatement.setString(1, new Timestamp(System.currentTimeMillis()).toString());
    preparedStatement.setString(2, token);
    int i = preparedStatement.executeUpdate();
    logger.info("Verify generated time updated for token: {}, status: {}", token, i);
    return i > 0;
  }

}
