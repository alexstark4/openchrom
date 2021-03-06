/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.templates.peaks;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.identifier.peak.IPeakIdentifierCSD;
import org.eclipse.chemclipse.chromatogram.csd.identifier.settings.IPeakIdentifierSettingsCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.xxd.process.supplier.templates.preferences.PreferenceSupplier;

public class PeakIdentifierCSD<T> extends AbstractPeakIdentifier implements IPeakIdentifierCSD<T> {

	@Override
	public IProcessingInfo<T> identify(List<? extends IPeakCSD> peaks, IPeakIdentifierSettingsCSD settings, IProgressMonitor monitor) {

		return applyIdentifier(peaks, settings, monitor);
	}

	@Override
	public IProcessingInfo<T> identify(IPeakCSD peak, IPeakIdentifierSettingsCSD settings, IProgressMonitor monitor) {

		return identify(extractPeaks(peak), settings, monitor);
	}

	@Override
	public IProcessingInfo<T> identify(IPeakCSD peak, IProgressMonitor monitor) {

		return identify(peak, getSettings(PreferenceSupplier.P_PEAK_IDENTIFIER_LIST_CSD), monitor);
	}

	@Override
	public IProcessingInfo<T> identify(List<? extends IPeakCSD> peaks, IProgressMonitor monitor) {

		return identify(peaks, getSettings(PreferenceSupplier.P_PEAK_IDENTIFIER_LIST_CSD), monitor);
	}

	@Override
	public IProcessingInfo<T> identify(IChromatogramSelectionCSD chromatogramSelectionCSD, IProgressMonitor monitor) {

		return identify(extractPeaks(chromatogramSelectionCSD), monitor);
	}

	@Override
	public IProcessingInfo<T> identify(IChromatogramSelectionCSD chromatogramSelectionCSD, IPeakIdentifierSettingsCSD peakIdentifierSettings, IProgressMonitor monitor) {

		return identify(extractPeaks(chromatogramSelectionCSD), peakIdentifierSettings, monitor);
	}
}
