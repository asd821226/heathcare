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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.ITableDataService;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.MedicalExaminationHelper;
import com.glaf.heathcare.mapper.MedicalExaminationMapper;
import com.glaf.heathcare.query.GrowthStandardQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.util.MedicalExaminationCache;

@Service("com.glaf.heathcare.service.medicalExaminationService")
@Transactional(readOnly = true)
public class MedicalExaminationServiceImpl implements MedicalExaminationService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GrowthStandardService growthStandardService;

	protected MedicalExaminationMapper medicalExaminationMapper;

	protected PersonService personService;

	protected ITableDataService tableDataService;

	public MedicalExaminationServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MedicalExamination> list) {
		GrowthStandardQuery query = new GrowthStandardQuery();
		List<GrowthStandard> rows = growthStandardService.list(query);
		Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
		if (rows != null && !rows.isEmpty()) {
			for (GrowthStandard gs : rows) {
				gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
			}
		}

		Calendar calendar = Calendar.getInstance();
		MedicalExaminationHelper helper = new MedicalExaminationHelper();
		for (MedicalExamination medicalExamination : list) {
			MedicalExaminationCache.removePrefix(medicalExamination.getTenantId());
			helper.evaluate(gsMap, medicalExamination);
			if (medicalExamination.getId() == 0) {
				medicalExamination.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAMINATION"));
				medicalExamination.setCreateTime(new Date());
				calendar.setTime(new Date());
				if (medicalExamination.getCheckDate() != null) {
					calendar.setTime(medicalExamination.getCheckDate());
				}
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				medicalExamination.setYear(year);
				medicalExamination.setMonth(month);
				medicalExaminationMapper.insertMedicalExamination(medicalExamination);
			}
		}
	}

	public int count(MedicalExaminationQuery query) {
		return medicalExaminationMapper.getMedicalExaminationCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			medicalExaminationMapper.deleteMedicalExaminationById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				medicalExaminationMapper.deleteMedicalExaminationById(id);
			}
		}
	}

	/**
	 * 获取最近一次检查记录
	 * 
	 * @param personId
	 * @return
	 */
	public MedicalExamination getLatestMedicalExamination(String personId) {
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		query.personId(personId);
		query.setOrderBy(" E.CHECKDATE_ desc ");

		List<MedicalExamination> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public MedicalExamination getMedicalExamination(long id) {
		if (id == 0) {
			return null;
		}
		MedicalExamination medicalExamination = medicalExaminationMapper.getMedicalExaminationById(id);
		return medicalExamination;
	}

	public int getMedicalExaminationCount(String tenantId, String checkId) {
		MedicalExaminationQuery query = new MedicalExaminationQuery();
		query.tenantId(tenantId);
		query.checkId(checkId);
		return medicalExaminationMapper.getMedicalExaminationCount(query);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalExaminationCountByQueryCriteria(MedicalExaminationQuery query) {
		return medicalExaminationMapper.getMedicalExaminationCount(query);
	}

	public List<MedicalExaminationCount> getMedicalExaminationIndexs(MedicalExaminationQuery query) {
		return medicalExaminationMapper.getMedicalExaminationIndexs(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalExamination> getMedicalExaminationsByQueryCriteria(int start, int pageSize,
			MedicalExaminationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalExamination> rows = sqlSessionTemplate.selectList("getMedicalExaminations", query, rowBounds);
		return rows;
	}

	public List<MedicalExamination> list(MedicalExaminationQuery query) {
		List<MedicalExamination> list = medicalExaminationMapper.getMedicalExaminations(query);
		return list;
	}

	@Transactional
	public void save(MedicalExamination medicalExamination) {
		MedicalExaminationCache.removePrefix(medicalExamination.getTenantId());
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.ageOfTheMoon(medicalExamination.getAgeOfTheMoon());
		query.sex(medicalExamination.getSex());
		List<GrowthStandard> rows = growthStandardService.list(query);
		Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
		if (rows != null && !rows.isEmpty()) {
			for (GrowthStandard gs : rows) {
				gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
			}
			MedicalExaminationHelper helper = new MedicalExaminationHelper();
			helper.evaluate(gsMap, medicalExamination);
		}

		if (medicalExamination.getId() == 0) {
			medicalExamination.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAMINATION"));
			medicalExamination.setCreateTime(new Date());

			if (medicalExamination.getCheckDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(medicalExamination.getCheckDate());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				medicalExamination.setYear(year);
				medicalExamination.setMonth(month);
			}

			medicalExaminationMapper.insertMedicalExamination(medicalExamination);
		} else {

			if (medicalExamination.getCheckDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(medicalExamination.getCheckDate());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				medicalExamination.setYear(year);
				medicalExamination.setMonth(month);
			}

			medicalExaminationMapper.updateMedicalExamination(medicalExamination);
		}
		if ("1".equals(medicalExamination.getType())) {// 入园体检
			Person person = personService.getPerson(medicalExamination.getPersonId());
			if (person.getHeight() == 0 && medicalExamination.getHeight() > 0) {
				person.setHeight(medicalExamination.getHeight());
			}
			if (person.getWeight() == 0 && medicalExamination.getWeight() > 0) {
				person.setWeight(medicalExamination.getWeight());
			}
			personService.update(person);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalExaminationMapper")
	public void setMedicalExaminationMapper(MedicalExaminationMapper medicalExaminationMapper) {
		this.medicalExaminationMapper = medicalExaminationMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@Transactional
	public void updateAllEvaluate(List<MedicalExamination> medicalExaminations) {
		for (MedicalExamination medicalExamination : medicalExaminations) {
			medicalExaminationMapper.updateMedicalExaminationEvaluate(medicalExamination);
		}
	}

}
