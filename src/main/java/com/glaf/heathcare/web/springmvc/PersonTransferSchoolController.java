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

package com.glaf.heathcare.web.springmvc;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.alibaba.fastjson.*;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.*;
import com.glaf.core.util.*;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personTransferSchool")
@RequestMapping("/heathcare/personTransferSchool")
public class PersonTransferSchoolController {
	protected static final Log logger = LogFactory.getLog(PersonTransferSchoolController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonTransferSchoolService personTransferSchoolService;

	protected SysTenantService sysTenantService;

	public PersonTransferSchoolController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					PersonTransferSchool personTransferSchool = personTransferSchoolService
							.getPersonTransferSchool(String.valueOf(x));
					if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
							&& (StringUtils.equals(loginContext.getTenantId(), personTransferSchool.getTenantId()))) {
						personTransferSchoolService.deleteById(personTransferSchool.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			PersonTransferSchool personTransferSchool = personTransferSchoolService
					.getPersonTransferSchool(String.valueOf(id));
			if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
					&& (StringUtils.equals(loginContext.getTenantId(), personTransferSchool.getTenantId()))) {
				personTransferSchoolService.deleteById(personTransferSchool.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		GradeInfoQuery query = new GradeInfoQuery();
		query.deleteFlag(0);
		query.locked(0);
		query.tenantId(loginContext.getTenantId());
		try {
			List<GradeInfo> grades = gradeInfoService.list(query);
			request.setAttribute("grades", grades);

			SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
			String type = request.getParameter("type");
			if (StringUtils.isNotEmpty(type) && StringUtils.equals(type, "join")) {
				request.setAttribute("toSchool", tenant.getName());
			} else {
				request.setAttribute("fromSchool", tenant.getName());
			}

			PersonTransferSchool personTransferSchool = personTransferSchoolService
					.getPersonTransferSchool(request.getParameter("id"));
			if (personTransferSchool != null) {
				request.setAttribute("personTransferSchool", personTransferSchool);
				request.setAttribute("fromSchool", personTransferSchool.getFromSchool());
				request.setAttribute("toSchool", personTransferSchool.getToSchool());
				Person person = personService.getPerson(personTransferSchool.getPersonId());
				request.setAttribute("person", person);
				if (person != null) {
					List<Person> persons = personService.getPersons(person.getGradeId());
					request.setAttribute("persons", persons);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("personTransferSchool.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/personTransferSchool/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonTransferSchoolQuery query = new PersonTransferSchoolQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(type)) {
			query.type(type);
		} else {
			query.type("join");
		}

		JSONObject result = new JSONObject();

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			query.tenantId(loginContext.getTenantId());
		} else {
			return result.toJSONString().getBytes();
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		int total = personTransferSchoolService.getPersonTransferSchoolCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<PersonTransferSchool> list = personTransferSchoolService.getPersonTransferSchoolsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (PersonTransferSchool personTransferSchool : list) {
					JSONObject rowJSON = personTransferSchool.toJsonObject();
					rowJSON.put("id", personTransferSchool.getId());
					rowJSON.put("rowId", personTransferSchool.getId());
					rowJSON.put("personTransferSchoolId", personTransferSchool.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/personTransferSchool/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("personTransferSchool.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/personTransferSchool/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePersonTransferSchool")
	public byte[] savePersonTransferSchool(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonTransferSchool personTransferSchool = new PersonTransferSchool();
		try {
			Tools.populate(personTransferSchool, params);
			personTransferSchool.setTenantId(loginContext.getTenantId());
			personTransferSchool.setFromTenantId(request.getParameter("fromTenantId"));
			personTransferSchool.setFromSchool(request.getParameter("fromSchool"));
			personTransferSchool.setToTenantId(request.getParameter("toTenantId"));
			personTransferSchool.setToSchool(request.getParameter("toSchool"));
			personTransferSchool.setPersonId(request.getParameter("personId"));
			personTransferSchool.setName(request.getParameter("name"));
			personTransferSchool.setSex(request.getParameter("sex"));
			personTransferSchool.setLeaveDate(RequestUtils.getDate(request, "leaveDate"));
			personTransferSchool.setCheckDate(RequestUtils.getDate(request, "checkDate"));
			personTransferSchool.setCheckOrganization(request.getParameter("checkOrganization"));
			personTransferSchool.setCheckOrganizationId(RequestUtils.getLong(request, "checkOrganizationId"));
			personTransferSchool.setCheckResult(request.getParameter("checkResult"));
			personTransferSchool.setRemark(request.getParameter("remark"));
			personTransferSchool.setType(request.getParameter("type"));
			personTransferSchool.setCreateBy(actorId);
			Person person = personService.getPerson(personTransferSchool.getPersonId());
			if (person != null) {
				personTransferSchool.setName(person.getName());
				personTransferSchool.setSex(person.getSex());
			}
			this.personTransferSchoolService.save(personTransferSchool);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personTransferSchoolService")
	public void setPersonTransferSchoolService(PersonTransferSchoolService personTransferSchoolService) {
		this.personTransferSchoolService = personTransferSchoolService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
