/*******************************************************************************
 *  Copyright (c) 2015 Lablicate UG (haftungsbeschränkt).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.kerner.utils.collections;

import java.util.Collection;
import java.util.Set;

import net.sf.kerner.utils.collections.set.FactoryLinkedHashSet;
import net.sf.kerner.utils.collections.set.TransformerSet;

public class TransformerObjectToHashCode extends TransformerAbstract<Object, Integer> implements TransformerSet<Object, Integer> {

	public TransformerObjectToHashCode() {

		super(new FactoryLinkedHashSet<Integer>());
	}

	public Integer transform(final Object element) {

		return element.hashCode();
	}

	@Override
	public Set<Integer> transformCollection(final Collection<? extends Object> element) {

		return (Set<Integer>)super.transformCollection(element);
	}
}
