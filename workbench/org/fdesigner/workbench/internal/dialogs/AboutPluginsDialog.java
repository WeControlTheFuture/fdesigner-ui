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
 *		IBM Corporation - initial API and implementation
 *  	Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog
 * 		font should be activated and used by other components.
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.framework.framework.Bundle;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.about.AboutPluginsPage;
import org.fdesigner.workbench.internal.about.ProductInfoDialog;

/**
 * Displays information about the product plugins.
 *
 * PRIVATE this class is internal to the ide
 */
public class AboutPluginsDialog extends ProductInfoDialog {
	public AboutPluginsDialog(Shell parentShell, String productName, Bundle[] bundles, String title, String message,
			String helpContextId) {
		super(parentShell);
		AboutPluginsPage page = new AboutPluginsPage();
		page.setHelpContextId(helpContextId);
		page.setBundles(bundles);
		page.setMessage(message);
		if (title == null && page.getProductName() != null)
			title = NLS.bind(WorkbenchMessages.AboutPluginsDialog_shellTitle, productName);
		initializeDialog(page, title, helpContextId);
	}
}
