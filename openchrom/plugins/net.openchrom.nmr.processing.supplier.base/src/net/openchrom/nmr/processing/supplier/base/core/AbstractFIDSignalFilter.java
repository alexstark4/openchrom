/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.processing.supplier.base.core;

import org.eclipse.chemclipse.filter.FilterCategory;
import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;

public abstract class AbstractFIDSignalFilter<ConfigType> extends AbstractComplexSignalFilter<ConfigType, FIDMeasurement> {

	public AbstractFIDSignalFilter(Class<ConfigType> configClass) {
		super(configClass);
	}

	@Override
	public FilterCategory[] getFilterCategories() {

		return new FilterCategory[]{FilterCategory.FID};
	}

	@Override
	public boolean acceptsIMeasurement(IMeasurement item) {

		return item instanceof FIDMeasurement && accepts((FIDMeasurement)item);
	}

	protected boolean accepts(FIDMeasurement item) {

		return true;
	}
}
