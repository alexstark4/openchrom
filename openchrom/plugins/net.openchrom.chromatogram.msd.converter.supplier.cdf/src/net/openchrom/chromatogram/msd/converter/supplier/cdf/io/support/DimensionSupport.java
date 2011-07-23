/*******************************************************************************
 * Copyright (c) 2008, 2011 Philip (eselmeister) Wenig.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307, USA
 * 
 * 
 * Contributors: Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.msd.converter.supplier.cdf.io.support;

import java.util.ArrayList;

import net.openchrom.chromatogram.msd.model.core.AbstractIon;
import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.ISupplierMassSpectrum;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;
import ucar.ma2.ArrayShort;
import ucar.ma2.DataType;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriteable;

public class DimensionSupport implements IDimensionSupport {

	public final int NULL_VALUE = 0;
	public final double NULL_VALUE_DOUBLE = -9999.0d;
	public final short NULL_VALUE_SHORT = -9999;
	public final float NULL_VALUE_TIME = 9.96921E36f;
	private ArrayList<IDataEntry> dataEntries;
	private NetcdfFileWriteable cdfChromatogram;
	private IChromatogram chromatogram;
	private ScanSupport scanSupport;
	private Dimension byteString32;
	private Dimension byteString64;
	private Dimension numberOfScans;
	private Dimension numberOfScanIons;
	private Dimension instrumentNumber;
	private Dimension errorNumber;

	public DimensionSupport(NetcdfFileWriteable cdfChromatogram, IChromatogram chromatogram) {

		dataEntries = new ArrayList<IDataEntry>();
		this.cdfChromatogram = cdfChromatogram;
		this.chromatogram = chromatogram;
		this.scanSupport = new ScanSupport(chromatogram);
		initializeDimensions();
	}

	private void initializeDimensions() {

		cdfChromatogram.addDimension(CDFConstants.DIMENSION_2_BYTE_STRING, 2);
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_4_BYTE_STRING, 4);
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_8_BYTE_STRING, 8);
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_16_BYTE_STRING, 16);
		// ----------
		byteString32 = cdfChromatogram.addDimension(CDFConstants.DIMENSION_32_BYTE_STRING, 32);
		byteString64 = cdfChromatogram.addDimension(CDFConstants.DIMENSION_64_BYTE_STRING, 64);
		// ----------
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_128_BYTE_STRING, 128);
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_255_BYTE_STRING, 255);
		cdfChromatogram.addDimension(CDFConstants.DIMENSION_RANGE, 2);
		// ----------
		numberOfScanIons = cdfChromatogram.addDimension(CDFConstants.DIMENSION_POINT_NUMBER, chromatogram.getNumberOfScanIons(), true, true, true);
		errorNumber = cdfChromatogram.addDimension(CDFConstants.DIMENSION_ERROR_NUMBER, 1);
		numberOfScans = cdfChromatogram.addDimension(CDFConstants.DIMENSION_SCAN_NUMBER, chromatogram.getNumberOfScans());
		instrumentNumber = cdfChromatogram.addDimension(CDFConstants.DIMENSION_INSTRUMENT_NUMBER, 1);
		// ----------
	}

	@Override
	public void addVariableCharD2(String varName, Dimension firstDimension, Dimension secondDimension, String content) {

		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(firstDimension);
		dimension.add(secondDimension);
		cdfChromatogram.addVariable(varName, DataType.CHAR, dimension);
		ArrayChar.D2 values = new ArrayChar.D2(firstDimension.getLength(), secondDimension.getLength());
		values.setString(0, content);
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableDoubleD1(String varName, Dimension firstDimension, double value) {

		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(firstDimension);
		cdfChromatogram.addVariable(varName, DataType.DOUBLE, dimension);
		ArrayDouble.D1 values = new ArrayDouble.D1(firstDimension.getLength());
		for(int i = 0; i < firstDimension.getLength(); i++) {
			values.set(i, value);
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableShortD1(String varName, Dimension firstDimension, short value) {

		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(firstDimension);
		cdfChromatogram.addVariable(varName, DataType.SHORT, dimension);
		ArrayShort.D1 values = new ArrayShort.D1(firstDimension.getLength());
		for(int i = 0; i < firstDimension.getLength(); i++) {
			values.set(i, value);
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableIntD1(String varName, Dimension firstDimension, int value) {

		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(firstDimension);
		cdfChromatogram.addVariable(varName, DataType.INT, dimension);
		ArrayInt.D1 values = new ArrayInt.D1(firstDimension.getLength());
		for(int i = 0; i < firstDimension.getLength(); i++) {
			values.set(i, value);
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableScanAcquisitionTime() {

		String varName = CDFConstants.VARIABLE_SCAN_ACQUISITION_TIME;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.DOUBLE, dimension);
		ArrayDouble.D1 values = new ArrayDouble.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, chromatogram.getScan(i + 1).getRetentionTime() / 1000.0d);
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableActualScanNumber() {

		String varName = CDFConstants.VARIABLE_ACTUAL_SCAN_NUMBER;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.INT, dimension);
		ArrayInt.D1 values = new ArrayInt.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, i);
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableTotalIntensity() {

		String varName = CDFConstants.VARIABLE_TOTAL_INTENSITY;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.DOUBLE, dimension);
		ArrayDouble.D1 values = new ArrayDouble.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, chromatogram.getScan(i + 1).getTotalSignal());
		}
		cdfChromatogram.addVariableAttribute(varName, "units", "Arbitrary Intensity Units");
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableMassRangeMin() {

		String varName = CDFConstants.VARIABLE_MASS_RANGE_MIN;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.DOUBLE, dimension);
		ArrayDouble.D1 values = new ArrayDouble.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, AbstractIon.getMZ(scanSupport.getMinIon(i))); // TODO
																							// da
																							// funktioniert
																							// was
																							// mit
																							// den
																							// mz
																							// werten
																							// nicht
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableMassRangeMax() {

		String varName = CDFConstants.VARIABLE_MASS_RANGE_MAX;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.DOUBLE, dimension);
		ArrayDouble.D1 values = new ArrayDouble.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, AbstractIon.getMZ(scanSupport.getMaxIon(i))); // TODO
																							// da
																							// funktioniert
																							// was
																							// mit
																							// den
																							// mz
																							// werten
																							// nicht
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableScanIndex() {

		String varName = CDFConstants.VARIABLE_SCAN_INDEX;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.INT, dimension);
		ArrayInt.D1 values = new ArrayInt.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, scanSupport.getScanIndex(i + 1));
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariablePointCount() {

		String varName = CDFConstants.VARIABLE_POINT_COUNT;
		ArrayList<Dimension> dimension = new ArrayList<Dimension>();
		dimension.add(numberOfScans);
		cdfChromatogram.addVariable(varName, DataType.INT, dimension);
		ArrayInt.D1 values = new ArrayInt.D1(numberOfScans.getLength());
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			values.set(i, scanSupport.getPointCount(i + 1));
		}
		dataEntries.add(new DataEntry(varName, values));
	}

	@Override
	public void addVariableScanValues() {

		ISupplierMassSpectrum scan;
		String varNameMassValues = CDFConstants.VARIABLE_MASS_VALUES;
		ArrayList<Dimension> dimensionMassValues = new ArrayList<Dimension>();
		dimensionMassValues.add(numberOfScanIons);
		cdfChromatogram.addVariable(varNameMassValues, DataType.FLOAT, dimensionMassValues);
		ArrayFloat.D1 valuesIons = new ArrayFloat.D1(numberOfScanIons.getLength());
		String varNameTimeValues = CDFConstants.VARIABLE_TIME_VALUES;
		ArrayList<Dimension> dimensionTimeValues = new ArrayList<Dimension>();
		dimensionTimeValues.add(numberOfScanIons);
		cdfChromatogram.addVariable(varNameTimeValues, DataType.FLOAT, dimensionTimeValues);
		ArrayFloat.D1 valuesTime = new ArrayFloat.D1(numberOfScanIons.getLength());
		String varNameAbundanceValues = CDFConstants.VARIABLE_INTENSITY_VALUES;
		ArrayList<Dimension> dimensionAbundanceValues = new ArrayList<Dimension>();
		dimensionAbundanceValues.add(numberOfScanIons);
		cdfChromatogram.addVariable(varNameAbundanceValues, DataType.FLOAT, dimensionAbundanceValues);
		ArrayFloat.D1 valuesAbundance = new ArrayFloat.D1(numberOfScanIons.getLength());
		/*
		 * F-Search could not show ion values correctly. Has F-Search
		 * implemented the cdf format correctly?
		 */
		int counter = 0;
		// float retentionTime = 0.0f;
		for(int i = 0; i < numberOfScans.getLength(); i++) {
			scan = chromatogram.getScan(i + 1);
			// retentionTime = scan.getRetentionTime() / (1000.0f * 60.0f);
			for(IIon mf : scan.getIons()) {
				valuesIons.set(counter, AbstractIon.getMZ(mf.getMZ()));
				valuesTime.set(counter, NULL_VALUE_TIME);
				// valuesTime.set(counter, retentionTime);
				valuesAbundance.set(counter, mf.getAbundance());
				counter++;
			}
		}
		cdfChromatogram.addVariableAttribute(varNameMassValues, "units", "M/Z");
		cdfChromatogram.addVariableAttribute(varNameTimeValues, "units", "Seconds");
		cdfChromatogram.addVariableAttribute(varNameAbundanceValues, "units", "Arbitrary Intensity Units");
		dataEntries.add(new DataEntry(varNameMassValues, valuesIons));
		dataEntries.add(new DataEntry(varNameTimeValues, valuesTime));
		dataEntries.add(new DataEntry(varNameAbundanceValues, valuesAbundance));
	}

	@Override
	public ArrayList<IDataEntry> getDataEntries() {

		return dataEntries;
	}

	@Override
	public Dimension getByteString32() {

		return byteString32;
	}

	@Override
	public Dimension getByteString64() {

		return byteString64;
	}

	@Override
	public Dimension getNumberOfScans() {

		return numberOfScans;
	}

	@Override
	public Dimension getNumberOfScanIons() {

		return numberOfScanIons;
	}

	@Override
	public Dimension getInstrumentNumber() {

		return instrumentNumber;
	}

	@Override
	public Dimension getErrorNumber() {

		return errorNumber;
	}
}
