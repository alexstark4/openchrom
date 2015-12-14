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
package net.sf.kerner.utils;

import net.sf.kerner.utils.transformer.TransformerToString;

public class TransformerToStringReplaceEmpty<E> implements TransformerToString<E> {

	public final static String DEFAULT_EMPTY_STRING = " ";
	private String emtpyString = DEFAULT_EMPTY_STRING;

	public synchronized String getEmtpyString() {

		return emtpyString;
	}

	public synchronized void setEmtpyString(final String emtpyString) {

		this.emtpyString = emtpyString;
	}

	public String transform(final E element) {

		if(element == null) {
			return getEmtpyString();
		}
		String result = element.toString();
		if(result.length() == 0) {
			result = getEmtpyString();
		}
		return result;
	}
}
