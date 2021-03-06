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

package com.glaf.chart.web.rest;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.chart.domain.Chart;
import com.glaf.chart.query.ChartQuery;
import com.glaf.chart.service.IChartService;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.Environment;
import com.glaf.core.domain.Database;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.service.ITreeModelService;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

@Controller("/rs/chart")
@Path("/rs/chart")
public class ChartResource {
	protected static Log logger = LogFactory.getLog(ChartResource.class);

	protected IChartService chartService;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	protected ITreeModelService treeModelService;

	@GET
	@POST
	@Path("checkSQL")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] checkSQL(@Context HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		JSONObject result = new JSONObject();
		String querySQL = request.getParameter("querySQL");
		if (StringUtils.isNotEmpty(querySQL)) {
			if (!DBUtils.isLegalQuerySql(querySQL)) {
				return ResponseUtils.responseJsonResult(false, "SQL查询不合法！");
			}

			if (!DBUtils.isAllowedSql(querySQL)) {
				return ResponseUtils.responseJsonResult(false, "SQL查询不合法！");
			}

			Map<String, Object> paramMap = new java.util.HashMap<String, Object>();

			querySQL = QueryUtils.replaceSQLVars(querySQL);
			querySQL = QueryUtils.replaceSQLParas(querySQL, paramMap);
			TableModel rowMode = new TableModel();
			rowMode.setSql(querySQL);

			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			long databaseId = RequestUtils.getLong(request, "databaseId");
			Database currentDB = config.getDatabase(loginContext, databaseId);
			String systemName = Environment.getCurrentSystemName();
			try {
				if (currentDB != null) {
					Environment.setCurrentSystemName(currentDB.getName());
				}
				List<Map<String, Object>> rows = tablePageService.getListData(querySQL, paramMap);
				if (rows != null && !rows.isEmpty()) {
					logger.debug("chart rows size:" + rows.size());
					JSONArray arrayJSON = new JSONArray();

					for (Map<String, Object> dataMap : rows) {
						JSONObject row = new JSONObject();
						Set<Entry<String, Object>> entrySet = dataMap.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String name = entry.getKey();
							Object value = entry.getValue();
							if (value != null) {
								if (value instanceof Date) {
									Date d = (Date) value;
									row.put(name, DateUtils.getDate(d));
								} else if (value instanceof Boolean) {
									row.put(name, (Boolean) value);
								} else if (value instanceof Integer) {
									row.put(name, (Integer) value);
								} else if (value instanceof Long) {
									row.put(name, (Long) value);
								} else if (value instanceof Double) {
									row.put(name, (Double) value);
								} else {
									row.put(name, value.toString());
								}
							}
						}
						arrayJSON.add(row);
					}
					result.put("rows", arrayJSON);
					result.put("total", rows.size());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			} finally {
				com.glaf.core.config.Environment.setCurrentSystemName(systemName);
			}
		} else {
			return ResponseUtils.responseJsonResult(false, "SQL查询不合法！");
		}

		return result.toString().getBytes("UTF-8");
	}

	@POST
	@Path("/deleteAll")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] deleteAll(@Context HttpServletRequest request) throws IOException {
		String rowIds = request.getParameter("chartIds");
		if (rowIds != null) {
			List<String> ids = StringTools.split(rowIds);
			if (ids != null && !ids.isEmpty()) {
				chartService.deleteByIds(ids);
			}
		}
		return ResponseUtils.responseJsonResult(true);
	}

	@POST
	@Path("/delete/{chartIds}")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] deleteAll(@PathParam("chartIds") String chartIds, @Context HttpServletRequest request)
			throws IOException {
		if (chartIds != null) {
			List<String> ids = StringTools.split(chartIds);
			if (ids != null && !ids.isEmpty()) {
				chartService.deleteByIds(ids);
			}
		}
		return ResponseUtils.responseJsonResult(true);
	}

	@POST
	@Path("/delete")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] deleteById(@Context HttpServletRequest request) throws IOException {
		String chartId = request.getParameter("chartId");
		if (StringUtils.isEmpty(chartId)) {
			chartId = request.getParameter("id");
		}
		chartService.deleteById(chartId);
		return ResponseUtils.responseJsonResult(true);
	}

	@POST
	@Path("/delete/{chartId}")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] deleteById(@PathParam("chartId") String chartId, @Context HttpServletRequest request)
			throws IOException {
		chartService.deleteById(chartId);
		return ResponseUtils.responseJsonResult(true);
	}

	@GET
	@POST
	@Path("/list")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] list(@Context HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ChartQuery query = new ChartQuery();
		Tools.populate(query, params);

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "easyui";
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
		int total = chartService.getChartCountByQueryCriteria(query);
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

			List<Chart> list = chartService.getChartsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Chart chart : list) {
					JSONObject rowJSON = chart.toJsonObject();

					rowsJSON.add(rowJSON);
				}

			}
		}
		return result.toString().getBytes("UTF-8");
	}

	@POST
	@Path("/saveChart")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] saveChart(@Context HttpServletRequest request) {
		String chartId = request.getParameter("chartId");
		if (StringUtils.isEmpty(chartId)) {
			chartId = request.getParameter("id");
		}
		String name = request.getParameter("chartName");
		String mapping = request.getParameter("mapping");
		Chart chart = null;
		if (StringUtils.isNotEmpty(chartId)) {
			chart = chartService.getChart(chartId);
			if (chart != null) {
				if (StringUtils.isNotEmpty(name)) {
					Chart model = chartService.getChartByName(name);
					if (model != null && !StringUtils.equals(chart.getId(), model.getId())) {
						// return ResponseUtils.responseJsonResult(false, "图表名称已经存在，请换个名称！");
					}
				}

				if (StringUtils.isNotEmpty(mapping)) {
					Chart model = chartService.getChartByMapping(mapping);
					if (model != null && !StringUtils.equals(chart.getId(), model.getId())) {
						// return ResponseUtils.responseJsonResult(false, "图表别名已经存在，请换个别名！");
					}
				}
			}
		}

		if (chart == null) {
			chart = new Chart();

			if (StringUtils.isNotEmpty(name)) {
				Chart model = chartService.getChartByName(name);
				if (model != null) {
					// return ResponseUtils.responseJsonResult(false, "图表名称已经存在，请换个名称！");
				}
			}

			if (StringUtils.isNotEmpty(mapping)) {
				Chart model = chartService.getChartByMapping(mapping);
				if (model != null) {
					// return ResponseUtils.responseJsonResult(false, "图表别名已经存在，请换个别名！");
				}
			}

		}

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		Tools.populate(chart, params);

		String queryIds = request.getParameter("queryIds");
		chart.setQueryIds(queryIds);

		this.chartService.save(chart);

		return ResponseUtils.responseJsonResult(true);
	}

	@javax.annotation.Resource
	public void setChartService(IChartService chartService) {
		this.chartService = chartService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	@javax.annotation.Resource
	public void setTreeModelService(ITreeModelService treeModelService) {
		this.treeModelService = treeModelService;
	}

	@GET
	@POST
	@Path("/treeJson")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] treeJson(@Context HttpServletRequest request) throws IOException {
		JSONArray array = new JSONArray();
		Long nodeId = RequestUtils.getLong(request, "nodeId");
		String nodeCode = request.getParameter("nodeCode");
		String selected = request.getParameter("selected");

		logger.debug(RequestUtils.getParameterMap(request));
		List<TreeModel> treeModels = new java.util.ArrayList<TreeModel>();
		List<String> chooseList = new java.util.ArrayList<String>();
		if (StringUtils.isNotEmpty(selected)) {
			chooseList = StringTools.split(selected);
		}

		TreeModel treeNode = null;

		if (nodeId != null && nodeId > 0) {
			treeNode = treeModelService.getTreeModel(nodeId);
		} else if (StringUtils.isNotEmpty(nodeCode)) {
			treeNode = treeModelService.getTreeModelByCode(nodeCode);
		}

		if (treeNode != null) {
			ChartQuery query = new ChartQuery();
			List<TreeModel> subTrees = treeModelService.getSubTreeModels(treeNode.getId());
			if (subTrees != null && !subTrees.isEmpty()) {
				for (TreeModel tree : subTrees) {
					tree.getDataMap().put("nocheck", "true");
					tree.getDataMap().put("iconSkin", "tree_folder");
					tree.getDataMap().put("isParent", "true");
					tree.setIconCls("folder");
					tree.setLevel(0);
					treeModels.add(tree);
					query.nodeId(tree.getId());
					List<Chart> charts = chartService.list(query);
					for (Chart chart : charts) {
						if (StringUtils.isNumeric(chart.getId())) {
							TreeModel t = new BaseTree();
							t.setId(Long.parseLong(chart.getId()));
							t.setParentId(tree.getId());
							t.setName(chart.getChartTitle());
							t.setCode(chart.getId());
							t.setTreeId(chart.getId());
							t.setIconCls("leaf");
							t.getDataMap().put("iconSkin", "tree_leaf");
							if (chooseList.contains(chart.getId())) {
								t.setChecked(true);
							}
							treeModels.add(t);
						}
					}
				}
			}
			TreeHelper treeHelper = new TreeHelper();
			array = treeHelper.getTreeJSONArray(treeModels);
		}

		return array.toJSONString().getBytes("UTF-8");
	}

	@GET
	@POST
	@Path("/view/{chartId}")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] view(@PathParam("chartId") String chartId, @Context HttpServletRequest request) throws IOException {
		Chart chart = null;
		if (StringUtils.isNotEmpty(chartId)) {
			chart = chartService.getChart(chartId);
		}
		JSONObject result = new JSONObject();
		if (chart != null) {
			result = chart.toJsonObject();
		}
		return result.toString().getBytes("UTF-8");
	}

}