/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.fdesigner.ide.extensions.actions;

import java.util.Iterator;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.ide.wizards.newresource.BasicNewFileResourceWizard;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.window.IShellProvider;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.ISharedImages;
import org.fdesigner.workbench.PlatformUI;

/**
 * Standard action for creating a file resource within the currently
 * selected folder or project.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @deprecated should use NewWizardMenu to populate a New submenu instead (see Navigator view)
 * @noextend This class is not intended to be subclassed by clients.
 */
@Deprecated
public class CreateFileAction extends SelectionListenerAction {

	/**
	 * The id of this action.
	 */
	public static final String ID = PlatformUI.PLUGIN_ID + ".CreateFileAction";//$NON-NLS-1$

	/**
	 * The shell in which to show any dialogs.
	 */
	protected IShellProvider shellProvider;

	/**
	 * Creates a new action for creating a file resource.
	 *
	 * @param shell the shell for any dialogs
	 *
	 * @deprecated {@link #CreateFileAction(IShellProvider)}
	 */
	@Deprecated
	public CreateFileAction(final Shell shell) {
		super(IDEWorkbenchMessages.CreateFileAction_text);
		Assert.isNotNull(shell);
		shellProvider = () -> shell;
		initAction();
	}

	/**
	 * Creates a new action for creating a file resource.
	 *
	 * @param provider the shell for any dialogs
	 *
	 * @deprecated see deprecated tag on class
	 * @since 3.4
	 */
	@Deprecated
	public CreateFileAction(IShellProvider provider){
		super(IDEWorkbenchMessages.CreateFileAction_toolTip);
		Assert.isNotNull(provider);
		shellProvider = provider;
		initAction();
	}
	/**
	 * Initializes for the constructor.
	 */
	private void initAction(){
		setToolTipText(IDEWorkbenchMessages.CreateFileAction_toolTip);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
		setId(ID);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IIDEHelpContextIds.CREATE_FILE_ACTION);
	}
	/**
	 * The <code>CreateFileAction</code> implementation of this
	 * <code>IAction</code> method opens a <code>BasicNewFileResourceWizard</code>
	 * in a wizard dialog under the shell passed to the constructor.
	 */
	@Override
	public void run() {
		BasicNewFileResourceWizard wizard = new BasicNewFileResourceWizard();
		wizard.init(PlatformUI.getWorkbench(), getStructuredSelection());
		wizard.setNeedsProgressMonitor(true);
		WizardDialog dialog = new WizardDialog(shellProvider.getShell(), wizard);
		dialog.create();
		dialog.getShell().setText(
				IDEWorkbenchMessages.CreateFileAction_title);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
				IIDEHelpContextIds.NEW_FILE_WIZARD);
		dialog.open();
	}

	/**
	 * The <code>CreateFileAction</code> implementation of this
	 * <code>SelectionListenerAction</code> method enables the action only
	 * if the selection contains folders and open projects.
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection s) {
		if (!super.updateSelection(s)) {
			return false;
		}
		Iterator<? extends IResource> resources = getSelectedResources().iterator();
		while (resources.hasNext()) {
			IResource resource = resources.next();
			if (!resourceIsType(resource, IResource.PROJECT | IResource.FOLDER)
					|| !resource.isAccessible()) {
				return false;
			}
		}
		return true;
	}
}
