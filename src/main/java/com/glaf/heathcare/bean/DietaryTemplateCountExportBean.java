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

package com.glaf.heathcare.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;

import com.glaf.heathcare.domain.DietaryTemplateCount;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.DietaryWeeklyRptModel;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.query.DietaryTemplateCountQuery;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryTemplateCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;
import com.glaf.heathcare.util.NutritionEvaluateUtils;

public class DietaryTemplateCountExportBean {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected DietaryTemplateCountService dietaryCountService;

	protected DietaryTemplateService dietaryTemplateService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	protected FoodDRIService foodDRIService;

	protected FoodDRIPercentService foodDRIPercentService;

	public void fillBlank(List<DietaryRptModel> list, int size) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}

		if (total < size) {
			DietaryItem item = null;
			DietaryRptModel model = new DietaryRptModel();
			List<DietaryItem> items = new ArrayList<DietaryItem>();
			for (int i = 0; i < size - total; i++) {
				item = new DietaryItem();
				items.add(item);
			}
			model.setItems(items);
			list.add(model);
		}
	}

	public DietaryTemplateCountService getDietaryTemplateCountService() {
		if (dietaryCountService == null) {
			dietaryCountService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateCountService");
		}
		return dietaryCountService;
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryTemplateService getDietaryTemplateService() {
		if (dietaryTemplateService == null) {
			dietaryTemplateService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateService");
		}
		return dietaryTemplateService;
	}

	public FoodCompositionService getFoodCompositionService() {
		if (foodCompositionService == null) {
			foodCompositionService = ContextFactory.getBean("com.glaf.heathcare.service.foodCompositionService");
		}
		return foodCompositionService;
	}

	public FoodDRIPercentService getFoodDRIPercentService() {
		if (foodDRIPercentService == null) {
			foodDRIPercentService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIPercentService");
		}
		return foodDRIPercentService;
	}

	public FoodDRIService getFoodDRIService() {
		if (foodDRIService == null) {
			foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
		}
		return foodDRIService;
	}

	public int getSize(List<DietaryRptModel> list) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}
		return total;
	}

	protected void populate(DietaryDayRptModel m, FoodDRI foodDRI) {
		if (foodDRI != null) {
			m.setCalciumStandard(foodDRI.getCalcium());
			m.setHeatEnergyStandard(foodDRI.getHeatEnergy());
			m.setProteinStandard(foodDRI.getProtein());
		}
	}

	protected void populate(DietaryWeeklyRptModel m, FoodDRI foodDRI) {
		if (foodDRI != null) {
			m.setCalciumStandard(foodDRI.getCalcium());
			// m.setCarbohydrateStandard(foodDRI.getCarbohydrate());
			// m.setCaroteneStandard(foodDRI.getCarotene());
			// m.setFatStandard(foodDRI.getFat());
			m.setHeatEnergyStandard(foodDRI.getHeatEnergy());
			m.setIodineStandard(foodDRI.getIodine());
			m.setIronStandard(foodDRI.getIron());
			// m.setNicotinicCidStandard(foodDRI.getNicotinicCid());
			m.setPhosphorusStandard(foodDRI.getPhosphorus());
			m.setProteinStandard(foodDRI.getProtein());
			// m.setRetinolStandard(foodDRI.getRetinol());
			m.setVitaminAStandard(foodDRI.getVitaminA());
			m.setVitaminB1Standard(foodDRI.getVitaminB1());
			// m.setVitaminB12Standard(foodDRI.getVitaminB12());
			m.setVitaminB2Standard(foodDRI.getVitaminB2());
			// m.setVitaminB6Standard(foodDRI.getVitaminB6());
			m.setVitaminCStandard(foodDRI.getVitaminC());
			m.setVitaminEStandard(foodDRI.getVitaminE());
			m.setZincStandard(foodDRI.getZinc());
		}
	}

	public Map<String, Object> prepareData(LoginContext loginContext, String sysFlag, int suitNo, long typeIdX) {
		Map<String, Object> context = new HashMap<String, Object>();

		FoodDRI foodDRI = null;
		FoodDRIPercent foodDRIPercent = null;
		if (typeIdX > 0) {
			foodDRI = getFoodDRIService().getFoodDRIByAge(4);
			foodDRIPercent = getFoodDRIPercentService().getFoodDRIPercent("3-6", typeIdX);
			logger.debug("foodDRI:" + foodDRI);
			if (foodDRI != null && foodDRIPercent != null) {
				foodDRI.setCalcium(foodDRI.getCalcium() * foodDRIPercent.getCalcium());
				foodDRI.setCarbohydrate(foodDRI.getCarbohydrate() * foodDRIPercent.getCarbohydrate());
				foodDRI.setFat(foodDRI.getFat() * foodDRIPercent.getFat());
				foodDRI.setHeatEnergy(foodDRI.getHeatEnergy() * foodDRIPercent.getHeatEnergy());
				foodDRI.setIodine(foodDRI.getIodine() * foodDRIPercent.getIodine());
				foodDRI.setIron(foodDRI.getIron() * foodDRIPercent.getIron());
				foodDRI.setNicotinicCid(foodDRI.getNicotinicCid() * foodDRIPercent.getNicotinicCid());
				foodDRI.setPhosphorus(foodDRI.getPhosphorus() * foodDRIPercent.getPhosphorus());
				foodDRI.setProtein(foodDRI.getProtein() * foodDRIPercent.getProtein());
				foodDRI.setRetinol(foodDRI.getRetinol() * foodDRIPercent.getRetinol());
				foodDRI.setVitaminA(foodDRI.getVitaminA() * foodDRIPercent.getVitaminA());
				foodDRI.setVitaminB1(foodDRI.getVitaminB1() * foodDRIPercent.getVitaminB1());
				foodDRI.setVitaminB12(foodDRI.getVitaminB12() * foodDRIPercent.getVitaminB12());
				foodDRI.setVitaminB2(foodDRI.getVitaminB2() * foodDRIPercent.getVitaminB2());
				foodDRI.setVitaminB6(foodDRI.getVitaminB6() * foodDRIPercent.getVitaminB6());
				foodDRI.setVitaminC(foodDRI.getVitaminC() * foodDRIPercent.getVitaminC());
				foodDRI.setVitaminE(foodDRI.getVitaminE() * foodDRIPercent.getVitaminE());
				foodDRI.setZinc(foodDRI.getZinc() * foodDRIPercent.getZinc());
				context.put("foodDRI", foodDRI);
				context.put("foodDRIPercent", foodDRIPercent);
			}
		}

		logger.debug("--------------------DietaryTemplateCountQuery-------------");

		DietaryTemplateCountQuery xquery = new DietaryTemplateCountQuery();
		if (loginContext.getTenantId() != null) {
			if (!StringUtils.equals(sysFlag, "Y")) {
				xquery.tenantId(loginContext.getTenantId());
			}
		} else {
			xquery.tenantId("SYS");
		}
		xquery.suitNo(suitNo);
		xquery.type("ALL");

		List<DietaryTemplateCount> countList = getDietaryTemplateCountService().list(xquery);
		DietaryTemplateCount weekAgv = new DietaryTemplateCount();
		Map<Integer, DietaryTemplateCount> countMap = new HashMap<Integer, DietaryTemplateCount>();
		if (countList != null && !countList.isEmpty()) {
			double heatEnergy = 0.0;
			double heatEnergyCarbohydrate = 0.0;
			double heatEnergyFat = 0.0;
			double protein = 0.0;
			double proteinAnimal = 0.0;
			double proteinAnimalBeans = 0.0;
			double fat = 0.0;
			double carbohydrate = 0.0;
			double vitaminA = 0.0;
			double vitaminB1 = 0.0;
			double vitaminB2 = 0.0;
			double vitaminC = 0.0;
			double vitaminE = 0.0;
			double calcium = 0.0;
			double iron = 0.0;
			double zinc = 0.0;
			double iodine = 0.0;
			double phosphorus = 0.0;
			double nicotinicCid = 0.0;
			int size = countList.size();
			for (DietaryTemplateCount cnt : countList) {
				int dayOfWeek = cnt.getDayOfWeek();
				countMap.put(dayOfWeek, cnt);
				heatEnergy = heatEnergy + cnt.getHeatEnergy();
				heatEnergyCarbohydrate = heatEnergyCarbohydrate + cnt.getHeatEnergyCarbohydrate();
				heatEnergyFat = heatEnergyFat + cnt.getHeatEnergyFat();
				protein = protein + cnt.getProtein();
				proteinAnimal = proteinAnimal + cnt.getProteinAnimal();
				proteinAnimalBeans = proteinAnimalBeans + cnt.getProteinAnimalBeans();
				fat = fat + cnt.getFat();
				carbohydrate = carbohydrate + cnt.getCarbohydrate();
				vitaminA = vitaminA + cnt.getVitaminA();
				vitaminB1 = vitaminB1 + cnt.getVitaminB1();
				vitaminB2 = vitaminB2 + cnt.getVitaminB2();
				vitaminC = vitaminC + cnt.getVitaminC();
				vitaminE = vitaminE + cnt.getVitaminE();
				calcium = calcium + cnt.getCalcium();
				iron = iron + cnt.getIron();
				zinc = zinc + cnt.getZinc();
				iodine = iodine + cnt.getIodine();
				nicotinicCid = nicotinicCid + cnt.getNicotinicCid();
				phosphorus = phosphorus + cnt.getPhosphorus();
			}
			weekAgv.setHeatEnergy(heatEnergy / size);
			weekAgv.setHeatEnergyCarbohydrate(heatEnergyCarbohydrate / size);
			weekAgv.setHeatEnergyFat(heatEnergyFat / size);
			weekAgv.setProtein(protein / size);
			weekAgv.setProteinAnimal(proteinAnimal / size);
			weekAgv.setProteinAnimalBeans(proteinAnimalBeans / size);
			weekAgv.setFat(fat / size);
			weekAgv.setCarbohydrate(carbohydrate / size);
			weekAgv.setVitaminA(vitaminA / size);
			weekAgv.setVitaminB1(vitaminB1 / size);
			weekAgv.setVitaminB2(vitaminB2 / size);
			weekAgv.setVitaminC(vitaminC / size);
			weekAgv.setVitaminE(vitaminE / size);
			weekAgv.setCalcium(calcium / size);
			weekAgv.setIron(iron / size);
			weekAgv.setZinc(zinc / size);
			weekAgv.setIodine(iodine / size);
			weekAgv.setPhosphorus(phosphorus / size);
			weekAgv.setNicotinicCid(nicotinicCid / size);
			context.put("weekAgv", weekAgv);

			DietaryWeeklyRptModel weekRptModel = new DietaryWeeklyRptModel();
			weekRptModel.setHeatEnergy(heatEnergy / size);
			weekRptModel.setHeatEnergyCarbohydrate(heatEnergyCarbohydrate / size);
			weekRptModel.setHeatEnergyFat(heatEnergyFat / size);
			weekRptModel.setProtein(protein / size);
			weekRptModel.setProteinAnimal(proteinAnimal / size);
			weekRptModel.setProteinAnimalBeans(proteinAnimalBeans / size);
			weekRptModel.setVitaminA(vitaminA / size);
			weekRptModel.setVitaminB1(vitaminB1 / size);
			weekRptModel.setVitaminB2(vitaminB2 / size);
			weekRptModel.setVitaminC(vitaminC / size);
			weekRptModel.setVitaminE(vitaminE / size);
			weekRptModel.setCalcium(calcium / size);
			weekRptModel.setIron(iron / size);
			weekRptModel.setZinc(zinc / size);
			weekRptModel.setIodine(iodine / size);
			weekRptModel.setPhosphorus(phosphorus / size);
			weekRptModel.setNicotinicCid(nicotinicCid / size);
			context.put("weekRptModel", weekRptModel);
			NutritionEvaluateUtils.evaluate(weekRptModel, foodDRI, foodDRIPercent);
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (loginContext.getTenantId() != null) {
			query.sysFlag(sysFlag);
			if (!StringUtils.equals(sysFlag, "Y")) {
				query.tenantId(loginContext.getTenantId());
			}
		} else {
			query.sysFlag(sysFlag);
		}

		query.suitNo(suitNo);

		FoodCompositionQuery queryx = new FoodCompositionQuery();
		queryx.locked(0);
		List<FoodComposition> foods = getFoodCompositionService().list(queryx);
		Map<Long, Long> typeIdMap = new HashMap<Long, Long>();
		for (FoodComposition fd : foods) {
			typeIdMap.put(fd.getId(), fd.getNodeId());
		}

		List<DietaryTemplate> list = getDietaryTemplateService().list(query);
		if (list != null && !list.isEmpty()) {
			int dayOfWeek = 0;
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate dietary : list) {
				if (dayOfWeek == 0) {
					dayOfWeek = dietary.getDayOfWeek();
				}
				templateIds.add(dietary.getId());
			}
			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);

			List<DietaryItem> allItems = getDietaryItemService().list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				DietaryRptModel model = null;
				List<DietaryItem> items = null;

				Map<Long, List<DietaryItem>> dietaryMap = new HashMap<Long, List<DietaryItem>>();
				for (DietaryItem item : allItems) {
					items = dietaryMap.get(item.getDietaryId());
					if (items == null) {
						items = new ArrayList<DietaryItem>();
					}
					if (typeIdMap != null && typeIdMap.get(item.getFoodId()) != null) {
						item.setTypeId(typeIdMap.get(item.getFoodId()));
					}
					items.add(item);
					dietaryMap.put(item.getDietaryId(), items);
				}

				List<DietaryRptModel> breakfastList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList5 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList6 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList7 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> breakfastMidList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList5 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList6 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList7 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> lunchList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList5 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList6 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList7 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> snackList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList5 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList6 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList7 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> dinnerList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList5 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList6 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList7 = new ArrayList<DietaryRptModel>();

				List<DietaryDayRptModel> weekList = new ArrayList<DietaryDayRptModel>();

				DietaryDayRptModel day1 = new DietaryDayRptModel();
				day1.setFullDay(dayOfWeek);
				day1.setWeekName("星期一");
				day1.setBreakfastList(breakfastList1);
				day1.setBreakfastMidList(breakfastMidList1);
				day1.setLunchList(lunchList1);
				day1.setSnackList(snackList1);
				day1.setDinnerList(dinnerList1);
				this.populate(day1, foodDRI);
				// weekList.add(day1);

				DietaryDayRptModel day2 = new DietaryDayRptModel();
				day2.setFullDay(dayOfWeek);
				day2.setWeekName("星期二");
				day2.setBreakfastList(breakfastList2);
				day2.setBreakfastMidList(breakfastMidList2);
				day2.setLunchList(lunchList2);
				day2.setSnackList(snackList2);
				day2.setDinnerList(dinnerList2);
				this.populate(day2, foodDRI);
				// weekList.add(day2);

				DietaryDayRptModel day3 = new DietaryDayRptModel();
				day3.setFullDay(dayOfWeek);
				day3.setWeekName("星期三");
				day3.setBreakfastList(breakfastList3);
				day3.setBreakfastMidList(breakfastMidList3);
				day3.setLunchList(lunchList3);
				day3.setSnackList(snackList3);
				day3.setDinnerList(dinnerList3);
				this.populate(day3, foodDRI);
				// weekList.add(day3);

				DietaryDayRptModel day4 = new DietaryDayRptModel();
				day4.setFullDay(dayOfWeek);
				day4.setWeekName("星期四");
				day4.setBreakfastList(breakfastList4);
				day4.setBreakfastMidList(breakfastMidList4);
				day4.setLunchList(lunchList4);
				day4.setSnackList(snackList4);
				day4.setDinnerList(dinnerList4);
				this.populate(day4, foodDRI);
				// weekList.add(day4);

				DietaryDayRptModel day5 = new DietaryDayRptModel();
				day5.setFullDay(dayOfWeek);
				day5.setWeekName("星期五");
				day5.setBreakfastList(breakfastList5);
				day5.setBreakfastMidList(breakfastMidList5);
				day5.setLunchList(lunchList5);
				day5.setSnackList(snackList5);
				day5.setDinnerList(dinnerList5);
				this.populate(day5, foodDRI);
				// weekList.add(day5);

				DietaryDayRptModel day6 = new DietaryDayRptModel();
				day6.setFullDay(dayOfWeek);
				day6.setWeekName("星期六");
				day6.setBreakfastList(breakfastList6);
				day6.setBreakfastMidList(breakfastMidList6);
				day6.setLunchList(lunchList6);
				day6.setSnackList(snackList6);
				day6.setDinnerList(dinnerList6);
				this.populate(day6, foodDRI);
				// weekList.add(day6);

				DietaryDayRptModel day7 = new DietaryDayRptModel();
				day7.setFullDay(dayOfWeek);
				day7.setWeekName("星期日");
				day7.setBreakfastList(breakfastList7);
				day7.setBreakfastMidList(breakfastMidList7);
				day7.setLunchList(lunchList7);
				day7.setSnackList(snackList7);
				day7.setDinnerList(dinnerList7);
				this.populate(day7, foodDRI);
				// weekList.add(day7);

				DietaryTemplateCount dietaryCount = null;

				boolean day1Exists = false;
				boolean day2Exists = false;
				boolean day3Exists = false;
				boolean day4Exists = false;
				boolean day5Exists = false;
				boolean day6Exists = false;
				boolean day7Exists = false;

				for (DietaryTemplate tpl : list) {
					items = dietaryMap.get(tpl.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietaryTemplate(tpl);
						model.setName(tpl.getName());
						model.setItems(items);
						long typeId = tpl.getTypeId();
						switch (dayOfWeek) {
						case Calendar.MONDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList1.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList1.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList1.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList1.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList1.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day1, dietaryCount, foodDRI, foodDRIPercent);
							}
							day1Exists = true;
							break;
						case Calendar.TUESDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList2.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList2.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList2.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList2.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList2.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day2, dietaryCount, foodDRI, foodDRIPercent);
							}
							day2Exists = true;
							break;
						case Calendar.WEDNESDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList3.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList3.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList3.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList3.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList3.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day3, dietaryCount, foodDRI, foodDRIPercent);
							}
							day3Exists = true;
							break;
						case Calendar.THURSDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList4.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList4.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList4.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList4.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList4.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day4, dietaryCount, foodDRI, foodDRIPercent);
							}
							day4Exists = true;
							break;
						case Calendar.FRIDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList5.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList5.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList5.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList5.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList5.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day5, dietaryCount, foodDRI, foodDRIPercent);
							}
							day5Exists = true;
							break;

						case Calendar.SATURDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList6.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList6.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList6.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList6.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList6.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day6, dietaryCount, foodDRI, foodDRIPercent);
							}
							day6Exists = true;
							break;

						case Calendar.SUNDAY:
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList7.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList7.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList7.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList7.add(model);
							}
							if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
								dinnerList7.add(model);
							}
							dietaryCount = countMap.get(dayOfWeek);
							if (dietaryCount != null) {
								NutritionEvaluateUtils.evaluate(day7, dietaryCount, foodDRI, foodDRIPercent);
							}
							day7Exists = true;
							break;
						}
					}
				}

				int total = 0;
				total = Math.max(getSize(breakfastList1), getSize(breakfastList2));
				total = Math.max(total, getSize(breakfastList3));
				total = Math.max(total, getSize(breakfastList4));
				total = Math.max(total, getSize(breakfastList5));
				total = Math.max(total, getSize(breakfastList6));
				total = Math.max(total, getSize(breakfastList7));

				this.fillBlank(breakfastList1, total);
				this.fillBlank(breakfastList2, total);
				this.fillBlank(breakfastList3, total);
				this.fillBlank(breakfastList4, total);
				this.fillBlank(breakfastList5, total);
				this.fillBlank(breakfastList6, total);
				this.fillBlank(breakfastList7, total);

				context.put("breakfastList1", breakfastList1);
				context.put("breakfastList2", breakfastList2);
				context.put("breakfastList3", breakfastList3);
				context.put("breakfastList4", breakfastList4);
				context.put("breakfastList5", breakfastList5);
				context.put("breakfastList6", breakfastList6);
				context.put("breakfastList7", breakfastList7);

				total = Math.max(getSize(breakfastMidList1), getSize(breakfastMidList2));
				total = Math.max(total, getSize(breakfastMidList3));
				total = Math.max(total, getSize(breakfastMidList4));
				total = Math.max(total, getSize(breakfastMidList5));
				total = Math.max(total, getSize(breakfastMidList6));
				total = Math.max(total, getSize(breakfastMidList7));

				this.fillBlank(breakfastMidList1, total);
				this.fillBlank(breakfastMidList2, total);
				this.fillBlank(breakfastMidList3, total);
				this.fillBlank(breakfastMidList4, total);
				this.fillBlank(breakfastMidList5, total);
				this.fillBlank(breakfastMidList6, total);
				this.fillBlank(breakfastMidList7, total);

				context.put("breakfastMidList1", breakfastMidList1);
				context.put("breakfastMidList2", breakfastMidList2);
				context.put("breakfastMidList3", breakfastMidList3);
				context.put("breakfastMidList4", breakfastMidList4);
				context.put("breakfastMidList5", breakfastMidList5);
				context.put("breakfastMidList6", breakfastMidList6);
				context.put("breakfastMidList7", breakfastMidList7);

				total = Math.max(getSize(lunchList1), getSize(lunchList2));
				total = Math.max(total, getSize(lunchList3));
				total = Math.max(total, getSize(lunchList4));
				total = Math.max(total, getSize(lunchList5));
				total = Math.max(total, getSize(lunchList6));
				total = Math.max(total, getSize(lunchList7));

				this.fillBlank(lunchList1, total);
				this.fillBlank(lunchList2, total);
				this.fillBlank(lunchList3, total);
				this.fillBlank(lunchList4, total);
				this.fillBlank(lunchList5, total);
				this.fillBlank(lunchList6, total);
				this.fillBlank(lunchList7, total);

				context.put("lunchList1", lunchList1);
				context.put("lunchList2", lunchList2);
				context.put("lunchList3", lunchList3);
				context.put("lunchList4", lunchList4);
				context.put("lunchList5", lunchList5);
				context.put("lunchList6", lunchList6);
				context.put("lunchList7", lunchList7);

				total = Math.max(getSize(snackList1), getSize(snackList2));
				total = Math.max(total, getSize(snackList3));
				total = Math.max(total, getSize(snackList4));
				total = Math.max(total, getSize(snackList5));
				total = Math.max(total, getSize(snackList6));
				total = Math.max(total, getSize(snackList7));

				this.fillBlank(snackList1, total);
				this.fillBlank(snackList2, total);
				this.fillBlank(snackList3, total);
				this.fillBlank(snackList4, total);
				this.fillBlank(snackList5, total);
				this.fillBlank(snackList6, total);
				this.fillBlank(snackList7, total);

				context.put("snackList1", snackList1);
				context.put("snackList2", snackList2);
				context.put("snackList3", snackList3);
				context.put("snackList4", snackList4);
				context.put("snackList5", snackList5);
				context.put("snackList6", snackList6);
				context.put("snackList7", snackList7);

				total = Math.max(getSize(dinnerList1), getSize(dinnerList2));
				total = Math.max(total, getSize(dinnerList3));
				total = Math.max(total, getSize(dinnerList4));
				total = Math.max(total, getSize(dinnerList5));
				total = Math.max(total, getSize(dinnerList6));
				total = Math.max(total, getSize(dinnerList7));

				this.fillBlank(dinnerList1, total);
				this.fillBlank(dinnerList2, total);
				this.fillBlank(dinnerList3, total);
				this.fillBlank(dinnerList4, total);
				this.fillBlank(dinnerList5, total);
				this.fillBlank(dinnerList6, total);
				this.fillBlank(dinnerList7, total);

				context.put("dinnerList1", dinnerList1);
				context.put("dinnerList2", dinnerList2);
				context.put("dinnerList3", dinnerList3);
				context.put("dinnerList4", dinnerList4);
				context.put("dinnerList5", dinnerList5);
				context.put("dinnerList6", dinnerList6);
				context.put("dinnerList7", dinnerList7);

				context.put("day1", day1);
				context.put("day2", day2);
				context.put("day3", day3);
				context.put("day4", day4);
				context.put("day5", day5);
				context.put("day6", day6);
				context.put("day7", day7);

				context.put("wkdata1", day1);
				context.put("wkdata2", day2);
				context.put("wkdata3", day3);
				context.put("wkdata4", day4);
				context.put("wkdata5", day5);
				context.put("wkdata6", day6);
				context.put("wkdata7", day7);

				if (day1Exists) {
					weekList.add(day1);
				}

				if (day2Exists) {
					weekList.add(day2);
				}

				if (day3Exists) {
					weekList.add(day3);
				}

				if (day4Exists) {
					weekList.add(day4);
				}

				if (day5Exists) {
					weekList.add(day5);
				}

				if (day6Exists) {
					weekList.add(day6);
				}

				if (day7Exists) {
					weekList.add(day7);
				}

				context.put("weekList", weekList);

			}
		}
		return context;
	}

	public void setDietaryTemplateCountService(DietaryTemplateCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	public void setFoodDRIPercentService(FoodDRIPercentService foodDRIPercentService) {
		this.foodDRIPercentService = foodDRIPercentService;
	}

	public void setFoodDRIService(FoodDRIService foodDRIService) {
		this.foodDRIService = foodDRIService;
	}

}
