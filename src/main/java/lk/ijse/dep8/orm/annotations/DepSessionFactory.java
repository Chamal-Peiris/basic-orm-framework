package lk.ijse.dep8.orm.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DepSessionFactory {
    private final List<Class<?>> entityClassList = new ArrayList<>();
    private Connection connection;

    public DepSessionFactory addAnnotatedClass(Class<?> entityClass){
        if (entityClass.getDeclaredAnnotation(Entity.class) == null){
            throw new RuntimeException("Invalid entity class");
        }
        entityClassList.add(entityClass);
        return this;
    }

    public DepSessionFactory setConnection(Connection connection){
        this.connection = connection;
        return this;
    }

    public DepSessionFactory build(){
        if (this.connection == null){
            throw new RuntimeException("Failed to build without a connection");
        }
        return this;
    }

    public void bootstrap() throws SQLException {
        for (Class<?> entity : entityClassList) {
            String tableName = entity.getDeclaredAnnotation(Entity.class).value();
            if (tableName.trim().isEmpty()) tableName = entity.getSimpleName();
            List<String> columns = new ArrayList<>();
            String primaryKey = null;

            Field[] fields = entity.getDeclaredFields();
            for (Field field : fields) {
                Id primaryKeyField = field.getDeclaredAnnotation(Id.class);
                if (primaryKeyField != null) {
                    primaryKey = field.getName();
                    continue;
                }

                String columnName = field.getName();
                columns.add(columnName);
            }
            if (primaryKey == null) throw new RuntimeException("Entity without a primary key");

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(tableName).append("(");
            for (String column : columns) {
                sb.append(column).append(" VARCHAR(255),");
            }
            sb.append(primaryKey).append("VARCHAR(255) PRIMARY KEY)");
            Statement stm = connection.createStatement();
            stm.executeQuery(sb.toString());
        }
    }
}
