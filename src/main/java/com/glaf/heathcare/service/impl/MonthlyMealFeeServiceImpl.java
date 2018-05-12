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

package com.glaf.heathcare.service.impl;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.MonthlyMealFeeService;

@Service("com.glaf.heathcare.service.monthlyMealFeeService")
@Transactional(readOnly = true)
public class MonthlyMealFeeServiceImpl implements MonthlyMealFeeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MonthlyMealFeeMapper monthlyMealFeeMapper;

	public MonthlyMealFeeServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MonthlyMealFee> list) {
		for (MonthlyMealFee monthlyMealFee : list) {
			if (monthlyMealFee.getId() == 0) {
				monthlyMealFee.setId(idGenerator.nextId("HEALTH_MONTHLY_MEAL_FEE"));
				monthlyMealFee.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<MonthlyMealFee> rows = new ArrayList<MonthlyMealFee>(batch_size);

		for (MonthlyMealFee bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					monthlyMealFeeMapper.bulkInsertMonthlyMealFee_oracle(rows);
				} else {
					monthlyMealFeeMapper.bulkInsertMonthlyMealFee(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				monthlyMealFeeMapper.bulkInsertMonthlyMealFee_oracle(rows);
			} else {
				monthlyMealFeeMapper.bulkInsertMonthlyMealFee(rows);
			}
			rows.clear();
		}
	}

	public int count(MonthlyMealFeeQuery query) {
		return monthlyMealFeeMapper.getMonthlyMealFeeCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			monthlyMealFeeMapper.deleteMonthlyMealFeeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				monthlyMealFeeMapper.deleteMonthlyMealFeeById(id);
			}
		}
	}

	public MonthlyMealFee getMonthlyMealFee(Long id) {
		if (id == null) {
			return null;
		}
		MonthlyMealFee monthlyMealFee = monthlyMealFeeMapper.getMonthlyMealFeeById(id);
		return monthlyMealFee;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMonthlyMealFeeCountByQueryCriteria(MonthlyMealFeeQuery query) {
		return monthlyMealFeeMapper.getMonthlyMealFeeCount(query);
	}

	public List<MonthlyMealFee> getMonthlyMealFees(String tenantId, int year, int month) {
		MonthlyMealFeeQuery query = new MonthlyMealFeeQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		List<MonthlyMealFee> list = monthlyMealFeeMapper.getMonthlyMealFees(query);
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MonthlyMealFee> getMonthlyMealFeesByQueryCriteria(int start, int pageSize, MonthlyMealFeeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MonthlyMealFee> rows = sqlSessionTemplate.selectList("getMonthlyMealFees", query, rowBounds);
		return rows;
	}

	public List<MonthlyMealFee> list(MonthlyMealFeeQuery query) {
		List<MonthlyMealFee> list = monthlyMealFeeMapper.getMonthlyMealFees(query);
		return list;
	}

	@Transactional
	public void save(MonthlyMealFee monthlyMealFee) {
		if (monthlyMealFee.getId() == 0) {
			monthlyMealFee.setId(idGenerator.nextId("HEALTH_MONTHLY_MEAL_FEE"));
			monthlyMealFee.setCreateTime(new Date());
			monthlyMealFeeMapper.insertMonthlyMealFee(monthlyMealFee);
		} else {
			monthlyMealFeeMapper.updateMonthlyMealFee(monthlyMealFee);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MonthlyMealFeeMapper")
	public void setMonthlyMealFeeMapper(MonthlyMealFeeMapper monthlyMealFeeMapper) {
		this.monthlyMealFeeMapper = monthlyMealFeeMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
