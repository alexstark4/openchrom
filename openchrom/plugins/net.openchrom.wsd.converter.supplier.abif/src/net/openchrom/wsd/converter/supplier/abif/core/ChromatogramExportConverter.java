/*******************************************************************************
 * Copyright (c) 2016, 2018 Matthias Mailänder, Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.wsd.converter.supplier.abif.core;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.chromatogram.AbstractChromatogramWSDExportConverter;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramExportConverter extends AbstractChromatogramWSDExportConverter implements IChromatogramExportConverter {

	@Override
	public IProcessingInfo convert(File file, IChromatogramWSD chromatogram, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		return processingInfo;
	}
}
