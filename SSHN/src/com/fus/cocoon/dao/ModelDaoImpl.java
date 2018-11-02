package com.fus.cocoon.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import com.fus.cocoon.annotation.ModelAttr;
import com.fus.cocoon.exception.RecordUpdateException;
import com.fus.cocoon.spring.StringUtilsExt;
import com.fus.cocoon.utils.object.ClassUtils;
import com.fus.cocoon.utils.object.Clazz;
public class ModelDaoImpl<T extends Model> implements  ModelDao<T>{
	private SessionFactory sessionFactory;
	private Properties SQL_MAPPER = new Properties();
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public T saveOrUpdate(T obj) throws RecordUpdateException {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(obj);
		} catch (Exception e) {
			throw new RecordUpdateException(e);
		}
		return obj;
	}

	public List<T> saveOrUpdateBatch(List<T> objs) throws RecordUpdateException {
		try {
			for (T obj : objs) {
				saveOrUpdate(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordUpdateException(e);
		}
		return objs;
	}

	public void deleteById(Class<T> objClass, Object id) throws RecordUpdateException {
		deleteObject(findSingleObject(objClass, id));
	}

	public void deleteObject(Object obj) throws RecordUpdateException {
		try {
			sessionFactory.getCurrentSession().delete(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordUpdateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T findSingleObject(Class<T> objClass, Object id) {
		if (id instanceof String) {
			return (T) sessionFactory.getCurrentSession().get(objClass, String.valueOf(id));
		}
		if (id instanceof Integer) {
			return (T) sessionFactory.getCurrentSession().get(objClass,Integer.parseInt(String.valueOf(id)));
		}
		if (id instanceof Long) {
			return (T) sessionFactory.getCurrentSession().get(objClass,Long.parseLong(String.valueOf(id)));
		}
		if (id instanceof Short) {
			return (T) sessionFactory.getCurrentSession().get(objClass,Short.parseShort(String.valueOf(id)));
		}
		return null;
	}

	public void deleteObjectBySQLQuery(String sql, Object[] params)throws RecordUpdateException {
		try {
			executeSQLQueryForUpdate(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordUpdateException(e);
		}
	}

	public int executeSQLQueryForUpdate(String sql, Object[] params)throws RecordUpdateException {
		try {
			return getSQLQuery(sessionFactory.getCurrentSession(), sql, params, null, false).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RecordUpdateException(e);
		}
	}

	private SQLQuery getSQLQuery(Session session, String sql, Object[] params,Class<?>[] entities, boolean addScalars) {
		SQLQuery query = session.createSQLQuery(sql);
		if (entities != null) {
			for (int i = 0; i < entities.length; i++) {
				query.addEntity(entities[i]);
			}
		}
		if (addScalars) {
			String[] scalars = getSqlScalars(sql);
			for (int i = 0; i < scalars.length; i++) {
				query.addScalar(scalars[i]);
			}
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query;
	}

	private String[] getSqlScalars(String sql) {
		List<String> scalars = new ArrayList<String>();
		sql = sql.replace("SELECT", "select").replace("FROM", "from").replace("WHERE", "where").substring(sql.indexOf("select") + "select".length());
		String selectColumn = "";
		int leftqt = 1;
		int rightqt = 0;
		char lastChar = ';';
		for (int i = 0; i < sql.length(); i++) {
			if ((sql.charAt(i) == 'f')&& (StringUtils.isBlank(String.valueOf(lastChar)))) {
				if (("from".equals(sql.substring(i, i + 4)))&& (leftqt == rightqt + 1)) {
					selectColumn = sql.substring(0, i).trim();
					break;
				}
			} else if (sql.charAt(i) == '(')
				leftqt++;
			else if (sql.charAt(i) == ')') {
				rightqt++;
			}
			lastChar = sql.charAt(i);
		}
		Pattern p = Pattern.compile("\\s|\t|\r|\n");
		Matcher m = p.matcher(selectColumn);
		selectColumn = m.replaceAll(" ");
		String[] columns = selectColumn.split(",");
		int leftBrack = 0;
		int rightBrack = 0;
		List<String> errorPartList = new ArrayList<String>();
		for (String column : columns) {
			String extraColumn = column.trim();
			for (int i = 0; i < extraColumn.length(); i++) {
				if (extraColumn.charAt(i) == '(')
					leftBrack++;
				else if (extraColumn.charAt(i) == ')') {
					rightBrack++;
				}
			}
			if (leftBrack == rightBrack) {
				leftBrack = 0;
				rightBrack = 0;
				errorPartList.clear();
			} else {
				errorPartList.add(extraColumn);
				continue;
			}
			int space = extraColumn.lastIndexOf(" ");
			if (-1 == space) {
				space = extraColumn.lastIndexOf(".");
			}
			String extraColumnAlias = extraColumn.substring(space + 1).trim();
			if (!"*".equals(extraColumnAlias)) {
				scalars.add(extraColumnAlias);
			}
		}
		if (errorPartList.size() > 0) {
			String extraColumn = ((String) errorPartList.get(0)).trim();
			String extraColumnAlias = "";
			if (extraColumn.startsWith("(")) {
				leftBrack = 0;
				rightBrack = 0;
				for (int i = 0; i < extraColumn.length(); i++) {
					if (extraColumn.charAt(i) == '(')
						leftBrack++;
					else if (extraColumn.charAt(i) == ')') {
						rightBrack++;
					}
					if (leftBrack == rightBrack)
						break;
					String nextString = "";
					if (i < extraColumn.length()) {
						Scanner scanner = new Scanner(extraColumn.substring(i + 1, extraColumn.length()));
						while (scanner.hasNext()) {
							nextString = scanner.next();
							if (!nextString.equalsIgnoreCase("as"))
								break;
						}
						extraColumnAlias = nextString.trim();
					}
				}
			} else {
				String nextStr = "";
				Scanner scanner = new Scanner(extraColumn);
				while (scanner.hasNext()) {
					nextStr = scanner.next();
					if (nextStr.equalsIgnoreCase("from"))
						break;
					extraColumnAlias = nextStr;
				}
			}
			if (!"*".equals(extraColumnAlias)) {
				scalars.add(extraColumnAlias);
			}
		}
		String[] scalarsArr = new String[scalars.size()];
		scalars.toArray(scalarsArr);
		return scalarsArr;
	}

	public List<?> executeSQLQueryForList(String sql, Object[] params,Class<?>[] entities) {
		return getSQLQuery(sessionFactory.getCurrentSession(), sql, params, entities, true).list();
	}

	public List<T> findByCondition(String sql, T obj) {
		SqlParamsObject sqlParams = getDynamicSqlParams(sql, obj, false);
		if (sqlParams != null) {
			return (List<T>) getExtendAttributesList(findObjectBySQLQuery(sqlParams.getSql(), sqlParams.getParams(),new Class[] { obj.getClass() }),sqlParams.getExtendAttrNames());
		}
		return Collections.emptyList();
	}

	private SqlParamsObject getDynamicSqlParams(String sql, Model obj,boolean isCount) {
		SqlParamsObject sqlParams = null;
		if (obj != null) {
			sqlParams = new SqlParamsObject();
			Map<String, String> customQueryCondition = obj.getCustomerQueryConditionMap();
			List<WhereClauseElement> whereClauseList = getWhereClauseElementList(obj, customQueryCondition);
			sqlParams.setSql(getDynamicSQL(obj, sql, whereClauseList, isCount));
			if (!isCount) {
				sqlParams.setExtendAttrNames(getSqlScalars(sql));
			}
			if (whereClauseList != null) {
				sqlParams.setParams(getDynamicSQLWhereClauseParams(whereClauseList));
			}
		}
		return sqlParams;
	}

	@SuppressWarnings("unchecked")
	private List<WhereClauseElement> getWhereClauseElementList(Object obj,Map<String, String> specialClauses) {
		List<WhereClauseElement> clauseList = new ArrayList<WhereClauseElement>();
		if (obj instanceof Model) {
			Clazz clazz = ClassUtils.getClazz(obj);
			if (clazz != null) {
				String operator = "=";
				List<Field> fields = new ArrayList<Field>();
				fields.addAll(clazz.getClassFields());
				Class superClass = clazz.getClazz().getSuperclass();
				Field[] superClassFields;
				while ((superClass != null) && (Object.class != superClass)) {
					String className = superClass.getName();
					if ("com.fus.cocoon.dao.Model".equals(className)) {
						superClassFields = superClass.getDeclaredFields();
						for (Field f : superClassFields) {
							if (f.getAnnotation(ModelAttr.class) != null) {
								fields.add(f);
							}
						}
					} else {
						superClassFields = superClass.getDeclaredFields();
						for (Field f : superClassFields) {
							fields.add(f);
						}
					}
					superClass = superClass.getSuperclass();
				}
				Object fieldName;
				if ((fields != null) && (fields.size() > 0)) {
					for (Field field : fields) {
						Transient tran = (Transient) field.getAnnotation(Transient.class);
						fieldName = field.getName();
						String clauseName = "";
						Object clauseValue = null;
						if ((tran == null)&& (!"serialVersionUID".equals(fieldName))) {
							try {
								Method beanIdentityGetter = clazz.getClazz().getMethod("getModelIdentity",new Class[0]);
								String beanIdentity = (String) beanIdentityGetter.invoke(obj, new Object[0]);
								clauseName = (StringUtils.isNotBlank(beanIdentity) ? beanIdentity : clazz.getClassSimpleName().toLowerCase())+ "." + (String) fieldName;
								Method getMethod = clazz.getClazz().getMethod("get"	+ StringUtilsExt.upperCasePrefixString((String) fieldName),new Class[0]);
								String op = "";
								if ((specialClauses != null)&& (StringUtils.isNotBlank(op = (String) specialClauses.get(clauseName)))) {
									operator = op;
								} else {
									Class fieldType = field.getType();
									if (String.class.equals(fieldType)) {
										operator = "=";
									}
								}
								clauseValue = getMethod.invoke(obj,new Object[0]);
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
							WhereClauseElement whereClauseElement = generateWhereClauseElement(clauseName, operator, clauseValue);
							if (whereClauseElement != null) {
								clauseList.add(whereClauseElement);
							}
						}
						operator = "=";
					}
				}
				try {
					Method getMethod = clazz.getClazz().getMethod("getExtendAttributesMap", new Class[0]);
					Map extendAttributesMap = (Map) getMethod.invoke(obj,new Object[0]);
					if (extendAttributesMap != null) {
						for (fieldName = extendAttributesMap.keySet().iterator(); ((Iterator) fieldName).hasNext();) {
							String clauseName = (String) ((Iterator) fieldName).next();
							Object clauseValue = extendAttributesMap.get(clauseName);
							String op = (String) specialClauses.get(clauseName);
							if (StringUtils.isNotBlank(op)) {
								operator = op;
							} else if (clauseValue instanceof String) {
								operator = "=";
							}
							WhereClauseElement whereClauseElement = generateWhereClauseElement(clauseName, operator, clauseValue);
							if (whereClauseElement != null) {
								clauseList.add(whereClauseElement);
							}
							operator = "=";
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return clauseList;
	}

	private WhereClauseElement generateWhereClauseElement(String clauseName,String operator, Object clauseValue) {
		WhereClauseElement whereClauseElement = null;
		boolean isValid = false;
		if (((clauseValue instanceof String))&& (StringUtils.isNotBlank(String.valueOf(clauseValue)))) {
			isValid = true;
		} else if ((!(clauseValue instanceof String)) && (clauseValue != null)) {
			isValid = true;
		}
		if (isValid) {
			whereClauseElement = new WhereClauseElement();
			whereClauseElement.setClauseName(clauseName);
			whereClauseElement.setOperator(operator);
			whereClauseElement.setClauseValue(clauseValue);
		}
		return whereClauseElement;
	}

	private String getDynamicSQL(Model obj, String staticSql,List<WhereClauseElement> clauseList, boolean isCount) {
		StringBuilder sql = new StringBuilder();
		if (StringUtils.isNotBlank(staticSql)) {
			sql.append(staticSql);
			sql.append(getDynamicSQLWhereClause(clauseList));
			sql.append(getDynamicSQLGroupByHavingClause(obj, isCount));
			if (!isCount) {
				sql.append(getDynamicSQLSortOrderClause(obj));
			}
		}
		return sql.toString();
	}

	private String getDynamicSQLWhereClause(List<WhereClauseElement> clauseList) {
		StringBuilder where = new StringBuilder();
		if ((clauseList != null) && (clauseList.size() > 0)) {
			for (WhereClauseElement element : clauseList) {
				if ((!(element.getClauseValue() instanceof List))|| (((element.getClauseValue() instanceof List)) && (("between".equals(element.getOperator())) || ("in".equals(element.getOperator()))))) {
					where.append(" and ");
					where.append(element.getClauseName());
					where.append(" "+ element.getOperator().replace("rlike", "like").replace("llike", "like"));
				}
				if ((element.getClauseValue() instanceof Date)) {
					where.append(" ? ");
				} else if ((element.getClauseValue() instanceof List)) {
					List valueList = (List) element.getClauseValue();
					if ((valueList != null) && (valueList.size() > 0)) {
						if ("between".equals(element.getOperator())) {
							if (valueList.size() == 2) {
								where.append(" ? and ? ");
							}
						} else if ("in".equals(element.getOperator())) {
							where.append(" ( ");
							for (int i = 0; i < valueList.size(); i++) {
								where.append(" ? ");
								if (i < valueList.size() - 1) {
									where.append(" , ");
								}
							}
							where.append(" ) ");
						} else {
							String op = element.getOperator();
							String[] ops = op.split(",");
							if (ops != null) {
								if (ops.length == valueList.size()) {
									where.append(" and ( ");
									int i = 0;
									for (int size = valueList.size(); i < size; i++) {
										where.append(element.getClauseName());
										where.append(" " + ops[i]);
										if (("is".equals(ops[i]))|| ("is not".equals(ops[i]))) {
											if ("null".equals(valueList.get(i))) {
												where.append(" null ");
											} else {
												where.append(" ? ");
											}
										} else {
											where.append(" ? ");
										}
										if (i < size - 1) {
											where.append(" or ");
										}
									}
									where.append(" ) ");
								} else {
									for (int i = 0; i < valueList.size(); i++) {
										where.append(" and "
												+ element.getClauseName());
										where.append(" "
												+ element.getOperator()
														.replace("rlike",
																"like")
														.replace("llike",
																"like"));
										if (("is".equals(element.getOperator()))
												|| ("is not".equals(element
														.getOperator()))) {
											if ("null".equals(valueList.get(i))) {
												where.append(" null ");
											} else {
												where.append(" ? ");
											}
										} else {
											where.append(" ? ");
										}
									}
								}
							}
						}
					}
				} else if ("in".equals(element.getOperator())) {
					where.append(" ( ");
					where.append(" ? ");
					where.append(" ) ");
				} else if (("is".equals(element.getOperator()))
						|| ("is not".equals(element.getOperator()))) {
					if ("null".equals(element.getClauseValue())) {
						where.append(" null ");
					} else {
						where.append(" ? ");
					}
				} else {
					where.append(" ? ");
				}
			}
		}
		return where.toString();
	}

	private String getDynamicSQLGroupByHavingClause(Object obj, boolean isCount) {
		StringBuilder sql = new StringBuilder();
		if ((obj instanceof Model)) {
			Model model = (Model) obj;
			if (!isCount) {
				List<String> groupBy = model.getGroupBy();
				if ((groupBy != null) && (groupBy.size() > 0)) {
					sql.append(" group by ");
					for (String gb : groupBy) {
						sql.append(gb);
						sql.append(",");
					}
					sql.delete(sql.length() - 1, sql.length());
				}
			}
			if (StringUtils.isNotBlank(model.getHaving())) {
				sql.append(" having " + model.getHaving() + " ");
			}
		}
		return sql.toString();
	}

	private String getDynamicSQLSortOrderClause(Object obj) {
		StringBuilder sql = new StringBuilder();
		if ((obj != null) && ((obj instanceof Model))) {
			Clazz objClass = ClassUtils.getClazz(obj);
			try {
				Method getMethod = objClass.getClazz().getMethod(
						"getSortFieldMap", null);
				Map<String, Object> sortFieldMap = (Map) getMethod.invoke(obj,
						new Object[0]);
				if ((sortFieldMap != null) && (sortFieldMap.size() > 0)) {
					boolean isMysql = "mysql".equalsIgnoreCase(this.SQL_MAPPER
							.getProperty("DB_TYPE"));
					sql.append(" order by ");
					for (String sortField : sortFieldMap.keySet()) {
						sql.append((isMysql ? "convert(" + sortField
								+ " using gbk)" : sortField)
								+ " "
								+ (String) sortFieldMap.get(sortField)
								+ ",");
					}
					sql.delete(sql.length() - 1, sql.length());
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	private Object[] getDynamicSQLWhereClauseParams(
			List<WhereClauseElement> clauseList) {
		List<Object> params = new ArrayList<Object>();
		Object realValue = null;
		if ((clauseList != null) && (clauseList.size() > 0)) {
			for (WhereClauseElement element : clauseList) {
				String operator = element.getOperator();
				if ((element.getClauseValue() instanceof List)) {
					List valueList = (List) element.getClauseValue();
					if ((valueList != null) && (valueList.size() > 0)) {
						Object value;
						if ((!"between".equals(operator))
								&& (!"in".equals(operator))) {
							String[] ops = operator.split(",");
							if (ops != null) {
								if (ops.length == valueList.size()) {
									for (int i = 0; i < valueList.size(); i++) {
										if (("is".equals(ops[i]))
												|| ("is not".equals(ops[i]))) {
											if (!"null"
													.equals(valueList.get(i))) {
												params.add(valueList.get(i));
											}
										} else {
											realValue = getRealClauseValue(
													valueList.get(i), ops[i]);
											if (realValue != null) {
												params.add(realValue);
											}
										}
									}
								} else {
									for (Iterator localIterator2 = valueList
											.iterator(); localIterator2
											.hasNext();) {
										value = localIterator2.next();
										realValue = getRealClauseValue(value,
												operator);
										if (realValue != null) {
											params.add(realValue);
										}
									}
								}
							}
						} else {
							for (value = valueList.iterator(); ((Scanner) value)
									.hasNext();) {
								realValue = getRealClauseValue(value, operator);
								if (realValue != null) {
									params.add(realValue);
								}
							}
						}
					}
				} else {
					realValue = getRealClauseValue(element.getClauseValue(),
							operator);
					if (realValue != null) {
						params.add(realValue);
					}
				}
			}
		}
		return params.toArray();
	}

	private Object getRealClauseValue(Object value, String operator) {
		if (value != null) {
			if ((value instanceof String)) {
				value = ((String) value).trim();
			}
			if ("like".equals(operator))
				value = "%" + value + "%";
			else if ("llike".equals(operator))
				value = "%" + value;
			else if ("rlike".equals(operator))
				value = value + "%";
			else if (("is".equals(operator)) || ("is not".equals(operator))) {
				value = null;
			}
		}

		return value;
	}

	public List<?> findObjectBySQLQuery(String sql, Object[] params,
			Class<?>[] entities) {
		return executeSQLQueryForList(sql, params, entities);
	}

	private List<?> getExtendAttributesList(List<?> list,
			String[] extendAttrNames) {
		if ((extendAttrNames != null) && (extendAttrNames.length > 0)
				&& (list != null) && (list.size() > 0)) {
			Object firstObj = list.get(0);
			if ((firstObj instanceof Object[])) {
				List<Object> returnList = new ArrayList<Object>(list.size());
				int elementCount = 0;
				for (Iterator localIterator1 = list.iterator(); localIterator1
						.hasNext();) {
					Object obj = localIterator1.next();
					Object[] objArray = (Object[]) obj;
					Map<String, Object> nativeObjectMap = new LinkedHashMap<String, Object>();
					Map<String, Object> extendAttributeMap = new LinkedHashMap<String, Object>();
					int extendAttrIndex = 0;
					for (Object objElement : objArray) {
						if ((objElement instanceof Model)) {
							nativeObjectMap.put(((Model) objElement)
									.getModelIdentity(), objElement);
						} else if (extendAttrIndex < extendAttrNames.length) {
							extendAttributeMap.put(
									extendAttrNames[(extendAttrIndex++)],
									objElement);
						}
					}
					int index = 0;
					for (String extendAttrName : extendAttributeMap.keySet()) {
						Object extendAttrValue = extendAttributeMap
								.get(extendAttrName);
						String beanSimpleName = StringUtilsExt
								.findWordBeforeFirstUppercaseCharacter(extendAttrName);
						if (!StringUtils.isNotBlank(beanSimpleName))
							break;
						Object beanObj = nativeObjectMap.get(beanSimpleName);
						if (beanObj == null)
							break;
						try {
							Method addMethod = beanObj.getClass().getMethod(
									"addExtendAttribute",
									new Class[] { String.class, Object.class });
							addMethod.invoke(beanObj, new Object[] {
									extendAttrName,
									extendAttrValue == null ? ""
											: extendAttrValue });
							index++;
						} catch (SecurityException e) {
							e.printStackTrace();
							break;
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							break;
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							break;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							break;
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							break;
						}
					}
					if (index < extendAttributeMap.size()) {
						break;
					}
					if (nativeObjectMap.size() > 0) {
						Object[] beanArray = nativeObjectMap.values().toArray();

						Object o = null;
						if (beanArray.length == 1)
							o = beanArray[0];
						else {
							o = beanArray;
						}
						returnList.add(o);
					}
					elementCount++;
				}
				if (elementCount >= list.size()) {
					list = null;
					return returnList;
				}
			}
		}
		return list;
	}

	public List<T> findByConditionPaging(String sql, T obj, Integer pageIndex,
			Integer pageSize) {
		SqlParamsObject sqlParams = getDynamicSqlParams(sql, obj, false);
		if (sqlParams != null) {
			return (List<T>) getExtendAttributesList(
					findObjectBySQLQueryPaging(sqlParams.getSql(), sqlParams
							.getParams(), new Class[] { obj.getClass() },
							pageIndex, pageSize), sqlParams
							.getExtendAttrNames());
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> findByConditionToMapPaging(String sql,
			T obj, Integer pageIndex, Integer pageSize) {
		if (!"oracle".equalsIgnoreCase(this.SQL_MAPPER.getProperty("DB_TYPE"))) {
			SqlParamsObject sqlParams = getDynamicSqlParams(sql, obj, false);
			return findObjectBySQLQueryToMapPaging(sqlParams.getSql(),
					sqlParams.getParams(), pageIndex, pageSize);
		}
		return resultListWithExtendAttributeToMap(findByConditionPaging(sql,
				obj, pageIndex, pageSize));
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findObjectBySQLQueryToMapPaging(
			String sql, Object[] params, Integer pageIndex, Integer pageSize) {
		return getSQLQuery(sessionFactory.getCurrentSession(), sql, params, null, false)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageIndex.intValue() * pageSize.intValue())
				.setMaxResults(pageSize.intValue()).list();
	}

	public List<?> findObjectBySQLQueryPaging(String sql, Object[] params,
			Class<?>[] entities, Integer pageIndex, Integer pageSize) {
		return getSQLQuery(sessionFactory.getCurrentSession(), sql, params, entities, true)
				.setFirstResult(pageIndex.intValue() * pageSize.intValue())
				.setMaxResults(pageSize.intValue()).list();
	}

	public List<Map<String, Object>> findByConditionToMap(String sql, T obj) {
		if (!"oracle".equalsIgnoreCase(this.SQL_MAPPER.getProperty("DB_TYPE"))) {
			SqlParamsObject sqlParams = getDynamicSqlParams(sql, obj, false);
			return findObjectBySQLQueryToMap(sqlParams.getSql(), sqlParams
					.getParams());
		}
		return resultListWithExtendAttributeToMap(findByCondition(sql, obj));
	}

	public List<Map<String, Object>> resultListWithExtendAttributeToMap(
			List<?> list) {
		if ((list != null) && (list.size() > 0)) {
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for (Iterator localIterator = list.iterator(); localIterator
					.hasNext();) {
				Object listObj = localIterator.next();
				Object[] arrayObj = (Object[]) null;
				if (!(listObj instanceof Object[])) {
					arrayObj = new Object[1];
					arrayObj[0] = listObj;
				} else {
					arrayObj = (Object[]) listObj;
				}
				Map recordMap = new HashMap();
				for (Object obj : arrayObj) {
					if ((obj instanceof Model)) {
						Clazz clazz = ClassUtils.getClazz(obj);
						try {
							Map extendAttributesMap = (Map) clazz.getClazz()
									.getMethod("getExtendAttributesMap",
											new Class[0]).invoke(obj,
											new Object[0]);
							if ((extendAttributesMap != null)
									&& (extendAttributesMap.size() > 0)) {
								recordMap.putAll(extendAttributesMap);
							}
							List<Field> objFields = new ArrayList<Field>();
							objFields.addAll(clazz.getClassFields());
							Class superClass = clazz.getClazz().getSuperclass();
							Field[] fields;
							while ((superClass != null)
									&& (Object.class != superClass)) {
								String className = superClass.getName();
								if ("com.fus.core.dao.Model".equals(className)) {
									fields = superClass.getDeclaredFields();
									for (Field f : fields) {
										if (f.getAnnotation(ModelAttr.class) != null) {
											objFields.add(f);
										}
									}
								} else {
									fields = superClass.getDeclaredFields();
									for (Field f : fields) {
										objFields.add(f);
									}
								}
								superClass = superClass.getSuperclass();
							}
							for (Field f : objFields) {
								String fieldName = f.getName();
								if (!"serialVersionUID".equals(fieldName)) {
									Method method = clazz
											.getClazz()
											.getMethod(
													"get"
															+ StringUtilsExt
																	.upperCasePrefixString(fieldName),
													new Class[0]);
									if (!recordMap.containsKey(fieldName)) {
										recordMap.put(fieldName, method.invoke(
												obj, new Object[0]));
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							return Collections.emptyList();
						}
					}
				}
				if (recordMap.size() > 0) {
					listMap.add(recordMap);
				}
			}
			return listMap;
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> findObjectBySQLQueryToMap(String sql,
			Object[] params) {
		return executeSQLQueryForListToMap(sql, params);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> executeSQLQueryForListToMap(String sql,
			Object[] params) {
		SQLQuery query = getSQLQuery(sessionFactory.getCurrentSession(), sql, params, null,
				false);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	public Object findSingleObjectBySQLQuery(String sql, Object[] params,
			Class<?>[] entities) {
		Iterator it = findObjectBySQLQuery(sql, params, entities).iterator();
		return it.hasNext() ? it.next() : null;
	}

	public Map<String, Object> findSingleObjectBySQLQueryToMap(String sql,
			Object[] params) {
		Iterator it = findObjectBySQLQueryToMap(sql, params).iterator();
		if (it.hasNext()) {
			return (Map) it.next();
		}
		return null;
	}

	public Long getCountByCondition(String sql, T obj) {
		SqlParamsObject sqlParams = getDynamicSqlParams(sql, obj, true);
		if (sqlParams != null) {
			return getCountBySQLQuery(sqlParams.getSql(), sqlParams.getParams());
		}
		return Long.valueOf(0L);
	}

	public Long getCountBySQLQuery(String sql, Object[] params) {
		return Long.valueOf(((BigDecimal) getSQLQuery(sessionFactory.getCurrentSession(), sql,
				params, null, false).list().iterator().next()).longValue());
	}

	public String getStaticSql(String sqlName) {
		return this.SQL_MAPPER.getProperty(sqlName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findSingleByCondition(String sql, T obj) {
		 Iterator it = findByCondition(sql, obj).iterator();
		    if (it.hasNext()) {
		      return (T)it.next();
		    }
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findSingleByConditionToMap(String sql, T obj) {
		Iterator it = findByConditionToMap(sql, obj).iterator();
		if (it.hasNext()) {
			return (Map) it.next();
		}
		return null;
	}
}
