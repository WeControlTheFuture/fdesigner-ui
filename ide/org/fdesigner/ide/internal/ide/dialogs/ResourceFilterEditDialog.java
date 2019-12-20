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
 *     Serge Beauchamp (Freescale Semiconductor) - initial API and implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.fdesigner.ide.dialogs.UIResourceFilterDescription;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IResourceFilterDescription;
import org.fdesigner.ui.jface.dialogs.IDialogConstants;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.dialogs.SelectionDialog;

/**
 * @since 3.6
 */
public class ResourceFilterEditDialog extends SelectionDialog {

	private ResourceFilterGroup resourceFilterGroup;

	/**
	 * Creates a resource filter edit dialog.
	 *
	 * @param parentShell
	 *            the parent shell
	 */
	public ResourceFilterEditDialog(Shell parentShell) {
		super(parentShell);
		setTitle(IDEWorkbenchMessages.ResourceFilterEditDialog_title);
		resourceFilterGroup = new ResourceFilterGroup();
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/**
	 * Set the container resource to be edited.
	 *
	 * @param container
	 */
	public void setContainer(IContainer container) {
		resourceFilterGroup.setContainer(container);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(shell,
				IIDEHelpContextIds.EDIT_RESOURCE_FILTER_DIALOG);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		resourceFilterGroup.createContents(dialogArea);
		return dialogArea;
	}

	@Override
	public boolean close() {
		resourceFilterGroup.dispose();
		return super.close();
	}

	/**
	 * @return the filters that were configured on this resource
	 */
	public UIResourceFilterDescription[] getFilters() {
		return resourceFilterGroup.getFilters();
	}

	/**
	 * @param filters
	 *            the initial filters of the dialog
	 */
	public void setFilters(UIResourceFilterDescription[] filters) {
		resourceFilterGroup.setFilters(filters);
	}

	/**
	 * @param filters
	 *            the initial filters of the dialog
	 */
	public void setFilters(IResourceFilterDescription[] filters) {
		resourceFilterGroup.setFilters(filters);
	}

	@Override
	protected void okPressed() {
		// Sets the dialog result to the selected path variable name(s).
		try {
			if (resourceFilterGroup.performOk())
				super.okPressed();
		} catch (Throwable t) {
			IDEWorkbenchPlugin.log(t.getMessage(), t);
		}
	}
}
