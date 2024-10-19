package utils.DataBase;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;

public interface BaseDB {

  DataSource datasource();


  default  <T> List<T> getDataBaseRecords(String sql, Duration seconds, Class<T> columnType){
   return DbUtils.getRecords(datasource(),sql,seconds,columnType);
  }

  default <T> T getDataBaseRecord(String sql,Duration seconds,Class<T> columnType){
    return DbUtils.getRecord(datasource(),sql,seconds,columnType);
  }

}
