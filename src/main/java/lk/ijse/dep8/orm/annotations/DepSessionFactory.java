package lk.ijse.dep8.orm.annotations;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class DepSessionFactory {
  private List<Class<?>> entityClassList=new ArrayList<>();
  private Connection connection;

  public DepSessionFactory addAnnotatedClass(Class<?> entityClass){
      if(entityClass.getDeclaredAnnotations(Entity.class)==null){
          throw new RuntimeException("Invalid Entity class");
      }
      entityClassList.add(entityClassClass);
    return this;
  }
  public DepSessionFactory setConnection(Connection connection){
   this.connection=connection;
   return this;
  }


}
