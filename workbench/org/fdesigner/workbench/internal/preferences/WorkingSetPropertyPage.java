/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.ui.jface.wizard.IWizard;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.ui.jface.wizard.WizardPage;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.preferences.WizardPropertyPage;

/**
 * Embeds a working set wizard for a given working set into a property page.
 *
 * @since 3.4
 */
public class WorkingSetPropertyPage extends WizardPropertyPage {

	private static final class ReadOnlyWizard extends Wizard {

		public ReadOnlyWizard() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean performFinish() {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addPages() {
			addPage(new ReadOnlyPage());
		}
	}

	private static final class ReadOnlyPage extends WizardPage {

		protected ReadOnlyPage() {
			super(WorkbenchMessages.WorkingSetPropertyPage_ReadOnlyWorkingSet_title);
			setDescription(WorkbenchMessages.WorkingSetPropertyPage_ReadOnlyWorkingSet_description);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new GridLayout(1, false));

			setControl(composite);
		}
	}

	private IWorkingSet fWorkingSet;

	public WorkingSetPropertyPage() {
		noDefaultAndApplyButton();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		fWorkingSet = Adapters.adapt(element, IWorkingSet.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void applyChanges() {
		// Wizard does all the work
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IWizard createWizard() {
		if (fWorkingSet.isEditable()) {
			return PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSetEditWizard(fWorkingSet);
		}

		return new ReadOnlyWizard();
	}

}
