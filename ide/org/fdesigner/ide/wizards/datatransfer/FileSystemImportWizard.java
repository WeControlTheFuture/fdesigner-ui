/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
package org.fdesigner.ide.wizards.datatransfer;

import java.util.List;

import org.fdesigner.ide.IDE;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ide.internal.wizards.datatransfer.DataTransferMessages;
import org.fdesigner.ide.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;
import org.fdesigner.resources.IResource;
import org.fdesigner.ui.jface.dialogs.IDialogSettings;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.workbench.IImportWizard;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * Standard workbench wizard for importing resources from the local file system
 * into the workspace.
 * <p>
 * This class may be instantiated and used without further configuration; this
 * class is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * IWizard wizard = new FileSystemImportWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 * <p>
 * During the call to <code>open</code>, the wizard dialog is presented to the
 * user. When the user hits Finish, the user-selected files are imported into
 * the workspace, the dialog closes, and the call to <code>open</code> returns.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileSystemImportWizard extends Wizard implements IImportWizard {
	private IWorkbench workbench;

	private IStructuredSelection selection;

	private WizardFileSystemResourceImportPage1 mainPage;

	/**
	 * Creates a wizard for importing resources into the workspace from
	 * the file system.
	 */
	public FileSystemImportWizard() {
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings
				.getSection("FileSystemImportWizard");//$NON-NLS-1$
		if (section == null) {
			section = workbenchSettings.addNewSection("FileSystemImportWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new WizardFileSystemResourceImportPage1(workbench, selection);
		addPage(mainPage);
	}


	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.workbench = workbench;
		this.selection = currentSelection;

		List<IResource> selectedResources = IDE.computeSelectedResources(currentSelection);
		if (!selectedResources.isEmpty()) {
			this.selection = new StructuredSelection(selectedResources);
		}

		setWindowTitle(DataTransferMessages.DataTransfer_importTitle);
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importdir_wiz.png"));//$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		return mainPage.finish();
	}
}
