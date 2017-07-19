/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.processor.supplier.tracecompare.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.openchrom.xxd.processor.supplier.tracecompare.preferences.PreferenceSupplier;

public class Processor {

	public static final String PROCESSOR_FILE_EXTENSION = ".otc";
	//
	private static Map<String, Pattern> samplePatterns = new HashMap<String, Pattern>();

	public static String getSamplePattern(String fileName) {

		Pattern pattern = getPattern();
		Matcher matcher = pattern.matcher(fileName);
		if(matcher.find()) {
			return matcher.group(1);
		} else {
			return "no match";
		}
	}

	public static boolean measurementExists(String pathDirectory, String fileExtension, String pattern) {

		if(pathDirectory.equals("") || fileExtension.equals("") || pattern.equals("")) {
			return false;
		}
		//
		File directory = new File(pathDirectory);
		for(File file : directory.listFiles()) {
			if(file.isFile()) {
				String fileName = file.getName();
				if(fileName.endsWith(fileExtension)) {
					if(fileName.startsWith(pattern)) {
						return true;
					}
				}
			}
		}
		//
		return false;
	}

	public static List<String> getMeasurementPatterns(String pathDirectory, String fileExtension) {

		List<String> patterns = new ArrayList<String>();
		//
		if(!pathDirectory.equals("") && !fileExtension.equals("")) {
			/*
			 * Extract the patterns.
			 */
			Set<String> measurementPatterns = new HashSet<String>();
			Pattern pattern = getPattern();
			File directory = new File(pathDirectory);
			for(File file : directory.listFiles()) {
				if(file.isFile()) {
					String fileName = file.getName();
					if(fileName.endsWith(fileExtension)) {
						Matcher matcher = pattern.matcher(fileName);
						if(matcher.find()) {
							measurementPatterns.add(matcher.group(1));
						}
					}
				}
			}
			//
			patterns.addAll(measurementPatterns);
			Collections.sort(patterns);
		}
		//
		return patterns;
	}

	public static List<File> getMeasurementFiles(String pathDirectory, String fileExtension, String pattern) {

		List<File> measurementFiles = new ArrayList<File>();
		//
		if(!pathDirectory.equals("") && !fileExtension.equals("") && !pattern.equals("")) {
			File directory = new File(pathDirectory);
			for(File file : directory.listFiles()) {
				if(file.isFile()) {
					String fileName = file.getName();
					if(fileName.endsWith(fileExtension)) {
						if(fileName.startsWith(pattern)) {
							measurementFiles.add(file);
						}
					}
				}
			}
		}
		//
		return measurementFiles;
	}

	private static Pattern getPattern() {

		String filePattern = PreferenceSupplier.getFilePattern();
		Pattern pattern = samplePatterns.get(filePattern);
		if(pattern == null) {
			pattern = Pattern.compile(filePattern);
			samplePatterns.put(filePattern, pattern);
		}
		//
		return pattern;
	}
}
