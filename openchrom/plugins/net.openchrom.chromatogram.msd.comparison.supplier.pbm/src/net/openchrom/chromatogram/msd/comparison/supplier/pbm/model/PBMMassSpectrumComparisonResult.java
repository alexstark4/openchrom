/*******************************************************************************
 * Copyright (c) 2008, 2011 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 *******************************************************************************/
package net.openchrom.chromatogram.msd.comparison.supplier.pbm.model;

import net.openchrom.chromatogram.msd.comparison.exceptions.ComparisonException;
import net.openchrom.chromatogram.msd.comparison.spectrum.AbstractMassSpectrumComparisonResult;
import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.MZLimitExceededException;
import net.openchrom.chromatogram.msd.model.implementation.DefaultIon;
import net.openchrom.chromatogram.msd.model.implementation.DefaultMassSpectrum;
import net.openchrom.chromatogram.msd.model.xic.IExtractedIonSignal;
import net.openchrom.chromatogram.msd.model.xic.IIonRange;
import net.openchrom.logging.core.Logger;

public class PBMMassSpectrumComparisonResult extends AbstractMassSpectrumComparisonResult {

	private static final Logger logger = Logger.getLogger(PBMMassSpectrumComparisonResult.class);
	public static final String DESCRIPTION = "PBM";
	private static final int NORMALIZATION_FACTOR = 100;
	private float matchQuality = 0.0f;

	public PBMMassSpectrumComparisonResult(IMassSpectrum unknown, IMassSpectrum reference, IIonRange massFragmentRange) throws ComparisonException {

		/*
		 * The super method checks if one of the arguments is null and throws an
		 * ComparisonException.
		 */
		super(unknown, reference, massFragmentRange);
		/*
		 * Build new mass spectra (normalized and with weighted intensity) and
		 * reassign the mass spectra to not override the original ones.
		 */
		unknown = adjustMassSpectrum(unknown, massFragmentRange);
		reference = adjustMassSpectrum(reference, massFragmentRange);
		matchQuality = calculateGeometricDistanceMatchQuality(unknown, reference, massFragmentRange);
		setDescription(DESCRIPTION);
	}

	@Override
	public float getMatchQuality() {

		return matchQuality;
	}

	// ----------------------------------------private methods
	/**
	 * This method will calculate new abundance values in the following manner:<br/>
	 * For each ion the new abundance will be set to:<br/>
	 * <br/>
	 * Inew = I * MZ;<br/>
	 * <br/>
	 * Set the highest intensity value to 100 so that no problems will occur
	 * when you calculate the new abundance values. A new mass spectrum will be
	 * returned.
	 */
	private IMassSpectrum adjustMassSpectrum(IMassSpectrum massSpectrum, IIonRange massFragmentRange) {

		IMassSpectrum adjustedMassSpectrum = new DefaultMassSpectrum();
		IIon adjustedIon;
		/*
		 * Normalize the abundance values to a highest value of 100.
		 */
		massSpectrum.normalize(NORMALIZATION_FACTOR);
		int startMZ = massFragmentRange.getStartIon();
		int stopMZ = massFragmentRange.getStopIon();
		IExtractedIonSignal signal;
		signal = massSpectrum.getExtractedIonSignal(startMZ, stopMZ);
		/*
		 * Calculate the new abundance value.
		 */
		float abundance;
		for(int mz = startMZ; mz <= stopMZ; mz++) {
			abundance = signal.getAbundance(mz);
			if(abundance >= 0) {
				/*
				 * Calculate the new abundance and add the ion to the
				 * fresh created mass spectrum.
				 */
				try {
					/*
					 * Inew = I * MZ
					 */
					adjustedIon = new DefaultIon(mz, abundance * mz);
					adjustedMassSpectrum.addIon(adjustedIon);
				} catch(AbundanceLimitExceededException e) {
					logger.warn(e);
				} catch(MZLimitExceededException e) {
					logger.warn(e);
				}
			}
		}
		return adjustedMassSpectrum;
	}
	// ----------------------------------------private methods
}
