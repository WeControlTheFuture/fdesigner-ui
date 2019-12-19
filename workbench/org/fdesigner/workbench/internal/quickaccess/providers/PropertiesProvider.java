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
 *******************************************************************************/

package org.fdesigner.workbench.internal.quickaccess.providers;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.ui.jface.preference.IPreferenceNode;
import org.fdesigner.ui.jface.preference.PreferenceManager;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.dialogs.PropertyPageContributorManager;
import org.fdesigner.workbench.internal.dialogs.PropertyPageManager;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * @since 3.3
 *
 */
public class PropertiesProvider extends QuickAccessProvider {

	private Map<String, PropertiesElement> idToElement;

	@Override
	public QuickAccessElement findElement(String id, String filterText) {
		getElements();
		return idToElement.get(id);
	}

	@Override
	public QuickAccessElement[] getElements() {
		if (idToElement == null) {
			idToElement = new HashMap<>();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (activePage != null) {
				PropertyPageManager pageManager = new PropertyPageManager();
				ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
					Object element = ((IStructuredSelection) selection).getFirstElement();
					PropertyPageContributorManager.getManager().contribute(pageManager, element);
					for (IPreferenceNode property : pageManager.getElements(PreferenceManager.PRE_ORDER)) {
						PropertiesElement propertiesElement = new PropertiesElement(element, property);
						idToElement.put(propertiesElement.getId(), propertiesElement);
					}
				}
			}
		}
		return idToElement.values().toArray(new QuickAccessElement[idToElement.values().size()]);
	}

	@Override
	public String getId() {
		return "org.eclipse.ui.properties"; //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_OBJ_NODE);
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccess_Properties;
	}

	@Override
	protected void doReset() {
		idToElement = null;
	}

	@Override
	public boolean requiresUiAccess() {
		return true;
	}
}
