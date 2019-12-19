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
 *      IBM Corporation - initial API and implementation
 *  	Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog
 *      font should be activated and used by other components.
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.about.AboutBundleGroupData;
import org.fdesigner.workbench.internal.about.AboutFeaturesPage;
import org.fdesigner.workbench.internal.about.ProductInfoDialog;

/**
 * Displays information about the product plugins.
 *
 * PRIVATE This class is internal to the workbench and must not be called
 * outside the workbench.
 */
public class AboutFeaturesDialog extends ProductInfoDialog {
	/**
	 * Constructor for AboutFeaturesDialog.
	 *
	 * @param parentShell      the parent shell
	 * @param productName      the product name
	 * @param bundleGroupInfos the bundle info
	 */
	public AboutFeaturesDialog(Shell parentShell, String productName, AboutBundleGroupData[] bundleGroupInfos,
			AboutBundleGroupData initialSelection) {
		super(parentShell);
		AboutFeaturesPage page = new AboutFeaturesPage();
		page.setProductName(productName);
		page.setBundleGroupInfos(bundleGroupInfos);
		page.setInitialSelection(initialSelection);
		String title;
		if (productName != null)
			title = NLS.bind(WorkbenchMessages.AboutFeaturesDialog_shellTitle, productName);
		else
			title = WorkbenchMessages.AboutFeaturesDialog_SimpleTitle;
		initializeDialog(page, title, IWorkbenchHelpContextIds.ABOUT_FEATURES_DIALOG);
	}
}
