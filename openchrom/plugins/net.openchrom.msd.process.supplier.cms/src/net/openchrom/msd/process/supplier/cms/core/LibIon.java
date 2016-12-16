/*******************************************************************************
 * Copyright (c) 2016 whitlow.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * whitlow - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.process.supplier.cms.core;

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;

class LibIon extends GenIon {

	// IRegularLibraryMassSpectrum libraryRef; // this ion was found in this library
	LibComponent componentRef; // this ion came from this library component

	LibIon(double mass, double abundance, int compSequence, LibComponent ref) {
		super(mass, abundance);
		ionCompSequence = compSequence;
		componentRef = ref;
	}
}
