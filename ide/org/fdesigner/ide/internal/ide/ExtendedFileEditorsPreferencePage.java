/*******************************************************************************
 * Copyright (c) 2015, 2018 Red Hat Inc and Others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - initial API and implementation
 *     Patrik Suzzi <psuzzi@gmail.com> - Bug 497156
 *******************************************************************************/
package org.fdesigner.ide.internal.ide;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.fdesigner.ide.IDE;
import org.fdesigner.ide.internal.ide.registry.UnassociatedEditorStrategyRegistry;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.preference.IPersistentPreferenceStore;
import org.fdesigner.ui.jface.preference.IPreferenceStore;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.viewers.ArrayContentProvider;
import org.fdesigner.ui.jface.viewers.ComboViewer;
import org.fdesigner.ui.jface.viewers.LabelProvider;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.util.Policy;
import org.fdesigner.workbench.internal.dialogs.FileEditorsPreferencePage;

/**
 * @since 3.12
 *
 */
public class ExtendedFileEditorsPreferencePage extends FileEditorsPreferencePage {

	private IPreferenceStore idePreferenceStore;

	@Override
	protected Composite createContents(Composite parent) {
		Composite res = (Composite)super.createContents(parent);

		final UnassociatedEditorStrategyRegistry registry = IDEWorkbenchPlugin.getDefault()
				.getUnassociatedEditorStrategyRegistry();
		Composite defaultStrategyComposite = new Composite(res, SWT.NONE);
		// Gets the first and only Link in the parent page
		Optional<Control> cLink = Stream.of(res.getChildren()).filter(c -> c instanceof Link).findFirst();
		defaultStrategyComposite.moveBelow(cLink.get());
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		defaultStrategyComposite.setLayout(layout);
		defaultStrategyComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		Label unknownTypeStrategyLabel = new Label(defaultStrategyComposite, SWT.NONE);
		unknownTypeStrategyLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		unknownTypeStrategyLabel
				.setText(IDEWorkbenchMessages.ExtendedFileEditorsPreferencePage_strategyForUnassociatedFiles);
		ComboViewer viewer = new ComboViewer(defaultStrategyComposite);
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object o) {
				String id = (String) o;
				String label = registry.getLabel(id);
				if (label != null) {
					return label;
				}
				IDEWorkbenchPlugin.log("Could not resolve unknownEditorStrategy '" + id + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				return NLS.bind(IDEWorkbenchMessages.ExtendedFileEditorsPreferencePage_labelNotResolved, id);
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(registry.retrieveAllStrategies());
		this.idePreferenceStore = IDEWorkbenchPlugin.getDefault().getPreferenceStore();
		viewer.setSelection(
				new StructuredSelection(this.idePreferenceStore.getString(IDE.UNASSOCIATED_EDITOR_STRATEGY_PREFERENCE_KEY)));
		viewer.addSelectionChangedListener(
				event -> idePreferenceStore.setValue(IDE.UNASSOCIATED_EDITOR_STRATEGY_PREFERENCE_KEY,
						(String) event.getStructuredSelection().getFirstElement()));

		return res;
	}

	@Override
	public boolean performOk() {
		if (idePreferenceStore != null && idePreferenceStore.needsSaving()
				&& idePreferenceStore instanceof IPersistentPreferenceStore) {
			try {
				((IPersistentPreferenceStore) idePreferenceStore).save();
			} catch (IOException e) {
				String message = JFaceResources.format("PreferenceDialog.saveErrorMessage", getTitle(), //$NON-NLS-1$
						e.getMessage());
				Policy.getStatusHandler().show(new Status(IStatus.ERROR, Policy.JFACE, message, e),
						JFaceResources.getString("PreferenceDialog.saveErrorTitle")); //$NON-NLS-1$
			}
		}
		return super.performOk();
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		idePreferenceStore.setToDefault(IDE.UNASSOCIATED_EDITOR_STRATEGY_PREFERENCE_KEY);
	}

}
