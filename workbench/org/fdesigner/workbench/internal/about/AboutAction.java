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
package org.fdesigner.workbench.internal.about;

import org.fdesigner.runtime.core.IProduct;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.actions.ActionFactory;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.dialogs.AboutDialog;

/**
 * Creates an About dialog and opens it.
 */
public class AboutAction extends Action implements ActionFactory.IWorkbenchAction {

	/**
	 * The workbench window; or <code>null</code> if this action has been
	 * <code>dispose</code>d.
	 */
	private IWorkbenchWindow workbenchWindow;

	/**
	 * Creates a new <code>AboutAction</code>.
	 *
	 * @param window the window
	 */
	public AboutAction(IWorkbenchWindow window) {
		if (window == null) {
			throw new IllegalArgumentException();
		}

		this.workbenchWindow = window;

		// use message with no fill-in
		IProduct product = Platform.getProduct();
		String productName = null;
		if (product != null) {
			productName = product.getName();
		}
		if (productName == null) {
			productName = ""; //$NON-NLS-1$
		}
		setText(NLS.bind(WorkbenchMessages.AboutAction_text, productName));
		setToolTipText(NLS.bind(WorkbenchMessages.AboutAction_toolTip, productName));
		setId("about"); //$NON-NLS-1$
		setActionDefinitionId(IWorkbenchCommandConstants.HELP_ABOUT);
		window.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.ABOUT_ACTION);
	}

	@Override
	public void run() {
		// make sure action is not disposed
		if (workbenchWindow != null) {
			new AboutDialog(workbenchWindow.getShell()).open();
		}
	}

	@Override
	public void dispose() {
		workbenchWindow = null;
	}
}
