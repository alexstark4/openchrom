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
package net.openchrom.xxd.process.supplier.templates.peaks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.AbstractPeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.IPeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.xxd.process.supplier.templates.preferences.PreferenceSupplier;
import net.openchrom.xxd.process.supplier.templates.settings.StandardsReferencerSettings;

public class StandardsReferencer extends AbstractPeakQuantifier implements IPeakQuantifier {

	private static final Logger logger = Logger.getLogger(StandardsReferencer.class);

	@Override
	public IProcessingInfo quantify(List<IPeak> peaks, IPeakQuantifierSettings settings, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = validate(peaks, settings, monitor);
		if(!processingInfo.hasErrorMessages()) {
			if(settings instanceof StandardsReferencerSettings) {
				StandardsReferencerSettings standardsReferencerSettings = (StandardsReferencerSettings)settings;
				for(ReferencerSettings referencerSettings : standardsReferencerSettings.getReferencerSettings()) {
					referencePeak(peaks, referencerSettings);
				}
			} else {
				processingInfo.addErrorMessage(StandardsReferencerSettings.DESCRIPTION, "The settings instance is wrong.");
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo quantify(IPeak peak, IPeakQuantifierSettings settings, IProgressMonitor monitor) {

		List<IPeak> peaks = new ArrayList<IPeak>();
		peaks.add(peak);
		return quantify(peaks, settings, monitor);
	}

	@Override
	public IProcessingInfo quantify(IPeak peak, IProgressMonitor monitor) {

		List<IPeak> peaks = new ArrayList<IPeak>();
		peaks.add(peak);
		StandardsReferencerSettings settings = getSettings();
		return quantify(peaks, settings, monitor);
	}

	@Override
	public IProcessingInfo quantify(List<IPeak> peaks, IProgressMonitor monitor) {

		StandardsReferencerSettings settings = getSettings();
		return quantify(peaks, settings, monitor);
	}

	private StandardsReferencerSettings getSettings() {

		StandardsReferencerSettings settings = new StandardsReferencerSettings();
		settings.setReferencerSettings(PreferenceSupplier.getSettings(PreferenceSupplier.P_STANDARDS_REFERENCER_LIST, ""));
		return settings;
	}

	private void referencePeak(List<? extends IPeak> peaks, ReferencerSettings referencerSettings) {

		int startRetentionTime = (int)(referencerSettings.getStartRetentionTime() * AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
		int stopRetentionTime = (int)(referencerSettings.getStopRetentionTime() * AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
		//
		try {
			if(startRetentionTime > 0 && startRetentionTime < stopRetentionTime) {
				for(IPeak peak : peaks) {
					if(isPeakMatch(peak, startRetentionTime, stopRetentionTime)) {
						String quantitationReference = referencerSettings.getName();
						peak.addQuantitationReference(quantitationReference);
					}
				}
			}
		} catch(PeakException e) {
			logger.warn(e);
		}
	}

	private boolean isPeakMatch(IPeak peak, int startRetentionTime, int stopRetentionTime) {

		IPeakModel peakModel = peak.getPeakModel();
		int retentionTime = peakModel.getRetentionTimeAtPeakMaximum();
		if(retentionTime >= startRetentionTime && retentionTime <= stopRetentionTime) {
			return true;
		} else {
			return false;
		}
	}

	private IProcessingInfo validate(List<IPeak> peaks, IPeakQuantifierSettings settings, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		if(peaks == null) {
			processingInfo.addErrorMessage(StandardsReferencerSettings.DESCRIPTION, "The peaks selection must not be null.");
		}
		if(settings == null) {
			processingInfo.addErrorMessage(StandardsReferencerSettings.DESCRIPTION, "The settings must not be null.");
		}
		return processingInfo;
	}
}
