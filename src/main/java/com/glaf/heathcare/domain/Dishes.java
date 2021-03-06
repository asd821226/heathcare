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
@Table(name = "HEALTH_DISHES")
public class Dishes implements Serializable, JSONable {
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
	 * 类别编号
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 名称拼音
	 */
	@Column(name = "NAMEPINYIN_", length = 200)
	protected String namePinyin;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 4000)
	protected String description;

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
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 系统标识
	 */
	@Column(name = "SYSFLAG_", length = 10)
	protected String sysFlag;

	/**
	 * 类别码
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 是否验证
	 */
	@Column(name = "VERIFYFLAG_", length = 1)
	protected String verifyFlag;

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
	protected List<DishesItem> items = new ArrayList<DishesItem>();

	public Dishes() {

	}

	public void addItem(DishesItem item) {
		if (items == null) {
			items = new ArrayList<DishesItem>();
		}
		items.add(item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dishes other = (Dishes) obj;
		if (id != other.id)
			return false;
		return true;
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

	public String getDescription() {
		return this.description;
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

	public double getIodine() {
		if (iodine > 0) {
			iodine = Math.round(iodine * 100D) / 100D;
		}
		return this.iodine;
	}

	public double getIron() {
		if (iron > 0) {
			iron = Math.round(iron * 100D) / 100D;
		}
		return this.iron;
	}

	public List<DishesItem> getItems() {
		return items;
	}

	public String getName() {
		return this.name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public double getNicotinicCid() {
		if (nicotinicCid > 0) {
			nicotinicCid = Math.round(nicotinicCid * 1000D) / 1000D;
		}
		return this.nicotinicCid;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public double getPhosphorus() {
		if (phosphorus > 0) {
			phosphorus = Math.round(phosphorus * 100D) / 100D;
		}
		return this.phosphorus;
	}

	public double getProtein() {
		if (protein > 0) {
			protein = Math.round(protein * 100D) / 100D;
		}
		return this.protein;
	}

	public double getRetinol() {
		if (retinol > 0) {
			retinol = Math.round(retinol * 100D) / 100D;
		}
		return this.retinol;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getSysFlag() {
		return this.sysFlag;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getType() {
		return this.type;
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

	public double getZinc() {
		if (zinc > 0) {
			zinc = Math.round(zinc * 100D) / 100D;
		}
		return this.zinc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public Dishes jsonToObject(JSONObject jsonObject) {
		return DishesJsonFactory.jsonToObject(jsonObject);
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

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setIodine(double iodine) {
		this.iodine = iodine;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setItems(List<DishesItem> items) {
		this.items = items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setRetinol(double retinol) {
		this.retinol = retinol;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
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

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	public JSONObject toJsonObject() {
		return DishesJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DishesJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
