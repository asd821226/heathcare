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
package com.glaf.heathcare.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonSickness;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.query.PersonSicknessQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.PersonSicknessService;

public class InfectiousDiseaseListPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PersonSicknessService personSicknessService = ContextFactory
				.getBean("com.glaf.heathcare.service.personSicknessService");
		PersonQuery query = new PersonQuery();
		query.tenantId(tenant.getTenantId());
		query.locked(0);
		query.deleteFlag(0);
		List<Person> persons = personService.list(query);

		GradeInfoQuery qx = new GradeInfoQuery();
		qx.tenantId(tenant.getTenantId());
		qx.locked(0);
		qx.deleteFlag(0);
		List<GradeInfo> grades = gradeInfoService.list(qx);

		PersonSicknessQuery q = new PersonSicknessQuery();
		q.tenantId(tenant.getTenantId());
		q.year(year);

		if (month > 0) {
			q.month(month);
		}

		if (params.get("gradeId") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "gradeId"))) {
			q.gradeId(ParamUtils.getString(params, "gradeId"));
		}

		if (params.get("infectiousFlag") != null
				&& StringUtils.isNotEmpty(ParamUtils.getString(params, "infectiousFlag"))) {
			q.infectiousFlag(ParamUtils.getString(params, "infectiousFlag"));
		}

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person p : persons) {
				personMap.put(p.getId(), p);
			}
		}

		Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
		if (grades != null && !grades.isEmpty()) {
			for (GradeInfo g : grades) {
				gradeMap.put(g.getId(), g);
			}
		}

		List<PersonSickness> list = personSicknessService.list(q);
		if (list != null && !list.isEmpty()) {
			Person person = null;
			GradeInfo gradeInfo = null;
			for (PersonSickness sick : list) {
				person = personMap.get(sick.getPersonId());
				if (person != null) {
					sick.setName(person.getName());
					if (StringUtils.equals(person.getSex(), "1")) {
						sick.setSex("男");
					} else {
						sick.setSex("女");
					}
					gradeInfo = gradeMap.get(person.getGradeId());
					if (gradeInfo != null) {
						sick.setGradeId(gradeInfo.getId());
						sick.setGradeName(gradeInfo.getName());
					}
				}
			}

			params.put("rows", list);
		}
	}

}
