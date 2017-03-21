/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.msd.processor.supplier.massshiftdetector.ui.editors;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.editors.AbstractExtendedEditorPage;
import org.eclipse.chemclipse.support.ui.editors.IExtendedEditorPage;
import org.eclipse.chemclipse.support.ui.wizards.ChromatogramWizardElements;
import org.eclipse.chemclipse.support.ui.wizards.IChromatogramWizardElements;
import org.eclipse.chemclipse.swt.ui.components.chromatogram.MultipleChromatogramOffsetUI;
import org.eclipse.chemclipse.swt.ui.support.AxisTitlesIntensityScale;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.msd.ui.wizards.ChromatogramInputEntriesWizard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import net.openchrom.chromatogram.msd.processor.supplier.massshiftdetector.core.MassShiftDetector;
import net.openchrom.chromatogram.msd.processor.supplier.massshiftdetector.model.ProcessorData;
import net.openchrom.chromatogram.msd.processor.supplier.massshiftdetector.preferences.PreferenceSupplier;
import net.openchrom.chromatogram.msd.processor.supplier.massshiftdetector.ui.runnables.ChromatogramImportRunnable;

public class PageSettings extends AbstractExtendedEditorPage implements IExtendedEditorPage {

	private static final Logger logger = Logger.getLogger(PageSettings.class);
	//
	private EditorProcessor editorProcessor;
	//
	private Text c12ChromatogramText;
	private Text c13ChromatogramText;
	private Spinner shiftLevelSpinner;
	private Button useAbsoluteValuesCheckBox;
	private Label labelNotes;
	private Label labelChromatogramInfo;
	private MultipleChromatogramOffsetUI chromatogramOverlay;

	public PageSettings(EditorProcessor editorProcessor, Composite container) {
		super("Settings", container, true);
		this.editorProcessor = editorProcessor;
	}

	@Override
	public void fillBody(ScrolledForm scrolledForm) {

		Composite body = scrolledForm.getBody();
		body.setLayout(new TableWrapLayout());
		body.setLayout(createFormTableWrapLayout(true, 3));
		/*
		 * 3 column layout
		 */
		createPropertiesSection(body);
		createProcessSection(body);
		createChromatogramSection(body);
	}

	private void createPropertiesSection(Composite parent) {

		Section section = createSection(parent, 3, "Settings", "The selected settings will be used for the current analysis.");
		Composite client = createClient(section, 3);
		//
		createC12ChromatogramText(client);
		createC13ChromatogramText(client);
		createShiftsSpinner(client);
		createUseAbsoluteValuesCheckBox(client);
		createNotesLabel(client);
		/*
		 * Add the client to the section.
		 */
		section.setClient(client);
	}

	private void createC12ChromatogramText(Composite client) {

		createLabel(client, "C12 - Chromatogram");
		//
		c12ChromatogramText = createText(client, SWT.BORDER, "");
		c12ChromatogramText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		Button button = new Button(client, SWT.PUSH);
		button.setText("Select");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IChromatogramWizardElements chromatogramWizardElements = new ChromatogramWizardElements();
				ChromatogramInputEntriesWizard inputWizard = new ChromatogramInputEntriesWizard(chromatogramWizardElements, "C12 - Chromatogram", "Select the C12 chromatogram.", PreferenceSupplier.getFilterPathC12Chromatogram());
				WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), inputWizard);
				wizardDialog.create();
				//
				if(wizardDialog.open() == WizardDialog.OK) {
					List<String> selectedChromatograms = chromatogramWizardElements.getSelectedChromatograms();
					if(selectedChromatograms.size() > 0) {
						String selectedChromatogram = chromatogramWizardElements.getSelectedChromatograms().get(0);
						c12ChromatogramText.setText(selectedChromatogram);
						//
						File file = new File(selectedChromatogram);
						if(file.exists()) {
							PreferenceSupplier.setFilterPathC12Chromatogram(file.getParentFile().toString());
						}
					}
				}
			}
		});
	}

	private void createC13ChromatogramText(Composite client) {

		createLabel(client, "C13 - Chromatogram");
		//
		c13ChromatogramText = createText(client, SWT.BORDER, "");
		c13ChromatogramText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		Button button = new Button(client, SWT.PUSH);
		button.setText("Select");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IChromatogramWizardElements chromatogramWizardElements = new ChromatogramWizardElements();
				ChromatogramInputEntriesWizard inputWizard = new ChromatogramInputEntriesWizard(chromatogramWizardElements, "C13 - Chromatogram", "Select the C13 chromatogram.", PreferenceSupplier.getFilterPathC13Chromatogram());
				WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), inputWizard);
				wizardDialog.create();
				//
				if(wizardDialog.open() == WizardDialog.OK) {
					List<String> selectedChromatograms = chromatogramWizardElements.getSelectedChromatograms();
					if(selectedChromatograms.size() > 0) {
						String selectedChromatogram = chromatogramWizardElements.getSelectedChromatograms().get(0);
						c13ChromatogramText.setText(selectedChromatogram);
						//
						File file = new File(selectedChromatogram);
						if(file.exists()) {
							PreferenceSupplier.setFilterPathC13Chromatogram(file.getParentFile().toString());
						}
					}
				}
			}
		});
	}

	private void createShiftsSpinner(Composite client) {

		createLabel(client, "Number of shifts");
		//
		shiftLevelSpinner = new Spinner(client, SWT.BORDER);
		shiftLevelSpinner.setMinimum(MassShiftDetector.MIN_LEVEL);
		shiftLevelSpinner.setMaximum(MassShiftDetector.MAX_LEVEL);
		shiftLevelSpinner.setIncrement(MassShiftDetector.INCREMENT_LEVEL);
		//
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 50;
		gridData.heightHint = 20;
		shiftLevelSpinner.setLayoutData(gridData);
		shiftLevelSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ProcessorData processorRawData = editorProcessor.getProcessorData();
				processorRawData.setLevel(shiftLevelSpinner.getSelection());
			}
		});
	}

	private void createUseAbsoluteValuesCheckBox(Composite client) {

		useAbsoluteValuesCheckBox = new Button(client, SWT.CHECK);
		useAbsoluteValuesCheckBox.setText("Use absolute values");
		useAbsoluteValuesCheckBox.setSelection(true);
		useAbsoluteValuesCheckBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		useAbsoluteValuesCheckBox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ProcessorData processorRawData = editorProcessor.getProcessorData();
				processorRawData.setUseAbsoluteValues(useAbsoluteValuesCheckBox.getSelection());
			}
		});
	}

	private void createNotesLabel(Composite client) {

		labelNotes = createLabel(client, "Please have a look at the retention time range 5 - 10 minutes.");
		labelNotes.setBackground(Colors.YELLOW);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		labelNotes.setLayoutData(gridData);
	}

	private void createChromatogramInfo(Composite client) {

		labelChromatogramInfo = createLabel(client, "");
		labelChromatogramInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void createChromatogramOverlay(Composite client) {

		Composite compositeRawData = new Composite(client, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;
		compositeRawData.setLayoutData(gridData);
		compositeRawData.setLayout(new FillLayout());
		chromatogramOverlay = new MultipleChromatogramOffsetUI(compositeRawData, SWT.NONE, new AxisTitlesIntensityScale());
	}

	private void createProcessSection(Composite parent) {

		Section section = createSection(parent, 3, "Process", "The selected chromatograms are processed with the given settings to detect mass shifts.");
		Composite client = createClient(section);
		/*
		 * Process
		 */
		createOverlayHyperlink(client, "Overlay Selected Chromatograms");
		createCalculateHyperlink(client, "Calculate Mass Shifts");
		/*
		 * Add the client to the section.
		 */
		section.setClient(client);
	}

	private ImageHyperlink createOverlayHyperlink(Composite client, String text) {

		Shell shell = Display.getCurrent().getActiveShell();
		ImageHyperlink imageHyperlink = getFormToolkit().createImageHyperlink(client, SWT.NONE);
		imageHyperlink.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImage.SIZE_16x16));
		imageHyperlink.setText(text);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalIndent = HORIZONTAL_INDENT;
		imageHyperlink.setLayoutData(gridData);
		imageHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			public void linkActivated(HyperlinkEvent e) {

				List<IChromatogramSelection> chromatogramSelections = new ArrayList<IChromatogramSelection>();
				String pathChromatogramC12 = c12ChromatogramText.getText().trim();
				String pathChromatogramC13 = c13ChromatogramText.getText().trim();
				ChromatogramImportRunnable runnable = new ChromatogramImportRunnable(pathChromatogramC12, pathChromatogramC13);
				ProgressMonitorDialog monitor = new ProgressMonitorDialog(shell);
				//
				try {
					monitor.run(true, true, runnable);
					chromatogramSelections = runnable.getChromatogramSelections();
				} catch(InterruptedException e1) {
					logger.warn(e1);
				} catch(InvocationTargetException e1) {
					logger.warn(e1);
				}
				//
				updateChromatogramSelections(chromatogramSelections);
			}
		});
		return imageHyperlink;
	}

	private ImageHyperlink createCalculateHyperlink(Composite client, String text) {

		Shell shell = Display.getCurrent().getActiveShell();
		ImageHyperlink imageHyperlink = getFormToolkit().createImageHyperlink(client, SWT.NONE);
		imageHyperlink.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE, IApplicationImage.SIZE_16x16));
		imageHyperlink.setText(text);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalIndent = HORIZONTAL_INDENT;
		imageHyperlink.setLayoutData(gridData);
		imageHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			public void linkActivated(HyperlinkEvent e) {

				ProcessorData processorRawData = editorProcessor.getProcessorData();
				if(processorRawData.getChromatogramReference() != null && processorRawData.getChromatogramShifted() != null) {
					editorProcessor.focusPage(EditorProcessor.PAGE_INDEX_SHIFT_HEATMAP);
				} else {
					MessageDialog.openWarning(shell, "Chromatogram Selection", "Please select a reference and a shifted chromatogram.");
				}
			}
		});
		return imageHyperlink;
	}

	private void createChromatogramSection(Composite parent) {

		Section section = createSection(parent, 3, "Overlay", "The selected chromatogram are displayed here in overlay modus.");
		Composite client = createClient(section, 1);
		//
		createChromatogramInfo(client);
		createChromatogramOverlay(client);
		/*
		 * Add the client to the section.
		 */
		section.setClient(client);
	}

	private void updateChromatogramSelections(List<IChromatogramSelection> chromatogramSelections) {

		if(chromatogramSelections.size() != 2) {
			labelChromatogramInfo.setText("Please select two reference chromatograms.");
			labelChromatogramInfo.setForeground(Colors.WHITE);
			labelChromatogramInfo.setBackground(Colors.RED);
		} else {
			IChromatogram chromatogram1 = chromatogramSelections.get(0).getChromatogram();
			IChromatogram chromatogram2 = chromatogramSelections.get(1).getChromatogram();
			int numberOfScans1 = chromatogram1.getNumberOfScans();
			int numberOfScans2 = chromatogram2.getNumberOfScans();
			//
			ProcessorData processorRawData = editorProcessor.getProcessorData();
			processorRawData.setChromatogramReference((IChromatogramMSD)chromatogram1);
			processorRawData.setChromatogramShifted((IChromatogramMSD)chromatogram2);
			//
			if(numberOfScans1 != numberOfScans2) {
				labelChromatogramInfo.setText("The selected chromatograms have a different number of scans (" + numberOfScans1 + " vs. " + numberOfScans2 + ").");
				labelChromatogramInfo.setForeground(Colors.BLACK);
				labelChromatogramInfo.setBackground(Colors.YELLOW);
			} else {
				labelChromatogramInfo.setText("The selected chromatograms are valid.");
				labelChromatogramInfo.setForeground(Colors.BLACK);
				labelChromatogramInfo.setBackground(Colors.GREEN);
			}
		}
		//
		chromatogramOverlay.updateSelection(chromatogramSelections, true);
	}
}
