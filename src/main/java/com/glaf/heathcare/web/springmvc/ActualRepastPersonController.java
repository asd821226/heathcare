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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonInfoService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/actualRepastPerson")
@RequestMapping("/heathcare/actualRepastPerson")
public class ActualRepastPersonController {
	protected static final Log logger = LogFactory.getLog(ActualRepastPersonController.class);

	protected DictoryService dictoryService;

	protected ActualRepastPersonService actualRepastPersonService;

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonInfoService personInfoService;

	public ActualRepastPersonController() {

	}

	@RequestMapping("/batchEdit")
	public ModelAndView batchEdit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		Date repastDate = RequestUtils.getDate(request, "repastDate");
		logger.debug("repastDate:" + repastDate);
		if (repastDate != null) {
			List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
			request.setAttribute("dictoryList", dictoryList);

			int fullDay = DateUtils.getYearMonthDay(repastDate);
			List<ActualRepastPerson> list = actualRepastPersonService.getActualRepastPersons(loginContext.getTenantId(),
					fullDay);
			Map<Integer, ActualRepastPerson> dataMap = new HashMap<Integer, ActualRepastPerson>();
			if (list != null && !list.isEmpty()) {
				for (ActualRepastPerson p : list) {
					dataMap.put(p.getAge(), p);
				}
			}

			Map<Integer, PersonInfo> planMap = new HashMap<Integer, PersonInfo>();
			List<PersonInfo> personInfos = personInfoService.getPersonInfos(loginContext.getTenantId());
			if (personInfos != null && !personInfos.isEmpty()) {
				for (PersonInfo p : personInfos) {
					planMap.put(p.getAge(), p);
				}
			}

			GradeInfoQuery query1 = new GradeInfoQuery();
			query1.tenantId(loginContext.getTenantId());
			query1.locked(0);
			List<GradeInfo> grades = gradeInfoService.list(query1);
			List<String> gradeIds = new ArrayList<String>();
			if (grades != null && !grades.isEmpty()) {
				for (GradeInfo grade : grades) {
					gradeIds.add(grade.getId());
				}
			}

			PersonQuery query2 = new PersonQuery();
			query2.tenantId(loginContext.getTenantId());
			query2.gradeIds(gradeIds);
			query2.locked(0);

			List<Person> persons = personService.list(query2);
			Map<String, Object> maleMap = new HashMap<String, Object>();
			Map<String, Object> femaleMap = new HashMap<String, Object>();
			for (int age = 3; age <= 6; age++) {
				maleMap.put(String.valueOf(age), 0);
				femaleMap.put(String.valueOf(age), 0);
			}
			if (persons != null && !persons.isEmpty()) {
				for (Person person : persons) {
					int age = person.getAge();
					if (age < 3) {
						age = 3;
					}
					if (age >= 3 && age <= 6) {
						if (StringUtils.equals(person.getSex(), "1")) {
							Integer cnt = (Integer) maleMap.get(String.valueOf(age));
							if (cnt != null) {
								cnt = cnt + 1;
								maleMap.put(String.valueOf(age), cnt);
							}
						} else {
							Integer cnt = (Integer) femaleMap.get(String.valueOf(age));
							if (cnt != null) {
								cnt = cnt + 1;
								femaleMap.put(String.valueOf(age), cnt);
							}
						}
					}
				}
			}

			List<ActualRepastPerson> rows = new ArrayList<ActualRepastPerson>();
			for (int age = 3; age <= 6; age++) {
				ActualRepastPerson model = dataMap.get(age);
				if (model == null) {
					model = new ActualRepastPerson();
					model.setAge(age);
				}
				PersonInfo p = planMap.get(age);
				if (p != null) {
					model.setMalePlan(p.getMale());
					model.setMalePark(ParamUtils.getInt(maleMap, String.valueOf(age)));
					model.setFemalePlan(p.getFemale());
					model.setFemalePark(ParamUtils.getInt(femaleMap, String.valueOf(age)));
					model.setClassType(p.getClassType());
				}
				rows.add(model);
			}

			request.setAttribute("rows", rows);
			request.setAttribute("repastDate", repastDate);
		}

		request.removeAttribute("privilege_write");
		if (loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("privilege_write", true);
		}

		String x_view = ViewProperties.getString("actualRepastPerson.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/actualRepastPerson/batchEdit", modelMap);
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
					ActualRepastPerson actualRepastPerson = actualRepastPersonService
							.getActualRepastPerson(String.valueOf(x));
					if (actualRepastPerson != null) {
						if (StringUtils.equals(actualRepastPerson.getTenantId(), loginContext.getTenantId())
								&& (loginContext.getRoles().contains("TenantAdmin")
										|| loginContext.getRoles().contains("HealthPhysician")
										|| loginContext.getRoles().contains("AttendanceManagement"))) {
							if ((DateUtils.getDaysBetween(
									DateUtils.toDate(String.valueOf(actualRepastPerson.getFullDay())),
									new Date())) > 30) {
								return ResponseUtils.responseJsonResult(false, "只能删除一月内的数据。");
							}
							actualRepastPersonService.deleteById(actualRepastPerson.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			ActualRepastPerson actualRepastPerson = actualRepastPersonService.getActualRepastPerson(String.valueOf(id));
			if (actualRepastPerson != null) {
				if (StringUtils.equals(actualRepastPerson.getTenantId(), loginContext.getTenantId())
						&& (loginContext.getRoles().contains("TenantAdmin")
								|| loginContext.getRoles().contains("HealthPhysician")
								|| loginContext.getRoles().contains("AttendanceManagement"))) {
					if ((DateUtils.getDaysBetween(DateUtils.toDate(String.valueOf(actualRepastPerson.getFullDay())),
							new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "只能删除一月内的数据。");
					}
					actualRepastPersonService.deleteById(actualRepastPerson.getId());
				}
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		ActualRepastPerson actualRepastPerson = actualRepastPersonService
				.getActualRepastPerson(request.getParameter("id"));
		if (actualRepastPerson != null) {
			request.setAttribute("actualRepastPerson", actualRepastPerson);
			request.setAttribute("repastDate", DateUtils.toDate(String.valueOf(actualRepastPerson.getFullDay())));
		} else {
			request.setAttribute("repastDate", new Date());
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("actualRepastPerson.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/actualRepastPerson/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ActualRepastPersonQuery query = new ActualRepastPersonQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		} else {
			String tenantId = request.getParameter("tenantId");
			query.tenantId(tenantId);
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

		JSONObject result = new JSONObject();
		int total = actualRepastPersonService.getActualRepastPersonCountByQueryCriteria(query);
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

			List<ActualRepastPerson> list = actualRepastPersonService.getActualRepastPersonsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
				Map<String, String> nameMap = new HashMap<String, String>();
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory dict : dictoryList) {
						nameMap.put(dict.getCode(), dict.getName());
					}
				}

				for (ActualRepastPerson actualRepastPerson : list) {
					JSONObject rowJSON = actualRepastPerson.toJsonObject();
					rowJSON.put("id", actualRepastPerson.getId());
					rowJSON.put("rowId", actualRepastPerson.getId());
					rowJSON.put("actualRepastPersonId", actualRepastPerson.getId());
					rowJSON.put("startIndex", ++start);
					if (nameMap.get(actualRepastPerson.getClassType()) != null) {
						rowJSON.put("classTypeName", nameMap.get(actualRepastPerson.getClassType()));
					}
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

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/actualRepastPerson/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("actualRepastPerson.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/actualRepastPerson/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ActualRepastPersonQuery query = new ActualRepastPersonQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}
		query.tenantId(tenantId);

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

		JSONObject result = new JSONObject();
		int total = actualRepastPersonService.getActualRepastPersonCountByQueryCriteria(query);
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

			List<ActualRepastPerson> list = actualRepastPersonService.getActualRepastPersonsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
				Map<String, String> nameMap = new HashMap<String, String>();
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory dict : dictoryList) {
						nameMap.put(dict.getCode(), dict.getName());
					}
				}

				for (ActualRepastPerson actualRepastPerson : list) {
					JSONObject rowJSON = actualRepastPerson.toJsonObject();
					rowJSON.put("id", actualRepastPerson.getId());
					rowJSON.put("rowId", actualRepastPerson.getId());
					rowJSON.put("actualRepastPersonId", actualRepastPerson.getId());
					rowJSON.put("startIndex", ++start);
					if (nameMap.get(actualRepastPerson.getClassType()) != null) {
						rowJSON.put("classTypeName", nameMap.get(actualRepastPerson.getClassType()));
					}
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

	@RequestMapping("/reviewlist")
	public ModelAndView reviewlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/actualRepastPerson/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveActualRepastPerson")
	public byte[] saveActualRepastPerson(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("AttendanceManagement")) {
			String id = request.getParameter("id");
			ActualRepastPerson actualRepastPerson = null;
			try {
				if (StringUtils.isNotEmpty(id)) {
					actualRepastPerson = actualRepastPersonService.getActualRepastPerson(id);
				}

				Date repastDate = RequestUtils.getDate(request, "repastDate");

				List<PersonInfo> personInfos = personInfoService.getPersonInfos(loginContext.getTenantId());

				if (actualRepastPerson == null) {
					if ((DateUtils.getDaysBetween(repastDate, new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "只能新增一月内数据。");
					}
					actualRepastPerson = new ActualRepastPerson();
					actualRepastPerson.setCreateBy(actorId);
					actualRepastPerson.setTenantId(loginContext.getTenantId());
					actualRepastPerson.setOrganizationId(loginContext.getOrganizationId());
				} else {
					repastDate = DateUtils.toDate(String.valueOf(actualRepastPerson.getFullDay()));
					if (!StringUtils.equals(loginContext.getTenantId(), actualRepastPerson.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					if ((DateUtils.getDaysBetween(repastDate, new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "数据只能一月内修改。");
					}
				}

				Tools.populate(actualRepastPerson, params);
				actualRepastPerson.setMale(RequestUtils.getInt(request, "male"));
				actualRepastPerson.setFemale(RequestUtils.getInt(request, "female"));
				actualRepastPerson.setAge(RequestUtils.getInt(request, "age"));
				actualRepastPerson.setClassType(request.getParameter("classType"));
				actualRepastPerson.setSortNo(RequestUtils.getInt(request, "sortNo"));

				for (PersonInfo personInfo : personInfos) {
					if (actualRepastPerson.getAge() == personInfo.getAge()) {
						if (actualRepastPerson.getMale() > personInfo.getMale()) {
							return ResponseUtils.responseJsonResult(false, "实际就餐人数不能大于计划人数。");
						}
						if (actualRepastPerson.getFemale() > personInfo.getFemale()) {
							return ResponseUtils.responseJsonResult(false, "实际就餐人数不能大于计划人数。");
						}
					}
				}

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(repastDate);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				actualRepastPerson.setYear(year);
				actualRepastPerson.setMonth(month);
				actualRepastPerson.setDay(day);
				actualRepastPerson.setFullDay(DateUtils.getYearMonthDay(repastDate));

				this.actualRepastPersonService.save(actualRepastPerson);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveBatch")
	public byte[] saveBatch(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("AttendanceManagement")) {
			Date repastDate = RequestUtils.getDate(request, "repastDate");
			logger.debug("repastDate:" + repastDate);
			if (repastDate != null) {
				if ((DateUtils.getDaysBetween(repastDate, new Date())) > 30) {
					return ResponseUtils.responseJsonResult(false, "只能修改一月内数据。");
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(repastDate);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int fullDay = DateUtils.getYearMonthDay(repastDate);
				try {
					List<PersonInfo> personInfos = personInfoService.getPersonInfos(loginContext.getTenantId());
					if (personInfos != null && !personInfos.isEmpty()) {
						logger.debug("personInfos size:" + personInfos.size());
						List<ActualRepastPerson> rows = new ArrayList<ActualRepastPerson>();
						for (PersonInfo p : personInfos) {
							int male = RequestUtils.getInt(request, "male_" + p.getAge());
							int female = RequestUtils.getInt(request, "female_" + p.getAge());
							if (male > p.getMale()) {
								return ResponseUtils.responseJsonResult(false,
										p.getAge() + "岁男生人数不能超过" + p.getMale() + "。");
							}
							if (female > p.getFemale()) {
								return ResponseUtils.responseJsonResult(false,
										p.getAge() + "岁女生人数不能超过" + p.getFemale() + "。");
							}
							if ((male + female) > 0) {
								ActualRepastPerson arp = new ActualRepastPerson();
								arp.setAge(p.getAge());
								arp.setClassType(p.getClassType());
								arp.setCreateBy(actorId);
								arp.setYear(year);
								arp.setMale(month);
								arp.setDay(day);
								arp.setFullDay(fullDay);
								arp.setMale(male);
								arp.setFemale(female);
								arp.setTenantId(loginContext.getTenantId());
								rows.add(arp);
							}
						}
						actualRepastPersonService.saveAll(loginContext.getTenantId(), fullDay, rows);
						return ResponseUtils.responseJsonResult(true);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.actualRepastPersonService")
	public void setActualRepastPersonService(ActualRepastPersonService actualRepastPersonService) {
		this.actualRepastPersonService = actualRepastPersonService;
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personInfoService")
	public void setPersonInfoService(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
