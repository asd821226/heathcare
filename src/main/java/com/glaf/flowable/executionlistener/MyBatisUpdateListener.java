/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.flowable.executionlistener;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.common.impl.db.DbSqlSession;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.glaf.core.dao.MyBatisEntityDAO;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.Tools;

public class MyBatisUpdateListener implements ExecutionListener {
	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory.getLog(MyBatisUpdateListener.class);

	protected String className;

	protected String operation;

	protected String statementId;

	public void notify(DelegateExecution execution) {
		Map<String, Object> params = new java.util.HashMap<String, Object>();
		CommandContext commandContext = Context.getCommandContext();

		Map<String, Object> variables = execution.getVariables();
		if (variables != null && variables.size() > 0) {
			Set<Entry<String, Object>> entrySet = variables.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String name = entry.getKey();
				Object value = entry.getValue();
				if (name != null && value != null && params.get(name) == null) {
					params.put(name, value);
				}
			}
		}

		logger.debug("params:" + params);

		Class<?> clazz = ClassUtils.loadClass(className);
		Object object = null;
		try {
			object = clazz.newInstance();
			Tools.populate(object, params);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
		SqlSession sqlSession = dbSqlSession.getSqlSession();
		MyBatisEntityDAO entityDAO = new MyBatisEntityDAO(sqlSession);
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setOperation(operation);
		sqlExecutor.setStatementId(statementId);
		sqlExecutor.setParameter(object);
		entityDAO.execute(sqlExecutor);

	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

}