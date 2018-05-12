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
 * 采购申请
 *
 */

@Entity
@Table(name = "GOODS_PURCHASE_PLAN")
public class GoodsPurchasePlan implements Serializable, JSONable {
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
	 * 物品编号
	 */
	@Column(name = "GOODSID_")
	protected long goodsId;

	/**
	 * 物品名称
	 */
	@Column(name = "GOODSNAME_", length = 200)
	protected String goodsName;

	/**
	 * 物品分类编号
	 */
	@Column(name = "GOODSNODEID_")
	protected long goodsNodeId;

	/**
	 * 计划采购日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PURCHASE_TIME_")
	protected Date purchaseTime;

	/**
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

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
	 * 周
	 */
	@Column(name = "WEEK_")
	protected int week;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 数量
	 */
	@Column(name = "QUANTITY_")
	protected double quantity;

	/**
	 * 库存量
	 */
	@javax.persistence.Transient
	protected double stockQuantity;

	/**
	 * 计量单位
	 */
	@Column(name = "UNIT_", length = 20)
	protected String unit;

	/**
	 * 限定单价
	 */
	@Column(name = "PRICE_")
	protected double price;

	/**
	 * 限定总价格
	 */
	@Column(name = "TOTALPRICE_")
	protected double totalPrice;

	/**
	 * 申请人编号
	 */
	@Column(name = "PROPOSERID_", length = 200)
	protected String proposerId;

	/**
	 * 申请人姓名
	 */
	@Column(name = "PROPOSERNAME_", length = 200)
	protected String proposerName;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 4000)
	protected String remark;

	/**
	 * 系统生成标识
	 */
	@Column(name = "SYSFLAG_", length = 1)
	protected String sysFlag;

	/**
	 * 每周一次标识
	 */
	@Column(name = "WEEKLYFLAG_", length = 1)
	protected String weeklyFlag;

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

	@javax.persistence.Transient
	protected int startIndex;

	/**
	 * 表后缀
	 */
	@javax.persistence.Transient
	protected String tableSuffix;

	public GoodsPurchasePlan() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsPurchasePlan other = (GoodsPurchasePlan) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getBusinessStatus() {
		return businessStatus;
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
		return day;
	}

	public int getFullDay() {
		return fullDay;
	}

	public long getGoodsId() {
		return this.goodsId;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public long getGoodsNodeId() {
		return goodsNodeId;
	}

	public long getId() {
		return this.id;
	}

	public int getMonth() {
		return month;
	}

	public double getPrice() {
		if (price > 0) {
			price = Math.round(price * 100D) / 100D;
		}
		return this.price;
	}

	public String getProposerId() {
		return this.proposerId;
	}

	public String getProposerName() {
		return this.proposerName;
	}

	public Date getPurchaseTime() {
		return this.purchaseTime;
	}

	public String getPurchaseTimeString() {
		if (this.purchaseTime != null) {
			return DateUtils.getDateTime(this.purchaseTime);
		}
		return "";
	}

	public double getQuantity() {
		if (quantity > 0) {
			quantity = Math.round(quantity * 10D) / 10D;
		}
		return this.quantity;
	}

	public String getRemark() {
		return this.remark;
	}

	public int getSemester() {
		return semester;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public double getStockQuantity() {
		if (stockQuantity > 0) {
			stockQuantity = Math.round(stockQuantity * 10D) / 10D;
		}
		return stockQuantity;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public double getTotalPrice() {
		if (totalPrice > 0) {
			totalPrice = Math.round(totalPrice * 100D) / 100D;
		}
		return this.totalPrice;
	}

	public String getUnit() {
		return this.unit;
	}

	public int getWeek() {
		return week;
	}

	public String getWeeklyFlag() {
		return weeklyFlag;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public GoodsPurchasePlan jsonToObject(JSONObject jsonObject) {
		return GoodsPurchasePlanJsonFactory.jsonToObject(jsonObject);
	}

	public void setBusinessStatus(int businessStatus) {
		this.businessStatus = businessStatus;
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

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsNodeId(long goodsNodeId) {
		this.goodsNodeId = goodsNodeId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void setStockQuantity(double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setWeeklyFlag(String weeklyFlag) {
		this.weeklyFlag = weeklyFlag;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return GoodsPurchasePlanJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GoodsPurchasePlanJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
