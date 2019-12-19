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
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.ui.jface.wizard.IWizardPage;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.dialogs.IWorkingSetNewWizard;
import org.fdesigner.workbench.dialogs.IWorkingSetPage;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.registry.WorkingSetDescriptor;
import org.fdesigner.workbench.internal.registry.WorkingSetRegistry;

/**
 * A new working set wizard allows the user to create a new working set using a
 * plugin specified working set page.
 *
 * @since 2.0
 * @see org.eclipse.ui.dialogs.IWorkingSetPage
 */
public class WorkingSetNewWizard extends Wizard implements IWorkingSetNewWizard {
	private WorkingSetTypePage workingSetTypePage;

	private IWorkingSetPage workingSetEditPage;

	private String editPageId;

	private IWorkingSet workingSet;

	private WorkingSetDescriptor[] descriptors;

	/**
	 * Creates a new instance of the receiver.
	 *
	 * @param descriptors the choice of descriptors
	 */
	public WorkingSetNewWizard(WorkingSetDescriptor[] descriptors) {
		super();
		Assert.isTrue(descriptors != null && descriptors.length > 0);
		this.descriptors = descriptors;
		setWindowTitle(WorkbenchMessages.WorkingSetNewWizard_title);
	}

	/**
	 * Overrides method in Wizard. Adds a page listing the available kinds of
	 * working sets. The second wizard page will depend on the selected working set
	 * type.
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();

		IWizardPage page;
		WorkingSetRegistry registry = WorkbenchPlugin.getDefault().getWorkingSetRegistry();

		if (descriptors.length > 1) {
			page = workingSetTypePage = new WorkingSetTypePage(this.descriptors);
		} else {
			editPageId = descriptors[0].getId();
			page = workingSetEditPage = registry.getWorkingSetPage(editPageId);
		}
		page.setWizard(this);
		addPage(page);
		setForcePreviousAndNextButtons(descriptors.length > 1);
	}

	/**
	 * Overrides method in Wizard.
	 *
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return (workingSetEditPage != null && workingSetEditPage.isPageComplete());
	}

	/**
	 * Overrides method in Wizard. Returns a working set page for creating the new
	 * working set. This second page is loaded from the plugin that defined the
	 * selected working set type.
	 *
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (workingSetTypePage != null && page == workingSetTypePage) {
			String pageId = workingSetTypePage.getSelection();
			if (pageId != null) {
				if (workingSetEditPage == null || pageId != editPageId) {
					WorkingSetRegistry registry = WorkbenchPlugin.getDefault().getWorkingSetRegistry();
					workingSetEditPage = registry.getWorkingSetPage(pageId);
					addPage(workingSetEditPage);
					editPageId = pageId;
				}
				return workingSetEditPage;
			}
		}
		return null;
	}

	/**
	 * Returns the new working set. Returns null if the wizard has been cancelled.
	 *
	 * @return the new working set or null if the wizard has been cancelled.
	 */
	@Override
	public IWorkingSet getSelection() {
		return workingSet;
	}

	/**
	 * Overrides method in Wizard. Stores the newly created working set and the id
	 * of the page used to create it.
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		workingSetEditPage.finish();
		workingSet = workingSetEditPage.getSelection();
		workingSet.setId(editPageId);
		return true;
	}
}
