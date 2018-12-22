/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.templates.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.IStatus;

import net.openchrom.xxd.process.supplier.templates.util.StandardsAssignerListUtil;
import net.openchrom.xxd.process.supplier.templates.util.StandardsAssignerValidator;

public class AssignerStandards extends HashMap<String, AssignerStandard> {

	private static final Logger logger = Logger.getLogger(AssignerStandards.class);
	//
	private static final long serialVersionUID = -9211939853837711565L;
	private StandardsAssignerListUtil listUtil = new StandardsAssignerListUtil();
	private static final String SEPARATOR_TOKEN = StandardsAssignerListUtil.SEPARATOR_TOKEN;
	private static final String SEPARATOR_ENTRY = StandardsAssignerListUtil.SEPARATOR_ENTRY;

	public void add(AssignerStandard setting) {

		if(setting != null) {
			put(setting.getName(), setting);
		}
	}

	public void load(String items) {

		loadSettings(items);
	}

	public void loadDefault(String items) {

		loadSettings(items);
	}

	public String save() {

		StringBuilder builder = new StringBuilder();
		Iterator<AssignerStandard> iterator = values().iterator();
		while(iterator.hasNext()) {
			AssignerStandard setting = iterator.next();
			extractSetting(setting, builder);
			if(iterator.hasNext()) {
				builder.append(SEPARATOR_TOKEN);
			}
		}
		return builder.toString().trim();
	}

	public String extractSettingString(AssignerStandard setting) {

		StringBuilder builder = new StringBuilder();
		extractSetting(setting, builder);
		return builder.toString();
	}

	public AssignerStandard extractSettingInstance(String item) {

		return extract(item);
	}

	public void importItems(File file) {

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				AssignerStandard setting = extract(line);
				if(setting != null) {
					add(setting);
				}
			}
			bufferedReader.close();
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportItems(File file) {

		try {
			PrintWriter printWriter = new PrintWriter(file);
			Iterator<AssignerStandard> iterator = values().iterator();
			while(iterator.hasNext()) {
				StringBuilder builder = new StringBuilder();
				AssignerStandard setting = iterator.next();
				extractSetting(setting, builder);
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			printWriter.close();
			return true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
			return false;
		}
	}

	private AssignerStandard extract(String text) {

		AssignerStandard setting = null;
		StandardsAssignerValidator validator = listUtil.getValidator();
		//
		IStatus status = validator.validate(text);
		if(status.isOK()) {
			setting = validator.getSetting();
		} else {
			logger.warn(status.getMessage());
		}
		//
		return setting;
	}

	private void loadSettings(String iems) {

		if(!"".equals(iems)) {
			String[] items = listUtil.parseString(iems);
			if(items.length > 0) {
				for(String item : items) {
					AssignerStandard setting = extractSettingInstance(item);
					if(setting != null) {
						add(setting);
					}
				}
			}
		}
	}

	private void extractSetting(AssignerStandard setting, StringBuilder builder) {

		builder.append(setting.getStartRetentionTime());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getStopRetentionTime());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getName());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getConcentration());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getConcentrationUnit());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getResponseFactor());
	}
}
