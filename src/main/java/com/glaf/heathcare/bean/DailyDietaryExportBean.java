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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.context.ContextFactory;

import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;
import com.glaf.heathcare.util.NutritionEvaluateUtils;

public class DailyDietaryExportBean {

	protected TenantConfigService tenantConfigService;

	protected DietaryService dietaryService;

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

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryService getDietaryService() {
		if (dietaryService == null) {
			dietaryService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryService");
		}
		return dietaryService;
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

	public TenantConfigService getTenantConfigService() {
		if (tenantConfigService == null) {
			tenantConfigService = ContextFactory.getBean("tenantConfigService");
		}
		return tenantConfigService;
	}

	protected void populate(DietaryDayRptModel m, FoodDRI foodDRI) {
		if (foodDRI != null) {
			m.setCalciumStandard(foodDRI.getCalcium());
			m.setHeatEnergyStandard(foodDRI.getHeatEnergy());
			m.setProteinStandard(foodDRI.getProtein());
		}
	}

	public Map<String, Object> prepareData(String tenantId, int fullDay, long typeIdX) {
		Map<String, Object> context = new HashMap<String, Object>();

		DietaryQuery query = new DietaryQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);

		FoodDRI foodDRI = null;
		FoodDRIPercent foodDRIPercent = null;
		if (typeIdX > 0) {
			foodDRI = getFoodDRIService().getFoodDRIByAge(4);
			foodDRIPercent = getFoodDRIPercentService().getFoodDRIPercent("3-6", typeIdX);
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

		List<Dietary> list = getDietaryService().list(query);
		if (list != null && !list.isEmpty()) {
			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : list) {
				dietaryIds.add(dietary.getId());
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.tenantId(tenantId);
			query2.dietaryIds(dietaryIds);
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
					items.add(item);
					dietaryMap.put(item.getDietaryId(), items);
				}

				List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

				DietaryDayRptModel dayRptModel = new DietaryDayRptModel();
				dayRptModel.setBreakfastList(breakfastList);
				dayRptModel.setBreakfastMidList(breakfastMidList);
				dayRptModel.setLunchList(lunchList);
				dayRptModel.setSnackList(snackList);
				dayRptModel.setDinnerList(dinnerList);

				double carbohydrateTotal = 0.0;
				double heatEnergyTotal = 0.0;
				double calciumTotal = 0.0;
				double proteinTotal = 0.0;
				double fatTotal = 0.0;

				double carbohydrateMomingTotal = 0.0;
				double heatEnergyMomingTotal = 0.0;
				double calciumMomingTotal = 0.0;
				double proteinMomingTotal = 0.0;
				double fatMomingTotal = 0.0;

				double carbohydrateNoonTotal = 0.0;
				double heatEnergyNoonTotal = 0.0;
				double calciumNoonTotal = 0.0;
				double proteinNoonTotal = 0.0;
				double fatNoonTotal = 0.0;

				double carbohydrateDinnerTotal = 0.0;
				double heatEnergyDinnerTotal = 0.0;
				double calciumDinnerTotal = 0.0;
				double proteinDinnerTotal = 0.0;
				double fatDinnerTotal = 0.0;

				for (Dietary dietary : list) {
					items = dietaryMap.get(dietary.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietary(dietary);
						model.setName(dietary.getName());
						model.setItems(items);
						model.setCalcium(dietary.getCalcium());
						model.setCarbohydrate(dietary.getCarbohydrate());
						model.setHeatEnergy(dietary.getHeatEnergy());
						model.setProtein(dietary.getProtein());
						model.setFat(dietary.getFat());

						long typeId = dietary.getTypeId();

						if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
							breakfastList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
						}
						if (typeId == 3305 || typeId == 3406 || typeId == 3416) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + dietary.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + dietary.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + dietary.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + dietary.getProtein();
							fatDinnerTotal = fatDinnerTotal + dietary.getFat();
						}

						carbohydrateTotal = carbohydrateTotal + dietary.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + dietary.getHeatEnergy();
						calciumTotal = calciumTotal + dietary.getCalcium();
						proteinTotal = proteinTotal + dietary.getProtein();
						fatTotal = fatTotal + dietary.getFat();

					}
				}

				if (breakfastList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfast", cnt);
					context.put("dietaryCountPercent_breakfast", cntPercent);

					context.put("breakfastList", breakfastList);
				}

				if (breakfastMidList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastMidList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfastMid", cnt);
					context.put("dietaryCountPercent_breakfastMid", cntPercent);

					context.put("breakfastMidList", breakfastMidList);
				}

				if (lunchList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : lunchList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_lunch", cnt);
					context.put("dietaryCountPercent_lunch", cntPercent);

					context.put("lunchList", lunchList);
				}

				if (snackList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : snackList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_snack", cnt);
					context.put("dietaryCountPercent_snack", cntPercent);

					context.put("snackList", snackList);
				}

				if (dinnerList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : dinnerList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_dinner", cnt);
					context.put("dietaryCountPercent_dinner", cntPercent);

					context.put("dinnerList", dinnerList);
				}

				if (foodDRI != null) {
					if (foodDRI.getHeatEnergy() > 0) {
						dayRptModel.setHeatEnergyPercent(heatEnergyTotal / foodDRI.getHeatEnergy() * 100D);
					}

					if (foodDRI.getProtein() > 0) {
						dayRptModel.setProteinPercent(proteinTotal / foodDRI.getProtein() * 100D);
					}

					if (foodDRI.getCarbohydrate() > 0) {
						dayRptModel.setCarbohydratePercent(carbohydrateTotal / foodDRI.getCarbohydrate() * 100D);
					}

					if (foodDRI.getFat() > 0) {
						dayRptModel.setFatPercent(fatTotal / foodDRI.getFat() * 100D);
					}

					if (foodDRI.getCalcium() > 0) {
						dayRptModel.setCalciumPercent(calciumTotal / foodDRI.getCalcium() * 100D);
					}
				}

				DietaryCount dietaryCountSum = new DietaryCount();
				dietaryCountSum.setCalcium(calciumTotal);
				dietaryCountSum.setCarbohydrate(carbohydrateTotal);
				dietaryCountSum.setFat(fatTotal);
				dietaryCountSum.setProtein(proteinTotal);
				dietaryCountSum.setHeatEnergy(heatEnergyTotal);

				NutritionEvaluateUtils.evaluate(dayRptModel, dietaryCountSum, foodDRI, foodDRIPercent);

				context.put("dayRptModel", dayRptModel);
				context.put("dietaryCountSum", dietaryCountSum);

				if (heatEnergyMomingTotal > 0) {
					DietaryCount momingTotal = new DietaryCount();
					momingTotal.setCalcium(calciumMomingTotal);
					momingTotal.setCarbohydrate(carbohydrateMomingTotal);
					momingTotal.setHeatEnergy(heatEnergyMomingTotal);
					momingTotal.setProtein(proteinMomingTotal);
					momingTotal.setFat(fatMomingTotal);

					context.put("momingTotal", momingTotal);

					DietaryCount momingTotalPercent = new DietaryCount();

					if (calciumMomingTotal > 0) {
						momingTotalPercent.setCalcium(calciumMomingTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						momingTotalPercent.setCarbohydrate(carbohydrateMomingTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						momingTotalPercent.setFat(fatMomingTotal / fatTotal * 100D);
					}

					if (heatEnergyMomingTotal > 0) {
						momingTotalPercent.setHeatEnergy(heatEnergyMomingTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						momingTotalPercent.setProtein(proteinMomingTotal / proteinTotal * 100D);
					}

					context.put("momingTotalPercent", momingTotalPercent);
				}

				if (heatEnergyNoonTotal > 0) {
					DietaryCount noonTotal = new DietaryCount();
					noonTotal.setCalcium(calciumNoonTotal);
					noonTotal.setCarbohydrate(carbohydrateNoonTotal);
					noonTotal.setHeatEnergy(heatEnergyNoonTotal);
					noonTotal.setProtein(proteinNoonTotal);
					noonTotal.setFat(fatNoonTotal);

					context.put("noonTotal", noonTotal);

					DietaryCount noonTotalPercent = new DietaryCount();

					if (calciumNoonTotal > 0) {
						noonTotalPercent.setCalcium(calciumNoonTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						noonTotalPercent.setCarbohydrate(carbohydrateNoonTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						noonTotalPercent.setFat(fatNoonTotal / fatTotal * 100D);
					}

					if (heatEnergyNoonTotal > 0) {
						noonTotalPercent.setHeatEnergy(heatEnergyNoonTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						noonTotalPercent.setProtein(proteinNoonTotal / proteinTotal * 100D);
					}

					context.put("noonTotalPercent", noonTotalPercent);
				}
			}
		}
		return context;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
