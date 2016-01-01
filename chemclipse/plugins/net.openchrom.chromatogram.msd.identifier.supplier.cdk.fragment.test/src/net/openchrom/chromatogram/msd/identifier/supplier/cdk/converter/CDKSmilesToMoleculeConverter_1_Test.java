/*******************************************************************************
 * Copyright (c) 2013, 2016 Marwin Wollschläger.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Marwin Wollschläger - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.msd.identifier.supplier.cdk.converter;

import org.openscience.cdk.interfaces.IMolecule;

import net.openchrom.chromatogram.msd.identifier.supplier.cdk.converter.CDKSmilesToMoleculeConverter;

import junit.framework.TestCase;

public class CDKSmilesToMoleculeConverter_1_Test extends TestCase {

	private CDKSmilesToMoleculeConverter converter;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		converter = new CDKSmilesToMoleculeConverter();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testMethod_1() {

		IMolecule molecule = converter.generate("c1=cc=cc=c1");
		assertNotNull(molecule);
	}

	public void testMethod_2() {

		IMolecule molecule = converter.generate(null);
		assertNull(molecule);
	}
}
