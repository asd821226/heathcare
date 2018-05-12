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

package com.glaf.heathcare.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "HEALTH_DIETARY_TEMPLATE")
public class DietaryTemplate implements Serializable, JSONable, java.lang.Comparable<DietaryTemplate> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 4000)
	protected String description;

	/**
	 * 年龄段
	 */
	@Column(name = "AGEGROUP_", length = 50)
	protected String ageGroup;

	/**
	 * 省份
	 */
	@Column(name = "PROVINCE_", length = 50)
	protected String province;

	/**
	 * 地域
	 */
	@Column(name = "REGION_", length = 50)
	protected String region;

	/**
	 * 季节编号
	 */
	@Column(name = "SEASON_")
	protected int season;

	/**
	 * 类别码
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 餐点类别编号
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 热能
	 */
	@Column(name = "HEATENERGY_")
	protected double heatEnergy;

	/**
	 * 蛋白质
	 */
	@Column(name = "PROTEIN_")
	protected double protein;

	/**
	 * 脂肪
	 */
	@Column(name = "FAT_")
	protected double fat;

	/**
	 * 碳水化合物
	 */
	@Column(name = "CARBOHYDRATE_")
	protected double carbohydrate;

	/**
	 * 微生素A
	 */
	@Column(name = "VITAMINA_")
	protected double vitaminA;

	/**
	 * 微生素B1
	 */
	@Column(name = "VITAMINB1_")
	protected double vitaminB1;

	/**
	 * 微生素B2
	 */
	@Column(name = "VITAMINB2_")
	protected double vitaminB2;

	/**
	 * 微生素B6
	 */
	@Column(name = "VITAMINB6_")
	protected double vitaminB6;

	/**
	 * 微生素B12
	 */
	@Column(name = "VITAMINB12_")
	protected double vitaminB12;

	/**
	 * 微生素C
	 */
	@Column(name = "VITAMINC_")
	protected double vitaminC;

	/**
	 * 胡萝卜素
	 */
	@Column(name = "CAROTENE_")
	protected double carotene;

	/**
	 * 视黄醇
	 */
	@Column(name = "RETINOL_")
	protected double retinol;

	/**
	 * 尼克酸
	 */
	@Column(name = "NICOTINICCID_")
	protected double nicotinicCid;

	/**
	 * 钙
	 */
	@Column(name = "CALCIUM_")
	protected double calcium;

	/**
	 * 铁
	 */
	@Column(name = "IRON_")
	protected double iron;

	/**
	 * 锌
	 */
	@Column(name = "ZINC_")
	protected double zinc;

	/**
	 * 碘
	 */
	@Column(name = "IODINE_")
	protected double iodine;

	/**
	 * 磷
	 */
	@Column(name = "PHOSPHORUS_")
	protected double phosphorus;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 日
	 */
	@Column(name = "DAY_")
	protected int day;

	/**
	 * 星期几
	 */
	@Column(name = "DAYOFWEEK_")
	protected int dayOfWeek;

	/**
	 * 周
	 */
	@Column(name = "WEEK_")
	protected int week;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 模板套编号
	 */
	@Column(name = "SUITNO_")
	protected int suitNo;

	/**
	 * 是否有效
	 */
	@Column(name = "ENABLEFLAG_", length = 1)
	protected String enableFlag;

	/**
	 * 是否实例
	 */
	@Column(name = "INSTANCEFLAG_", length = 1)
	protected String instanceFlag;

	/**
	 * 是否分享
	 */
	@Column(name = "SHAREFLAG_", length = 1)
	protected String shareFlag;

	/**
	 * 系统内置
	 */
	@Column(name = "SYSFLAG_", length = 1)
	protected String sysFlag;

	/**
	 * 是否验证
	 */
	@Column(name = "VERIFYFLAG_", length = 1)
	protected String verifyFlag;

	/**
	 * 业务状态
	 */
	@Column(name = "BUSINESSSTATUS_")
	protected int businessStatus;

	/**
	 * 确认人
	 */
	@Column(name = "CONFIRMBY_", length = 50)
	protected String confirmBy;

	/**
	 * 确认时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONFIRMTIME_")
	protected Date confirmTime;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected List<DietaryItem> items = new ArrayList<DietaryItem>();

	public DietaryTemplate() {

	}

	public void addItem(DietaryItem item) {
		if (items == null) {
			items = new ArrayList<DietaryItem>();
		}
		items.add(item);
	}

	@Override
	public int compareTo(DietaryTemplate o) {
		if (o == null) {
			return -1;
		}

		DietaryTemplate obj = o;

		int l = this.sortNo - obj.getSortNo();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	public String getAgeGroup() {
		return this.ageGroup;
	}

	public int getBusinessStatus() {
		return businessStatus;
	}

	public double getCalcium() {
		if (calcium > 0) {
			calcium = Math.round(calcium * 100D) / 100D;
		}
		return this.calcium;
	}

	public double getCarbohydrate() {
		if (carbohydrate > 0) {
			carbohydrate = Math.round(carbohydrate * 100D) / 100D;
		}
		return this.carbohydrate;
	}

	public double getCarotene() {
		if (carotene > 0) {
			carotene = Math.round(carotene * 100D) / 100D;
		}
		return this.carotene;
	}

	public String getConfirmBy() {
		return confirmBy;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public int getDay() {
		return this.day;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public String getDescription() {
		return this.description;
	}

	public String getEnableFlag() {
		return this.enableFlag;
	}

	public double getFat() {
		if (fat > 0) {
			fat = Math.round(fat * 100D) / 100D;
		}
		return this.fat;
	}

	public double getHeatEnergy() {
		if (heatEnergy > 0) {
			heatEnergy = Math.round(heatEnergy);
		}
		return this.heatEnergy;
	}

	public long getId() {
		return this.id;
	}

	public String getInstanceFlag() {
		return instanceFlag;
	}

	public double getIodine() {
		if (iodine > 0) {
			iodine = Math.round(iodine * 100D) / 100D;
		}
		return iodine;
	}

	public double getIron() {
		if (iron > 0) {
			iron = Math.round(iron * 100D) / 100D;
		}
		return this.iron;
	}

	public List<DietaryItem> getItems() {
		if (items != null && !items.isEmpty()) {
			Collections.sort(items);
		}
		return items;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public double getNicotinicCid() {
		if (nicotinicCid > 0) {
			nicotinicCid = Math.round(nicotinicCid * 1000D) / 1000D;
		}
		return this.nicotinicCid;
	}

	public double getPhosphorus() {
		if (phosphorus > 0) {
			phosphorus = Math.round(phosphorus * 100D) / 100D;
		}
		return phosphorus;
	}

	public double getProtein() {
		if (protein > 0) {
			protein = Math.round(protein * 100D) / 100D;
		}
		return this.protein;
	}

	public String getProvince() {
		return province;
	}

	public String getRegion() {
		return region;
	}

	public double getRetinol() {
		if (retinol > 0) {
			retinol = Math.round(retinol * 100D) / 100D;
		}
		return this.retinol;
	}

	public int getSeason() {
		return this.season;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public int getSuitNo() {
		return suitNo;
	}

	public String getSysFlag() {
		return this.sysFlag;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return this.type;
	}

	public long getTypeId() {
		return this.typeId;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	public String getVerifyFlag() {
		return this.verifyFlag;
	}

	public double getVitaminA() {
		if (vitaminA > 0) {
			vitaminA = Math.round(vitaminA * 1000D) / 1000D;
		}
		return this.vitaminA;
	}

	public double getVitaminB1() {
		if (vitaminB1 > 0) {
			vitaminB1 = Math.round(vitaminB1 * 1000D) / 1000D;
		}
		return this.vitaminB1;
	}

	public double getVitaminB12() {
		if (vitaminB12 > 0) {
			vitaminB12 = Math.round(vitaminB12 * 1000D) / 1000D;
		}
		return this.vitaminB12;
	}

	public double getVitaminB2() {
		if (vitaminB2 > 0) {
			vitaminB2 = Math.round(vitaminB2 * 1000D) / 1000D;
		}
		return this.vitaminB2;
	}

	public double getVitaminB6() {
		if (vitaminB6 > 0) {
			vitaminB6 = Math.round(vitaminB6 * 1000D) / 1000D;
		}
		return this.vitaminB6;
	}

	public double getVitaminC() {
		if (vitaminC > 0) {
			vitaminC = Math.round(vitaminC * 1000D) / 1000D;
		}
		return this.vitaminC;
	}

	public int getWeek() {
		return this.week;
	}

	public int getYear() {
		return this.year;
	}

	public double getZinc() {
		if (zinc > 0) {
			zinc = Math.round(zinc * 100D) / 100D;
		}
		return this.zinc;
	}

	public DietaryTemplate jsonToObject(JSONObject jsonObject) {
		return DietaryTemplateJsonFactory.jsonToObject(jsonObject);
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setBusinessStatus(int businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public void setCarotene(double carotene) {
		this.carotene = carotene;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInstanceFlag(String instanceFlag) {
		this.instanceFlag = instanceFlag;
	}

	public void setIodine(double iodine) {
		this.iodine = iodine;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setItems(List<DietaryItem> items) {
		this.items = items;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setRetinol(double retinol) {
		this.retinol = retinol;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSuitNo(int suitNo) {
		this.suitNo = suitNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}

	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public void setVitaminB12(double vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public void setVitaminB6(double vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	public JSONObject toJsonObject() {
		return DietaryTemplateJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DietaryTemplateJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
