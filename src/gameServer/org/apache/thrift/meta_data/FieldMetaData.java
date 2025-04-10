

package org.apache.thrift.meta_data;

import java.util.HashMap;
import java.util.Map;
import org.apache.thrift.TBase;
import org.apache.thrift.TFieldIdEnum;

public class FieldMetaData implements java.io.Serializable {
  public final String fieldName;
  public final byte requirementType;
  public final FieldValueMetaData valueMetaData;
  private static Map<Class<? extends TBase>, Map<? extends TFieldIdEnum, FieldMetaData>> structMap;

  static {
    structMap = new HashMap<Class<? extends TBase>, Map<? extends TFieldIdEnum, FieldMetaData>>();
  }

  public FieldMetaData(String name, byte req, FieldValueMetaData vMetaData){
    this.fieldName = name;
    this.requirementType = req;
    this.valueMetaData = vMetaData;
  }

  public static void addStructMetaDataMap(Class<? extends TBase> sClass, Map<? extends TFieldIdEnum, FieldMetaData> map){
    structMap.put(sClass, map);
  }

  public static Map<? extends TFieldIdEnum, FieldMetaData> getStructMetaDataMap(Class<? extends TBase> sClass){
    if (!structMap.containsKey(sClass)){ 
      try{
        sClass.newInstance();
      } catch (InstantiationException e){
        throw new RuntimeException("InstantiationException for TBase class: " + sClass.getName() + ", message: " + e.getMessage());
      } catch (IllegalAccessException e){
        throw new RuntimeException("IllegalAccessException for TBase class: " + sClass.getName() + ", message: " + e.getMessage());
      }
    }
    return structMap.get(sClass);
  }
}
