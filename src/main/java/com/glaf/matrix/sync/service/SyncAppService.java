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

package com.glaf.matrix.sync.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.sync.domain.SyncApp;
import com.glaf.matrix.sync.query.SyncAppQuery;

@Transactional(readOnly = true)
public interface SyncAppService {

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(Long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	SyncApp getSyncApp(Long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSyncAppCountByQueryCriteria(SyncAppQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SyncApp> getSyncAppsByQueryCriteria(int start, int pageSize, SyncAppQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<SyncApp> list(SyncAppQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(SyncApp syncApp);

}
