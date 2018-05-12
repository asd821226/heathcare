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

import java.util.*;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.WeeklyActualQuantityModel;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.FoodCompositionService;

public class WeekMultiSheetGoodsActualQuantityPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(Tenant tenant, int year, int month, Map<String, Object> params) {
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			int startFullDay = DateUtils.getYearMonthDay(startDate);
			int endFullDay = DateUtils.getYearMonthDay(endDate);
			String tenantId = tenant.getTenantId();

			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.GOODSID_ as goodsid, sum(QUANTITY_) as  quantity  from GOODS_ACTUAL_QUANTITY")
					.append(String.valueOf(IdentityFactory.getTenantHash(tenantId))).append(" E ")
					.append(" where ( E.TENANTID_ = '").append(tenantId).append("' ) ")
					.append(" and ( E.FULLDAY_ = #{fullday:int} ) ").append(" and ( E.BUSINESSSTATUS_ = 9 ) ")
					.append(" group by E.GOODSID_").append(" order by E.NODESORT_ asc ");
			ITablePageService tablePageService = ContextFactory.getBean("tablePageService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			FoodCompositionQuery query = new FoodCompositionQuery();
			query.locked(0);
			List<FoodComposition> foods = foodCompositionService.list(query);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}
			int pageNo = 1;
			List<String> sheetNames = new ArrayList<String>();
			List<WeeklyActualQuantityModel> weekList = new ArrayList<WeeklyActualQuantityModel>();
			for (int fullday = startFullDay; fullday <= endFullDay; fullday++) {
				params.put("fullday", fullday);
				List<Map<String, Object>> list = tablePageService.getListData(sqlBuffer.toString(), params);
				List<GoodsActualQuantity> rows = new ArrayList<GoodsActualQuantity>();
				if (list != null && !list.isEmpty()) {
					FoodComposition food = null;
					for (Map<String, Object> dataMap : list) {
						GoodsActualQuantity m = new GoodsActualQuantity();
						m.setGoodsId(ParamUtils.getLong(dataMap, "goodsid"));
						m.setQuantity(ParamUtils.getDouble(dataMap, "quantity"));
						if (foodMap.get(m.getGoodsId()) != null) {
							food = foodMap.get(m.getGoodsId());
							m.setGoodsName(food.getName());
							m.setGoodsNodeId(food.getNodeId());
							rows.add(m);
						}
					}
				}

				if (!rows.isEmpty()) {
					List<GoodsActualQuantity> rows1 = new ArrayList<GoodsActualQuantity>();
					List<GoodsActualQuantity> rows2 = new ArrayList<GoodsActualQuantity>();
					List<GoodsActualQuantity> rows3 = new ArrayList<GoodsActualQuantity>();
					for (GoodsActualQuantity m : rows) {
						switch ((int) m.getGoodsNodeId()) {
						case 4405:
						case 4406:
						case 4407:
							rows1.add(m);
							break;
						case 4409:
						case 4410:
						case 4411:
						case 4412:
						case 4413:
							rows2.add(m);
							break;
						default:
							rows3.add(m);
							break;
						}
					}

					int max = Math.max(rows1.size(), rows2.size());
					max = Math.max(max, rows3.size());
					if (max < 20) {
						max = Math.max(max, 20);
					}

					for (int i = 0, len = rows1.size(); i < max - len; i++) {
						GoodsActualQuantity m = new GoodsActualQuantity();
						rows1.add(m);
					}
					for (int i = 0, len = rows2.size(); i < max - len; i++) {
						GoodsActualQuantity m = new GoodsActualQuantity();
						rows2.add(m);
					}
					for (int i = 0, len = rows3.size(); i < max - len; i++) {
						GoodsActualQuantity m = new GoodsActualQuantity();
						rows3.add(m);
					}

					WeeklyActualQuantityModel model = new WeeklyActualQuantityModel();
					model.setPageNo(pageNo++);
					model.setRows1(rows1);
					model.setRows2(rows2);
					model.setRows3(rows3);
					model.setDateString(DateUtils.getDate(DateUtils.toDate(String.valueOf(fullday))));
					weekList.add(model);
					sheetNames.add(model.getDateString());

				}
			}

			params.put("weekList", weekList);
			params.put("sheetNames", sheetNames);
			params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startDate));
			params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endDate));
		}
	}

}
