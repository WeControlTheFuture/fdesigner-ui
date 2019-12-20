/*******************************************************************************
 * Copyright (c) 2014-2016 Red Hat Inc., and others
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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.resources.IProject;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.PlatformUI;

/**
 * Opens the {@link SmartImportWizard} on selected project.
 *
 * @since 3.12
 *
 */
public class ConfigureProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) {
		IProject project = null;
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			Object item = ((IStructuredSelection)selection).getFirstElement();
			project = Adapters.adapt(item, IProject.class);
		}
		if (project == null) {
			return null;
		}
		File file = SmartImportWizard.toFile(project);
		if (file == null) {
			return null;
		}

		SmartImportWizard wizard = new SmartImportWizard();
		wizard.setInitialImportSource(file);
		// inherit workingSets
		Set<IWorkingSet> workingSets = new HashSet<>();
		for (IWorkingSet workingSet : PlatformUI.getWorkbench().getWorkingSetManager().getWorkingSets()) {
			for (IAdaptable element : workingSet.getElements()) {
				if (project.getAdapter(element.getClass()) == element) {
					workingSets.add(workingSet);
				}
			}
		}
		wizard.setInitialWorkingSets(workingSets);
		return new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard).open();
	}

}
