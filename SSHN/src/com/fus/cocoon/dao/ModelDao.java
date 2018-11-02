package com.fus.cocoon.dao;

import java.util.List;
import java.util.Map;

import com.fus.cocoon.exception.RecordUpdateException;
public interface ModelDao<T extends Model> {
	public  void deleteById(Class<T> paramClass, Object paramObject)throws RecordUpdateException;
	public  List<T> saveOrUpdateBatch(List<T> paramList)throws RecordUpdateException;
	public  T saveOrUpdate(T param) throws RecordUpdateException;
	public  void deleteObject(Object paramObject) throws RecordUpdateException;
	public  void deleteObjectBySQLQuery(String paramString, Object[] paramArrayOfObject)throws RecordUpdateException;
	public  List<?> executeSQLQueryForList(String paramString, Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass);
	public  int executeSQLQueryForUpdate(String paramString, Object[] paramArrayOfObject)throws RecordUpdateException;
	public  List<T> findByCondition(String paramString, T paramT);
	public  List<T> findByConditionPaging(String paramString, T paramT, Integer paramInteger1, Integer paramInteger2);
	public  List<Map<String, Object>> findByConditionToMap(String paramString, T paramT);
    public  List<Map<String, Object>> findByConditionToMapPaging(String paramString, T paramT, Integer paramInteger1, Integer paramInteger2);
    public  List<?> findObjectBySQLQuery(String paramString, Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass);
    public  List<?> findObjectBySQLQueryPaging(String paramString, Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass, Integer paramInteger1, Integer paramInteger2);
    public  List<Map<String, Object>> findObjectBySQLQueryToMap(String paramString, Object[] paramArrayOfObject);
    public  List<Map<String, Object>> findObjectBySQLQueryToMapPaging(String paramString, Object[] paramArrayOfObject, Integer paramInteger1, Integer paramInteger2);
    public  Object findSingleObjectBySQLQuery(String paramString, Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass);
    public  Map<String, Object> findSingleObjectBySQLQueryToMap(String paramString, Object[] paramArrayOfObject);
    public  Long getCountByCondition(String paramString, T paramT);
    public  Long getCountBySQLQuery(String paramString, Object[] paramArrayOfObject);
    public  String getStaticSql(String paramString);
    public  T findSingleByCondition(String paramString, T paramT);
    public  Map<String, Object> findSingleByConditionToMap(String paramString, T paramT);
}
