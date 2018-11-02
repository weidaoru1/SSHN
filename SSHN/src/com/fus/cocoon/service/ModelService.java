package com.fus.cocoon.service;

import java.util.List;
import java.util.Map;

import com.fus.cocoon.dao.Model;
import com.fus.cocoon.exception.RecordUpdateException;
public interface ModelService <T extends Model>{
	public  void deleteById(Class<T> arg0, Object arg2)throws RecordUpdateException;
	public  T saveOrUpdate(T arg0)throws RecordUpdateException;
	public  List<T> saveOrUpdateBatch(List<T> arg0)throws RecordUpdateException;
	public  void deleteObject(Object arg0) throws RecordUpdateException;
	public  void deleteObjectBySQLQuery(String arg0, Object[] arg2)  throws RecordUpdateException;
	public  List<?> executeSQLQueryForList(String arg0, Object[] arg2, Class<?>[] arg3);
	public  int executeSQLQueryForUpdate(String arg0, Object[] arg2)throws RecordUpdateException;
	public  List<T> findByCondition(String arg0, T arg2);
	public  List<T> findByConditionPaging(String arg0, T arg2, Integer arg3, Integer arg4);
	public  List<Map<String, Object>> findByConditionToMap(String arg0, T arg2);
    public  List<Map<String, Object>> findByConditionToMapPaging(String arg0, T arg2, Integer arg3, Integer arg4);
    public  List<?> findObjectBySQLQuery(String arg0, Object[] arg2, Class<?>[] arg3);
    public  List<?> findObjectBySQLQueryPaging(String arg0, Object[] arg2, Class<?>[] arg3, Integer arg4, Integer arg5);
    public  List<Map<String, Object>> findObjectBySQLQueryToMap(String arg0, Object[] arg2);
    public  List<Map<String, Object>> findObjectBySQLQueryToMapPaging(String arg0, Object[] arg2, Integer arg3, Integer arg4);
    public  Object findSingleObjectBySQLQuery(String arg0, Object[] arg2, Class<?>[] arg3);
    public  Map<String, Object> findSingleObjectBySQLQueryToMap(String arg0, Object[] arg2);
    public  Long getCountByCondition(String arg0, T arg2);
    public  Long getCountBySQLQuery(String arg0, Object[] arg2);
    public  String getStaticSql(String arg0);
    public  T findSingleByCondition(String paramString, T paramT);
    public  Map<String, Object> findSingleByConditionToMap(String paramString, T paramT);
}
