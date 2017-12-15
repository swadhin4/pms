/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.jpa.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.ejb.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.ClassUtils;

/**
 * The Class GenericQueryExecutorDAOImpl.
 * 
 * 
 */
@Repository
public class GenericQueryExecutorDAOImpl implements GenericQueryExecutorDAO {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(GenericQueryExecutorDAOImpl.class);

  /** The entity manager. */
  @PersistenceContext(unitName = "springJpaPersistenceUnit")
  private EntityManager entityManager;

  /**
   * Execute query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param clazz
   *          the clazz
   * @return the list
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#executeQuery
   *      (java.lang.String, java.lang.Class)
   */
  @Override
  public <T> List<T> executeQuery(final String ejbql, final Class<T> clazz) {
    return executeQuery(ejbql, clazz, null);
  }

  /**
   * Execute query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param clazz
   *          the clazz
   * @param params
   *          the params
   * @return the list
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#executeQuery
   *      (java.lang.String, java.lang.Class, java.util.Map)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> executeQuery(final String ejbql, final Class<T> clazz, final Map<String, Object> params) {
    LOGGER.debug("Executing Query.." + ejbql);
    Query query = entityManager.createQuery(ejbql, clazz);
    setParameters(query, params);
    return query.getResultList();
  }

  /**
   * Execute query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param clazz
   *          the clazz
   * @param page
   *          the page
   * @param pageSize
   *          the page size
   * @return the page
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#executeQuery
   *      (java.lang.String, java.lang.Class, int, int)
   */
  @Override
  public <T> Page<T> executeQuery(final String ejbql, final Class<T> clazz, final int page, final int pageSize) {
    return executeQuery(ejbql, clazz, null, page, pageSize);
  }

  /**
   * Execute query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param clazz
   *          the clazz
   * @param params
   *          the params
   * @param page
   *          the page
   * @param pageSize
   *          the page size
   * @return the page
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#executeQuery
   *      (java.lang.String, java.lang.Class, java.util.Map, int, int)
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> Page<T> executeQuery(final String ejbql, final Class<T> clazz, final Map<String, Object> params,
      final int page, final int pageSize) {
    LOGGER.debug("Executing Query.." + ejbql);
    int index = StringUtils.indexOfIgnoreCase(ejbql, "order ");
    String countEjbql = ejbql;
    if (index > 0) {
      countEjbql = StringUtils.substring(ejbql, 0, index);
    }
    LOGGER.debug("Executing Count Query.." + countEjbql);
    Query countQuery = entityManager.createQuery("select count(*) " + countEjbql);
    setParameters(countQuery, params);

    Long total = (Long) countQuery.getSingleResult();
    Query query = entityManager.createQuery(ejbql, clazz);
    setParameters(query, params);
    int first = (page - 1) * pageSize;
    query.setFirstResult(first);
    query.setMaxResults(pageSize);
    List<T> collection = query.getResultList();
    PageRequest pageRequest = new PageRequest(page - 1, pageSize);
    Page<T> finalResult = new PageImpl<T>(collection, pageRequest, total);
    return finalResult;
  }

  /**
   * Sets the parameters.
   * 
   * @param query
   *          the query
   * @param params
   *          the params
   */
  private void setParameters(final Query query, final Map<String, Object> params) {
    if (MapUtils.isNotEmpty(params)) {
      for (Map.Entry<String, Object> substitute : params.entrySet()) {
        if (ClassUtils.isAssignableValue(Date.class, substitute.getValue())) {
          query.setParameter(substitute.getKey(), (Date) substitute.getValue(), TemporalType.DATE);
        } else {
          query.setParameter(substitute.getKey(), substitute.getValue());
        }
      }
    }
    // FIXME. 
    query.setHint(QueryHints.HINT_CACHEABLE, "true");
  }

  /**
   * Execute projected query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @return the list
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#
   *      executeProjectedQuery(java.lang.String)
   */
  @Override
  public <T> List<T> executeProjectedQuery(final String ejbql) {
    return executeProjectedQuery(ejbql, null);

  }

  /**
   * Execute projected query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param params
   *          the params
   * @return the list
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#
   *      executeProjectedQuery(java.lang.String, java.util.Map)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> executeProjectedQuery(final String ejbql, final Map<String, Object> params) {
    Query query = entityManager.createQuery(ejbql);
    setParameters(query, params);
    return query.getResultList();

  }

  /**
   * Find count.
   * 
   * @param ejbql
   *          the ejbql
   * @param params
   *          the params
   * @return the long
   * @see com.pms.jpa.repositories.GenericQueryExecutorDAO#findCount(java.lang.String,
   *      java.util.Map)
   */
  @Override
  public long findCount(final String ejbql, final Map<String, Object> params) {
    Query query = entityManager.createQuery(ejbql);
    setParameters(query, params);
    long count = (Long) query.getSingleResult();
    return count;

  }

  /**
   * Execute projected query.
   * 
   * @param <T>
   *          the generic type
   * @param ejbql
   *          the ejbql
   * @param params
   *          the params
   * @return the list
   * @see com.telenoetica.jpa.repositories.GenericQueryExecutorDAO#
   *      executeProjectedQuery(java.lang.String, java.util.Map)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> executeSQLProjectedQuery(final String sqlString, final Map<String, Object> params) {
    Query query = entityManager.createNativeQuery(sqlString);
    setParameters(query, params);
    return query.getResultList();
  }


}