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

package com.glaf.heathcare.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class GoodsPurchaseDomainFactory {

	public static final String TABLENAME = "GOODS_PURCHASE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("goodsId", "GOODSID_");
		columnMap.put("goodsName", "GOODSNAME_");
		columnMap.put("goodsNodeId", "GOODSNODEID_");
		columnMap.put("purchaseTime", "PURCHASE_TIME_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("day", "DAY_");
		columnMap.put("week", "WEEK_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("quantity", "QUANTITY_");
		columnMap.put("unit", "UNIT_");
		columnMap.put("price", "PRICE_");
		columnMap.put("totalPrice", "TOTALPRICE_");
		columnMap.put("proposerId", "PROPOSERID_");
		columnMap.put("proposerName", "PROPOSERNAME_");
		columnMap.put("batchNo", "BATCHNO_");
		columnMap.put("supplier", "SUPPLIER_");
		columnMap.put("supplierId", "SUPPLIERID_");
		columnMap.put("contact", "CONTACT_");
		columnMap.put("standard", "STANDARD_");
		columnMap.put("address", "ADDRESS_");
		columnMap.put("expiryDate", "EXPIRYDATE_");
		columnMap.put("invoiceFlag", "INVOICEFLAG_");
		columnMap.put("businessStatus", "BUSINESSSTATUS_");
		columnMap.put("confirmTime", "CONFIRMTIME_");
		columnMap.put("confirmBy", "CONFIRMBY_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("goodsId", "Long");
		javaTypeMap.put("goodsName", "String");
		javaTypeMap.put("goodsNodeId", "Long");
		javaTypeMap.put("purchaseTime", "Date");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("day", "Integer");
		javaTypeMap.put("week", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("quantity", "Double");
		javaTypeMap.put("unit", "String");
		javaTypeMap.put("price", "Double");
		javaTypeMap.put("totalPrice", "Double");
		javaTypeMap.put("proposerId", "String");
		javaTypeMap.put("proposerName", "String");
		javaTypeMap.put("batchNo", "String");
		javaTypeMap.put("supplier", "String");
		javaTypeMap.put("supplierId", "String");
		javaTypeMap.put("contact", "String");
		javaTypeMap.put("standard", "String");
		javaTypeMap.put("address", "String");
		javaTypeMap.put("expiryDate", "Date");
		javaTypeMap.put("invoiceFlag", "String");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("businessStatus", "Integer");
		javaTypeMap.put("confirmTime", "Date");
		javaTypeMap.put("confirmBy", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
	}

	public static void alterTables(long databaseId) {
		TableDefinition tableDefinition = null;
		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				DBUtils.alterTable(conn, tableDefinition);
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
	}

	public static TableDefinition createTable() {
		TableDefinition tableDefinition = getTableDefinition(TABLENAME);
		if (!DBUtils.tableExists(TABLENAME)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static TableDefinition createTable(String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static void createTables(long databaseId) {
		String sqlStatement = null;
		TableDefinition tableDefinition = null;

		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			String dbType = DBConnectionFactory.getDatabaseType(conn);
			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
				statement = conn.createStatement();
				statement.executeUpdate(sqlStatement);
				JdbcUtils.close(statement);
			}
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
	}

	public static Map<String, String> getColumnMap() {
		return columnMap;
	}

	public static Map<String, String> getJavaTypeMap() {
		return javaTypeMap;
	}

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);
		tableDefinition.setName("GoodsPurchase");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition goodsId = new ColumnDefinition();
		goodsId.setName("goodsId");
		goodsId.setColumnName("GOODSID_");
		goodsId.setJavaType("Long");
		tableDefinition.addColumn(goodsId);

		ColumnDefinition goodsName = new ColumnDefinition();
		goodsName.setName("goodsName");
		goodsName.setColumnName("GOODSNAME_");
		goodsName.setJavaType("String");
		goodsName.setLength(200);
		tableDefinition.addColumn(goodsName);

		ColumnDefinition goodsNodeId = new ColumnDefinition();
		goodsNodeId.setName("goodsNodeId");
		goodsNodeId.setColumnName("GOODSNODEID_");
		goodsNodeId.setJavaType("Long");
		tableDefinition.addColumn(goodsNodeId);

		ColumnDefinition purchaseTime = new ColumnDefinition();
		purchaseTime.setName("purchaseTime");
		purchaseTime.setColumnName("PURCHASE_TIME_");
		purchaseTime.setJavaType("Date");
		tableDefinition.addColumn(purchaseTime);

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition day = new ColumnDefinition();
		day.setName("day");
		day.setColumnName("DAY_");
		day.setJavaType("Integer");
		tableDefinition.addColumn(day);

		ColumnDefinition week = new ColumnDefinition();
		week.setName("week");
		week.setColumnName("WEEK_");
		week.setJavaType("Integer");
		tableDefinition.addColumn(week);

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

		ColumnDefinition quantity = new ColumnDefinition();
		quantity.setName("quantity");
		quantity.setColumnName("QUANTITY_");
		quantity.setJavaType("Double");
		tableDefinition.addColumn(quantity);

		ColumnDefinition unit = new ColumnDefinition();
		unit.setName("unit");
		unit.setColumnName("UNIT_");
		unit.setJavaType("String");
		unit.setLength(20);
		tableDefinition.addColumn(unit);

		ColumnDefinition price = new ColumnDefinition();
		price.setName("price");
		price.setColumnName("PRICE_");
		price.setJavaType("Double");
		tableDefinition.addColumn(price);

		ColumnDefinition totalPrice = new ColumnDefinition();
		totalPrice.setName("totalPrice");
		totalPrice.setColumnName("TOTALPRICE_");
		totalPrice.setJavaType("Double");
		tableDefinition.addColumn(totalPrice);

		ColumnDefinition proposerId = new ColumnDefinition();
		proposerId.setName("proposerId");
		proposerId.setColumnName("PROPOSERID_");
		proposerId.setJavaType("String");
		proposerId.setLength(200);
		tableDefinition.addColumn(proposerId);

		ColumnDefinition proposerName = new ColumnDefinition();
		proposerName.setName("proposerName");
		proposerName.setColumnName("PROPOSERNAME_");
		proposerName.setJavaType("String");
		proposerName.setLength(200);
		tableDefinition.addColumn(proposerName);

		ColumnDefinition batchNo = new ColumnDefinition();
		batchNo.setName("batchNo");
		batchNo.setColumnName("BATCHNO_");
		batchNo.setJavaType("String");
		batchNo.setLength(200);
		tableDefinition.addColumn(batchNo);

		ColumnDefinition supplier = new ColumnDefinition();
		supplier.setName("supplier");
		supplier.setColumnName("SUPPLIER_");
		supplier.setJavaType("String");
		supplier.setLength(250);
		tableDefinition.addColumn(supplier);
		
		ColumnDefinition supplierId = new ColumnDefinition();
		supplierId.setName("supplierId");
		supplierId.setColumnName("SUPPLIERID_");
		supplierId.setJavaType("String");
		supplierId.setLength(50);
		tableDefinition.addColumn(supplierId);

		ColumnDefinition contact = new ColumnDefinition();
		contact.setName("contact");
		contact.setColumnName("CONTACT_");
		contact.setJavaType("String");
		contact.setLength(200);
		tableDefinition.addColumn(contact);

		ColumnDefinition standard = new ColumnDefinition();
		standard.setName("standard");
		standard.setColumnName("STANDARD_");
		standard.setJavaType("String");
		standard.setLength(50);
		tableDefinition.addColumn(standard);

		ColumnDefinition address = new ColumnDefinition();
		address.setName("address");
		address.setColumnName("ADDRESS_");
		address.setJavaType("String");
		address.setLength(500);
		tableDefinition.addColumn(address);

		ColumnDefinition expiryDate = new ColumnDefinition();
		expiryDate.setName("expiryDate");
		expiryDate.setColumnName("EXPIRYDATE_");
		expiryDate.setJavaType("Date");
		tableDefinition.addColumn(expiryDate);

		ColumnDefinition invoiceFlag = new ColumnDefinition();
		invoiceFlag.setName("invoiceFlag");
		invoiceFlag.setColumnName("INVOICEFLAG_");
		invoiceFlag.setJavaType("String");
		invoiceFlag.setLength(1);
		tableDefinition.addColumn(invoiceFlag);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(4000);
		tableDefinition.addColumn(remark);

		ColumnDefinition businessStatus = new ColumnDefinition();
		businessStatus.setName("businessStatus");
		businessStatus.setColumnName("BUSINESSSTATUS_");
		businessStatus.setJavaType("Integer");
		tableDefinition.addColumn(businessStatus);

		ColumnDefinition confirmTime = new ColumnDefinition();
		confirmTime.setName("confirmTime");
		confirmTime.setColumnName("CONFIRMTIME_");
		confirmTime.setJavaType("Date");
		tableDefinition.addColumn(confirmTime);

		ColumnDefinition confirmBy = new ColumnDefinition();
		confirmBy.setName("confirmBy");
		confirmBy.setColumnName("CONFIRMBY_");
		confirmBy.setJavaType("String");
		confirmBy.setLength(50);
		tableDefinition.addColumn(confirmBy);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setName("createBy");
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	public static void processDataRequest(DataRequest dataRequest) {
		if (dataRequest != null) {
			if (dataRequest.getFilter() != null) {
				if (dataRequest.getFilter().getField() != null) {
					dataRequest.getFilter().setColumn(columnMap.get(dataRequest.getFilter().getField()));
					dataRequest.getFilter().setJavaType(javaTypeMap.get(dataRequest.getFilter().getField()));
				}

				List<FilterDescriptor> filters = dataRequest.getFilter().getFilters();
				for (FilterDescriptor filter : filters) {
					filter.setParent(dataRequest.getFilter());
					if (filter.getField() != null) {
						filter.setColumn(columnMap.get(filter.getField()));
						filter.setJavaType(javaTypeMap.get(filter.getField()));
					}

					List<FilterDescriptor> subFilters = filter.getFilters();
					for (FilterDescriptor f : subFilters) {
						f.setColumn(columnMap.get(f.getField()));
						f.setJavaType(javaTypeMap.get(f.getField()));
						f.setParent(filter);
					}
				}
			}
		}
	}

	private GoodsPurchaseDomainFactory() {

	}

}
