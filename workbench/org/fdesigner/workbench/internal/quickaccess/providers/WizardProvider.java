/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 472654
 *******************************************************************************/

package org.fdesigner.workbench.internal.quickaccess.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.activities.WorkbenchActivityHelper;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;
import org.fdesigner.workbench.wizards.IWizardCategory;
import org.fdesigner.workbench.wizards.IWizardDescriptor;

/**
 * @since 3.3
 *
 */
public class WizardProvider extends QuickAccessProvider {

	private QuickAccessElement[] cachedElements;
	private Map<String, WizardElement> idToElement = new HashMap<>();

	@Override
	public QuickAccessElement findElement(String id, String filter) {
		getElements();
		return idToElement.get(id);
	}

	@Override
	public QuickAccessElement[] getElements() {
		if (cachedElements == null) {
			IWizardCategory rootCategory = WorkbenchPlugin.getDefault().getNewWizardRegistry().getRootCategory();
			List<IWizardDescriptor> result = new ArrayList<>();
			collectWizards(rootCategory, result);
			IWizardDescriptor[] wizards = result.toArray(new IWizardDescriptor[result.size()]);
			for (IWizardDescriptor wizard : wizards) {
				if (!WorkbenchActivityHelper.filterItem(wizard)) {
					WizardElement wizardElement = new WizardElement(wizard);
					idToElement.put(wizardElement.getId(), wizardElement);
				}
			}
			cachedElements = idToElement.values().toArray(new QuickAccessElement[idToElement.size()]);
		}
		return cachedElements;
	}

	private void collectWizards(IWizardCategory category, List<IWizardDescriptor> result) {
		result.addAll(Arrays.asList(category.getWizards()));
		for (IWizardCategory childCategory : category.getCategories()) {
			collectWizards(childCategory, result);
		}
	}

	@Override
	public String getId() {
		return "org.eclipse.ui.wizards"; //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_OBJ_NODE);
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccess_New;
	}

	@Override
	protected void doReset() {
		cachedElements = null;
		idToElement.clear();
	}
}
