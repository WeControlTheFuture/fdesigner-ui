/*******************************************************************************
 * Copyright (c) 2014-2015 Red Hat Inc., and others
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
 ******************************************************************************/
package org.fdesigner.ide.internal.wizards.datatransfer;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.PlatformUI;

/**
 * Command to open a {@link SmartImportWizard}
 * 
 * @since 3.12
 *
 */
public class OpenFolderCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		ISelection sel = workbench.getActiveWorkbenchWindow().getSelectionService().getSelection();
		IStructuredSelection structuredSel = null;
		if (sel != null && sel instanceof IStructuredSelection) {
			structuredSel = (IStructuredSelection)sel;
		}
		SmartImportWizard wizard = new SmartImportWizard();
		wizard.init(workbench, structuredSel);
		new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard).open();
		return null;
	}

}