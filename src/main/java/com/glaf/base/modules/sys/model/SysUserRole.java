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

package com.glaf.base.modules.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.util.SysUserRoleJsonFactory;
import com.glaf.core.base.JSONable;

/**
 * 用户与角色的关联关系
 *
 */
@Entity
@Table(name = "SYS_USER_ROLE")
public class SysUserRole implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 50, nullable = false)
	protected String id;

	/**
	 * 所属租户
	 */
	@Column(name = "TENANTID")
	protected String tenantId;

	/**
	 * 授权人
	 */
	@Column(name = "AUTHORIZED")
	protected int authorized;// 0-角色用户 1-代理用户

	/**
	 * 委托人
	 */
	@Column(name = "AUTHORIZEFROM")
	protected String authorizeFrom;

	@javax.persistence.Transient
	protected String authorizeFromName;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AVAILDATEEND")
	protected Date availDateEnd;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AVAILDATESTART")
	protected Date availDateStart;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	protected Date createDate;

	/**
	 * 部门角色编号
	 */
	@Column(name = "ROLEID", length = 50)
	protected String roleId;

	/**
	 * 流程描述
	 */
	@Column(name = "PROCESSDESCRIPTION", length = 500)
	protected String processDescription;

	/**
	 * 用户名
	 */
	@Column(name = "USERID", length = 50)
	protected String userId;

	@javax.persistence.Transient
	private SysRole role;

	@javax.persistence.Transient
	private SysUser user;

	public SysUserRole() {

	}

	public int getAuthorized() {
		return authorized;
	}

	public String getAuthorizeFrom() {
		return authorizeFrom;
	}

	public String getAuthorizeFromName() {
		return authorizeFromName;
	}

	public Date getAvailDateEnd() {
		return availDateEnd;
	}

	public Date getAvailDateStart() {
		return availDateStart;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getId() {
		return id;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public SysRole getRole() {
		return role;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public SysUser getUser() {
		return user;
	}

	public String getUserId() {
		return userId;
	}

	public SysUserRole jsonToObject(JSONObject jsonObject) {
		return SysUserRoleJsonFactory.jsonToObject(jsonObject);
	}

	public void setAuthorized(int authorized) {
		this.authorized = authorized;
	}

	public void setAuthorizeFrom(String authorizeFrom) {
		this.authorizeFrom = authorizeFrom;
	}

	public void setAuthorizeFromName(String authorizeFromName) {
		this.authorizeFromName = authorizeFromName;
	}

	public void setAvailDateEnd(Date availDateEnd) {
		this.availDateEnd = availDateEnd;
	}

	public void setAvailDateStart(Date availDateStart) {
		this.availDateStart = availDateStart;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JSONObject toJsonObject() {
		return SysUserRoleJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysUserRoleJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}