/*******************************************************************************
 * Copyright (c) 2015, 2016 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.kerner.utils;

import net.sf.kerner.utils.transformer.Transformer;

/**
 *
 *
 * <p>
 * <b>Example:</b><br>
 *
 * </p>
 * <p>
 *
 * <pre>
 * TODO example
 * </pre>
 *
 * </p>
 * <p>
 * last reviewed: 0000-00-00
 * </p>
 *
 * @author <a href="mailto:alexanderkerner24@gmail.com">Alexander Kerner</a>
 *
 * @param <O>
 */
public interface View<O> extends Transformer<O, O> {

	O getOriginal();
}
