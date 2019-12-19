/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alain Bernard <alain.bernard1224@gmail.com> - Bug 281490
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.Path;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.wizard.IWizardNode;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchWizard;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.activities.ws.WorkbenchTriggerPoints;
import org.fdesigner.workbench.internal.registry.WizardsRegistryReader;
import org.fdesigner.workbench.model.AdaptableList;
import org.fdesigner.workbench.wizards.IWizardCategory;

/**
 * The import wizard allows the user to choose which nested import wizard to
 * run. The set of available wizards comes from the import wizard extension
 * point.
 */
public class ImportWizard extends Wizard {

	// the list selection page
	static class SelectionPage extends WorkbenchWizardListSelectionPage {
		SelectionPage(IWorkbench w, IStructuredSelection ss, AdaptableList e, String s) {
			super(w, ss, e, s, WorkbenchTriggerPoints.IMPORT_WIZARDS);
		}

		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			getWorkbench().getHelpSystem().setHelp(getControl(),
					IWorkbenchHelpContextIds.IMPORT_WIZARD_SELECTION_WIZARD_PAGE);
		}

		@Override
		public IWizardNode createWizardNode(WorkbenchWizardElement element) {
			return new WorkbenchWizardNode(this, element) {
				@Override
				public IWorkbenchWizard createWizard() throws CoreException {
					return wizardElement.createWizard();
				}
			};
		}

	}

	private IStructuredSelection selection;

	private IWorkbench workbench;

	/**
	 * Creates the wizard's pages lazily.
	 */
	@Override
	public void addPages() {
		addPage(new SelectionPage(this.workbench, this.selection, getAvailableImportWizards(),
				WorkbenchMessages.ImportWizard_selectWizard));
	}

	/**
	 * Returns the import wizards that are available for invocation.
	 */
	protected AdaptableList getAvailableImportWizards() {
		// TODO: imports are still flat - we need to get at the flat list. All
		// wizards will be in the "other" category.
		IWizardCategory root = WorkbenchPlugin.getDefault().getImportWizardRegistry().getRootCategory();
		WizardCollectionElement otherCategory = (WizardCollectionElement) root
				.findCategory(new Path(WizardsRegistryReader.UNCATEGORIZED_WIZARD_CATEGORY));
		if (otherCategory == null) {
			return new AdaptableList();
		}
		return otherCategory.getWizardAdaptableList();
	}

	/**
	 * Initializes the wizard.
	 *
	 * @param aWorkbench       the workbench
	 * @param currentSelection the current selection
	 */
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		this.workbench = aWorkbench;
		this.selection = currentSelection;

		setWindowTitle(WorkbenchMessages.ImportWizard_title);
		setDefaultPageImageDescriptor(
				WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_WIZBAN_IMPORT_WIZ));
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		((SelectionPage) getPages()[0]).saveWidgetValues();
		return true;
	}
}
