/*******************************************************************************
 * Copyright (c) 2015, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.jtables.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public abstract class WriterTableObjectToRowsAbstract<T> extends WriterTableString {

	public WriterTableObjectToRowsAbstract(final File file) throws IOException {
		super(file);
	}

	public WriterTableObjectToRowsAbstract(final OutputStream stream) throws IOException {
		super(stream);
	}

	public WriterTableObjectToRowsAbstract(final Writer writer) throws IOException {
		super(writer);
	}

	public abstract WriterTableObjectToRowsAbstract<T> writeElement(String delimiter, T element) throws IOException;

	public WriterTableObjectToRowsAbstract<T> writeElement(final T element) throws IOException {

		writeElement(DEFAULT_DELIMITER, element);
		return this;
	}

	public WriterTableObjectToRowsAbstract<T> writeElements(final Collection<? extends T> elements) throws IOException {

		writeElements(DEFAULT_DELIMITER, elements);
		return this;
	}

	public WriterTableObjectToRowsAbstract<T> writeElements(final String delimiter, final Collection<? extends T> elements) throws IOException {

		final Iterator<? extends T> it = elements.iterator();
		while(it.hasNext()) {
			writeElement(delimiter, it.next());
		}
		return this;
	}
}
