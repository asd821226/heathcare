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

package com.glaf.report.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
 
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.CustomProperties;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.FileUtils;
import com.glaf.report.data.ReportDefinition;
import com.glaf.report.domain.Report;
import com.glaf.report.query.ReportQuery;
import com.glaf.report.service.IReportService;

public class ReportContainer {
	private static class ReportContainerHolder {
		private static final ReportContainer INSTANCE = new ReportContainer();
	}

	protected final static Log logger = LogFactory.getLog(ReportContainer.class);

	private final static ConcurrentMap<String, ReportDefinition> rptConcurrentMap = new ConcurrentHashMap<String, ReportDefinition>();

	private final static String sp = System.getProperty("file.separator");

	private final static String DEFAULT_CONFIG_PATH = "/conf/report";

	public static final ReportContainer getContainer() {
		return ReportContainerHolder.INSTANCE;
	}

	private ReportContainer() {

	}

	public ReportDefinition getReportDefinition(String reportId) {
		/**
		 * 如果是生产环境，才从缓存中获取数据
		 */
		logger.debug("ENV:" + SystemConfig.getString("RunEnvironment"));
		if (StringUtils.equals(SystemConfig.getString("RunEnvironment"), "product")
				&& rptConcurrentMap.containsKey(reportId) && rptConcurrentMap.get(reportId) != null) {
			logger.debug("load template from cache.");
			return rptConcurrentMap.get(reportId);
		}
		Map<String, ReportDefinition> reportMap = reload();
		ReportDefinition rdf = reportMap.get(reportId);
		if (rdf != null) {
			if (rdf.getTemplateId() != null) {

			} else if (rdf.getTemplateFile() != null) {
				String filename = "";
				if (StringUtils.startsWith(rdf.getTemplateFile(), "/WEB-INF")) {
					filename = SystemProperties.getAppPath() + rdf.getTemplateFile();
				} else if (StringUtils.startsWith(rdf.getTemplateFile(), "WEB-INF")) {
					filename = SystemProperties.getAppPath() + sp + rdf.getTemplateFile();
				} else {
					filename = SystemProperties.getConfigRootPath() + sp + rdf.getTemplateFile();
				}
				logger.debug("read template:" + filename);
				byte[] data = FileUtils.getBytes(filename);
				if (data != null) {
					rdf.setData(data);
					rptConcurrentMap.put(reportId, rdf);
				}
			}
		}

		return rdf;
	}

	public Map<String, ReportDefinition> reload() {
		Map<String, ReportDefinition> reportMap = new HashMap<String, ReportDefinition>();
		String configLocation = SystemProperties.getString("report.config.path");
		if (StringUtils.isEmpty(configLocation)) {
			configLocation = CustomProperties.getString("report.config.path");
		}
		if (StringUtils.isEmpty(configLocation)) {
			configLocation = DEFAULT_CONFIG_PATH;
		}

		ReportDefinitionReader reader = new ReportDefinitionReader();
		InputStream inputStream = null;
		try {
			String configPath = SystemProperties.getConfigRootPath() + configLocation;
			logger.info(configPath);
			File directory = new File(configPath);
			if (directory.isDirectory()) {
				String[] filelist = directory.list();
				if (filelist != null) {
					for (int i = 0, len = filelist.length; i < len; i++) {
						String filename = configPath + sp + filelist[i];
						File file = new File(filename);
						if (file.isFile() && file.getName().endsWith(".report.xml")) {
							logger.debug(file.getAbsolutePath());
							inputStream = new FileInputStream(file);
							List<ReportDefinition> reports = reader.read(inputStream);
							for (ReportDefinition rdf : reports) {
								reportMap.put(rdf.getReportId(), rdf);
							}
							com.glaf.core.util.IOUtils.closeStream(inputStream);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			com.glaf.core.util.IOUtils.closeStream(inputStream);
		}

		try {
			IReportService reportService = ContextFactory.getBean("reportService");
			ReportQuery query = new ReportQuery();
			query.enableFlag("1");
			List<Report> reports = reportService.list(query);
			if (reports != null && !reports.isEmpty()) {
				for (Report rpt : reports) {
					if (StringUtils.isNotEmpty(rpt.getReportTemplate())) {
						String filename = rpt.getReportTemplate();
						if (StringUtils.startsWith(rpt.getReportTemplate(), "/WEB-INF")) {
							filename = SystemProperties.getAppPath() + rpt.getReportTemplate();
						}
						File file = new File(filename);
						if (file.exists() && file.isFile()) {
							logger.debug(file.getAbsolutePath());
							byte[] bytes = FileUtils.getBytes(file);
							ReportDefinition rd = new ReportDefinition();
							rd.setData(bytes);
							rd.setReportId(rpt.getId());
							rd.setTemplateFile(rpt.getReportTemplate());
							reportMap.put(rd.getReportId(), rd);

							ReportDefinition rd2 = new ReportDefinition();
							rd2.setData(bytes);
							rd2.setReportId(rpt.getName());
							rd2.setTemplateFile(rpt.getReportTemplate());
							reportMap.put(rd2.getReportId(), rd2);
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return reportMap;
	}

}