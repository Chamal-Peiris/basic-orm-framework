package lk.ijse.dep8.orm.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


public class Bootstrap {
  private List<String> stringList=new ArrayList<>();

  public Bootstrap append(String string){
      stringList.add(string);
      return this;
  }
  public String build(){
      return stringList.stream().reduce((old,current)->old+=current).get();
  }
}
