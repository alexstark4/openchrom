/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.classifier.supplier.ratios.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.provider.ICellColorProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;

import net.openchrom.xxd.classifier.supplier.ratios.model.TraceRatio;

public class TraceRatioColorProvider implements ICellColorProvider {

	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.000");

	@Override
	public void setCellColor(ExtendedTableViewer tableViewer) {

		TableViewerColumn tableViewerColumn = tableViewer.getTableViewerColumn(TraceRatioResultTitles.DEVIATION);
		if(tableViewerColumn != null) {
			tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {

					if(cell != null) {
						Color background = cell.getBackground();
						Color foreground = cell.getForeground();
						//
						TraceRatio traceRatio = (TraceRatio)cell.getElement();
						double deviation = traceRatio.getDeviation();
						double deviationWarn = traceRatio.getDeviationWarn();
						double deviationError = traceRatio.getDeviationError();
						//
						if(deviation < deviationWarn) {
							cell.setBackground(Colors.GREEN);
							cell.setForeground(Colors.BLACK);
						} else if(deviation >= deviationWarn && deviation < deviationError) {
							cell.setBackground(Colors.YELLOW);
							cell.setForeground(Colors.BLACK);
						} else if(deviation >= deviationError) {
							cell.setBackground(Colors.RED);
							cell.setForeground(Colors.BLACK);
						} else {
							cell.setBackground(background);
							cell.setForeground(foreground);
						}
						//
						cell.setText(decimalFormat.format(deviation));
						super.update(cell);
					}
				}
			});
		}
	}
}
