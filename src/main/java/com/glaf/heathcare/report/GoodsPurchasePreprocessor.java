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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.Tenant;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPurchaseService;

public class GoodsPurchasePreprocessor implements IReportPreprocessor {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void prepare(Tenant tenant, Map<String, Object> params) {
		int year = ParamUtils.getInt(params, "year");
		int month = ParamUtils.getInt(params, "month");
		params.put("year", year);
		params.put("month", month);
		String tenantId = tenant.getTenantId();

		FoodCompositionService foodCompositionService = ContextFactory
				.getBean("com.glaf.heathcare.service.foodCompositionService");
		GoodsPurchaseService goodsPurchaseService = ContextFactory
				.getBean("com.glaf.heathcare.service.goodsPurchaseService");
		FoodCompositionQuery query = new FoodCompositionQuery();
		query.locked(0);
		List<FoodComposition> foods = foodCompositionService.list(query);
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		GoodsPurchaseQuery q = new GoodsPurchaseQuery();
		q.tenantId(tenantId);
		q.businessStatus(9);
		Date startTime = ParamUtils.getDate(params, "startTime");
		Date endTime = ParamUtils.getDate(params, "endTime");
		if (startTime != null) {
			q.purchaseTimeGreaterThanOrEqual(startTime);
		}
		if (endTime != null) {
			q.purchaseTimeLessThanOrEqual(endTime);
		}

		Map<String, User> userMap = IdentityFactory.getUserMap(tenantId);
		List<GoodsPurchase> rows = goodsPurchaseService.list(q);

		if (rows != null && !rows.isEmpty()) {
			int startIndex = 1;
			User user = null;
			FoodComposition food = null;
			for (GoodsPurchase m : rows) {
				m.setStartIndex(startIndex++);
				food = foodMap.get(m.getGoodsId());
				if (food != null) {
					m.setGoodsName(food.getName());
				}
				user = userMap.get(m.getCreateBy());
				if (user != null) {

				}
			}
			params.put("rows", rows);
			params.put("items", rows);
			params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startTime));
			params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endTime));
		}

	}

}
