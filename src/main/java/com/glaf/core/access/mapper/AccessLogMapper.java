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

package com.glaf.core.access.mapper;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.core.access.domain.*;
import com.glaf.core.access.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component
public interface AccessLogMapper {

	void bulkInsertAccessLog(List<AccessLog> list);

	void bulkInsertAccessLog_oracle(List<AccessLog> list);

	void deleteAccessLogById(Long id);

	void deleteAccessLogs(Date dateBefore);

	AccessLog getAccessLogById(Long id);

	int getAccessLogCount(AccessLogQuery query);

	List<AccessLog> getAccessLogs(AccessLogQuery query);

	List<AccessTotal> getAccessTotal(AccessLogQuery query);

	List<AccessTotal> getHourAccessTotal(int day);

	List<AccessTotal> getMinuteAccessTotal(int day);

	void insertAccessLog(AccessLog model);

}
