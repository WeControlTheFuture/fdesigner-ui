/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Mickael Istria (Red Hat Inc.) - Bug 486901
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.actions;

import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.ISelectionChangedListener;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.INullSelectionListener;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.actions.ActionFactory;
import org.fdesigner.workbench.actions.PartEventAction;
import org.fdesigner.workbench.dialogs.PropertyDialogAction;

/**
 * Implementation for the action Property on the Project menu.
 */
public class ProjectPropertyDialogAction extends PartEventAction implements
		INullSelectionListener, ActionFactory.IWorkbenchAction {

	/**
	 * The workbench window; or <code>null</code> if this
	 * action has been <code>dispose</code>d.
	 */
	private IWorkbenchWindow workbenchWindow;

	/**
	 * Create a new dialog.
	 *
	 * @param window the window
	 */
	public ProjectPropertyDialogAction(IWorkbenchWindow window) {
		super(""); //$NON-NLS-1$
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.workbenchWindow = window;
		setText(IDEWorkbenchMessages.Workbench_projectProperties);
		setToolTipText(IDEWorkbenchMessages.Workbench_projectPropertiesToolTip);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IIDEHelpContextIds.PROJECT_PROPERTY_DIALOG_ACTION);
		workbenchWindow.getSelectionService().addSelectionListener(this);
		workbenchWindow.getPartService().addPartListener(this);
		setActionDefinitionId("org.eclipse.ui.project.properties"); //$NON-NLS-1$
	}

	/**
	 * Opens the project properties dialog.
	 */
	@Override
	public void run() {
		IProject project = getProject();
		if (project == null) {
			return;
		}

		SelProvider selProvider = new SelProvider();
		selProvider.projectSelection = new StructuredSelection(project);
		PropertyDialogAction propAction = new PropertyDialogAction(workbenchWindow, selProvider);
		propAction.run();
	}

	/**
	 * Update the enablement state when a the selection changes.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection sel) {
		setEnabled(getProject() != null);
	}

	/**
	 * Update the enablement state when a new part is activated.
	 */
	@Override
	public void partActivated(IWorkbenchPart part) {
		super.partActivated(part);
		setEnabled(getProject() != null);
	}

	/**
	 * Returns a project from the selection of the active part.
	 */
	private IProject getProject() {
		IWorkbenchPart part = getActivePart();
		Object selection = null;
		if (part instanceof IEditorPart) {
			selection = ((IEditorPart) part).getEditorInput();
		} else {
			ISelection sel = workbenchWindow.getSelectionService()
					.getSelection();
			if ((sel != null) && (sel instanceof IStructuredSelection)) {
				selection = ((IStructuredSelection) sel).getFirstElement();
			}
		}
		IResource resource = Adapters.adapt(selection, IResource.class);
		if (resource == null) {
			return null;
		}
		return resource.getProject();
	}

	@Override
	public void dispose() {
		if (workbenchWindow == null) {
			// action has already been disposed
			return;
		}
		workbenchWindow.getSelectionService().removeSelectionListener(this);
		workbenchWindow.getPartService().removePartListener(this);
		workbenchWindow = null;
	}

	/*
	 * Helper class to simulate a selection provider
	 */
	private static final class SelProvider implements ISelectionProvider {
		protected IStructuredSelection projectSelection = StructuredSelection.EMPTY;

		@Override
		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			// do nothing
		}

		@Override
		public ISelection getSelection() {
			return projectSelection;
		}

		@Override
		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			// do nothing
		}

		@Override
		public void setSelection(ISelection selection) {
			// do nothing
		}
	}
}
