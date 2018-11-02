package com.fus.cocoon.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import com.fus.cocoon.dao.Model;
import com.fus.cocoon.dao.ModelDao;
import com.fus.cocoon.dict.RecordState;
import com.fus.cocoon.exception.RecordUpdateException;
import com.fus.cocoon.spring.StringUtilsExt;
public class ModelServiceImpl <T extends Model> implements ModelService<T>{
	@Resource(name="modelDao")
	private ModelDao<T> modelDao;
	
	public void setModelDao(ModelDao<T> modelDao) {
		this.modelDao = modelDao;
	}
	public T saveOrUpdate(T obj){
		if (obj != null) {
			StringUtilsExt.trimObject(obj);
			obj.setUpdateTime(new Date());
			if (obj.getCreateTime() == null){
				obj.setCreateTime(obj.getUpdateTime());
			}
			 if (StringUtils.isBlank(obj.getCreator())) {
			        obj.setCreator("admin");
			 }
			 if (StringUtils.isBlank(obj.getUpdator())){
				 obj.setUpdator("admin");
			 }
			if (StringUtils.isBlank(obj.getState())){
				obj.setState(RecordState.NORMAL.state());
			}
		}
		return this.modelDao.saveOrUpdate(obj);
	}
	public List<T> saveOrUpdateBatch(List<T> objs){
		if (objs != null) {
			for (T obj : objs) {
				 saveOrUpdate(obj);
			}
			return objs;
		}
		return Collections.emptyList();
	}
	
	public void deleteById(Class<T> objClass, Object id)throws RecordUpdateException{
		this.modelDao.deleteById(objClass, id);
	}
	public void deleteObject(Object obj){
	   this.modelDao.deleteObject(obj);
	}
	public void deleteObjectBySQLQuery(String sql, Object[] params){
	    this.modelDao.deleteObjectBySQLQuery(sql, params);
	}
	public List<?> executeSQLQueryForList(String sql, Object[] params, Class<?>[] entities){
		return this.modelDao.executeSQLQueryForList(sql, params, entities);
	}
	public int executeSQLQueryForUpdate(String sql, Object[] params)throws RecordUpdateException{
		 return this.modelDao.executeSQLQueryForUpdate(sql, params);
	}
	public List<T> findByCondition(String sql, T obj){
		return this.modelDao.findByCondition(sql, obj);
	}
	public List<T> findByConditionPaging(String sql, T obj, Integer pageIndex, Integer pageSize){
		return this.modelDao.findByConditionPaging(sql, obj, pageIndex, pageSize);
	}
	public List<Map<String, Object>> findByConditionToMap(String sql, T obj){
		return this.modelDao.findByConditionToMap(sql, obj);
	}
	public List<Map<String, Object>> findByConditionToMapPaging(String sql, T obj, Integer pageIndex, Integer pageSize){
		return this.modelDao.findByConditionToMapPaging(sql, obj, pageIndex, pageSize);
	}
	 public List<?> findObjectBySQLQuery(String sql, Object[] params, Class<?>[] entities){
		 return this.modelDao.findObjectBySQLQuery(sql, params, entities);
	 }
	 public List<?> findObjectBySQLQueryPaging(String sql, Object[] params, Class<?>[] entities, Integer pageIndex, Integer pageSize){
		 return this.modelDao.findObjectBySQLQueryPaging(sql, params, entities, pageIndex, pageSize);
	 }
	 public List<Map<String, Object>> findObjectBySQLQueryToMap(String sql, Object[] params){
		 return this.modelDao.findObjectBySQLQueryToMap(sql, params);
	 }
	 public List<Map<String, Object>> findObjectBySQLQueryToMapPaging(String sql, Object[] params, Integer pageIndex, Integer pageSize){
		 return this.modelDao.findObjectBySQLQueryToMapPaging(sql, params, pageIndex, pageSize);
	 }
	 public Object findSingleObjectBySQLQuery(String sql, Object[] params, Class<?>[] entities){
		 return this.modelDao.findSingleObjectBySQLQuery(sql, params, entities);
	 }
	 public Map<String, Object> findSingleObjectBySQLQueryToMap(String sql, Object[] params){
		 return this.modelDao.findSingleObjectBySQLQueryToMap(sql, params);
	 }
	 public Long getCountByCondition(String sql, T obj){
		 return this.modelDao.getCountByCondition(sql, obj);
	 }
	 public Long getCountBySQLQuery(String sql, Object[] params){
		 return this.modelDao.getCountBySQLQuery(sql, params);
	 }
	 public String getStaticSql(String sqlName){
		 return this.modelDao.getStaticSql(sqlName);
	 }
	@Override
	public T findSingleByCondition(String sql, T obj) {
		 return this.modelDao.findSingleByCondition(sql, obj);
	}
	@Override
	public Map<String, Object> findSingleByConditionToMap(String paramString,
			T paramT) {
		return this.modelDao.findSingleByConditionToMap(paramString, paramT);
	}

}
