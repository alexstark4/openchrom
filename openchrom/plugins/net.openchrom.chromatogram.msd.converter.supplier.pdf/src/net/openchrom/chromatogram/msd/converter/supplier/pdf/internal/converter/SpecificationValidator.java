/*******************************************************************************
 * Copyright (c) 2011 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 *******************************************************************************/
package net.openchrom.chromatogram.msd.converter.supplier.pdf.internal.converter;

import java.io.File;

public class SpecificationValidator {

	/**
	 * Use only static methods.
	 */
	private SpecificationValidator() {

	}

	public static File validateExcelSpecification(File file) {

		File validFile;
		String path = file.getAbsolutePath().toLowerCase();
		if(file.isDirectory()) {
			validFile = new File(file.getAbsolutePath() + File.separator + "CHROMATOGRAM.pdf");
		} else {
			if(path.endsWith(".")) {
				validFile = new File(file.getAbsolutePath() + "pdf");
			} else if(!path.endsWith(".pdf")) {
				validFile = new File(file.getAbsolutePath() + ".pdf");
			} else {
				validFile = file;
			}
		}
		return validFile;
	}
}
