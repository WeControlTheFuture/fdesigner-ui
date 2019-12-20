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
import org.fdesigner.ide.internal.wizards.datatransfer.WizardArchiveFileResourceExportPage1;
import org.fdesigner.resources.IResource;
import org.fdesigner.ui.jface.dialogs.IDialogSettings;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.workbench.IExportWizard;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * Standard workbench wizard for exporting resources from the workspace to a zip
 * file.
 * <p>
 * This class may be instantiated and used without further configuration; this
 * class is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * IWizard wizard = new ZipFileExportWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 * <p>
 * During the call to <code>open</code>, the wizard dialog is presented to the
 * user. When the user hits Finish, the user-selected workspace resources are
 * exported to the user-specified zip file, the dialog closes, and the call to
 * <code>open</code> returns.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ZipFileExportWizard extends Wizard implements IExportWizard {
	private IStructuredSelection selection;

	private WizardArchiveFileResourceExportPage1 mainPage;

	/**
	 * Creates a wizard for exporting workspace resources to a zip file.
	 */
	public ZipFileExportWizard() {
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings
				.getSection("ZipFileExportWizard");//$NON-NLS-1$
		if (section == null) {
			section = workbenchSettings.addNewSection("ZipFileExportWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new WizardArchiveFileResourceExportPage1(selection);
		addPage(mainPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.selection = currentSelection;
		List<IResource> selectedResources = IDE.computeSelectedResources(currentSelection);
		if (!selectedResources.isEmpty()) {
			this.selection = new StructuredSelection(selectedResources);
		}

		setWindowTitle(DataTransferMessages.DataTransfer_export);
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		return mainPage.finish();
	}
}
