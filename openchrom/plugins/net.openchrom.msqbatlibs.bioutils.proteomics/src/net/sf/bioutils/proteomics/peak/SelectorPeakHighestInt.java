/*******************************************************************************
 * Copyright (c) 2015 Lablicate UG (haftungsbeschränkt).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.bioutils.proteomics.peak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sf.kerner.utils.collections.Selector;
import net.sf.kerner.utils.collections.comparator.ComparatorInverter;

public class SelectorPeakHighestInt<T extends Peak> implements Selector<T> {

	public T select(final Collection<? extends T> elements) {

		final ArrayList<T> list = new ArrayList<T>(elements);
		Collections.sort(list, new ComparatorInverter<Peak>(new ComparatorPeakByIntensity()));
		return list.get(0);
	}
}
