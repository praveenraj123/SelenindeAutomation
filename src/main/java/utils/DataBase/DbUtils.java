package utils.DataBase;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import getters.DbConnection;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.helpers.Reporter;
import org.slf4j.helpers.Util;
import utils.SleepingUtils;
import utils.TestDataReader;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbUtils implements BaseDB{

  private static String JDBC;
  private static String user;
  private static String pass;
  private static Long second;

  public DbUtils(){
    var path="src/main/resources/DB/dbconfig.yml";
    var connection= TestDataReader.readDataFromYMl(path,DbConnection.class);
    JDBC=connection.getDbConnectorData().getUrl();
    user=connection.getDbConnectorData().getUser();
    pass=connection.getDbConnectorData().getPass();
     }


    @Step
    public static <T> List<T> getRecords(DataSource source ,String sql, Duration second, Class<T> rowType){
     List<T> result= new ArrayList<>();
     int attempt=1;
      LocalDateTime time= LocalDateTime.now();
     Reporter.info(String.format(" SQL Query --> %s",sql));
        do {
          try {
            if (rowType == String.class) {
              result = Jdbi.create(source).withHandle((handle) -> {
                return handle.createQuery(sql).mapTo(rowType).list();
              });
            } else {
              result = Jdbi.create(source).withHandle(handle -> {
                return handle.createQuery(sql).mapToBean(rowType).list();
              });
            }
          } catch (Exception e) {
            Reporter.error(e.toString());
          }
          Reporter.info(String.format("Attempted sql %d time(s) ",attempt));
          ++attempt;
          SleepingUtils.Wait(Duration.ofSeconds(30));
        }while (result.isEmpty() && LocalDateTime.now().isBefore(time.plus(second)));
      return result;
    }

  @Step
  @SuppressWarnings("un checked")
  public static <T> T getRecord(DataSource source, String sql, Duration second, Class<T> rowType){
    Optional<T> option=Optional.empty();
    int attempt=1;
    LocalDateTime time= LocalDateTime.now();
    Reporter.info(String.format(" SQL Query --> %s",sql));
    do {
      try {
        if (rowType == String.class) {
          option = Jdbi.create(source).withHandle((handle) -> {
            return handle.createQuery(sql).mapTo(rowType).findFirst();
          });
        } else {
          option = Jdbi.create(source).withHandle(handle -> {
            return handle.createQuery(sql).mapToBean(rowType).findFirst();
          });
        }
      } catch (Exception e) {
        Reporter.error(e.toString());
      }
      Reporter.info(String.format("Attempted sql %d time(s) ",attempt));
      ++attempt;
      SleepingUtils.Wait(Duration.ofSeconds(30));
    }while (option.isEmpty() && LocalDateTime.now().isBefore(time.plus(second)));
    return (T) option;
  }

  public static HikariDataSource getDataSource(){
    HikariConfig config=new HikariConfig();
    var db = new DbUtils();
    config.setJdbcUrl(JDBC);
    config.setUsername(user);
    config.setPassword(pass);
    return new HikariDataSource(config);
  }

  public static void main(String[] args) {
    var path="src/main/resources/DB/dbconfig.yml";
    var connection= TestDataReader.readDataFromYMl(path,DbConnection.class);
    JDBC=connection.getDbConnectorData().getUrl();
    user=connection.getDbConnectorData().getUser();
    pass=connection.getDbConnectorData().getPass();
    try {
      var result=getRecords(getDataSource(),"select customername from customers; ",Duration.ofSeconds(30),String.class);
      System.out.println(result.size());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DataSource datasource() {
    return null;
  }
}
