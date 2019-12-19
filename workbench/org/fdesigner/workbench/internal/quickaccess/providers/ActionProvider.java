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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fdesigner.ui.jface.action.ActionContributionItem;
import org.fdesigner.ui.jface.action.IContributionItem;
import org.fdesigner.ui.jface.action.MenuManager;
import org.fdesigner.ui.jface.action.SubContributionItem;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.WorkbenchWindow;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * @since 3.3
 *
 */
public class ActionProvider extends QuickAccessProvider {

	private Map<String, ActionElement> idToElement;

	@Override
	public String getId() {
		return "org.eclipse.ui.actions"; //$NON-NLS-1$
	}

	@Override
	public QuickAccessElement findElement(String id, String filterText) {
		getElements();
		return idToElement.get(id);
	}

	@Override
	public QuickAccessElement[] getElements() {
		if (idToElement == null) {
			idToElement = new HashMap<>();
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window instanceof WorkbenchWindow) {
				MenuManager menu = ((WorkbenchWindow) window).getMenuManager();
				Set<ActionContributionItem> result = new HashSet<>();
				collectContributions(menu, result);
				ActionContributionItem[] actions = result.toArray(new ActionContributionItem[result.size()]);
				for (ActionContributionItem action : actions) {
					ActionElement actionElement = new ActionElement(action);
					idToElement.put(actionElement.getId(), actionElement);
				}
			}
		}
		return idToElement.values().toArray(new ActionElement[idToElement.values().size()]);
	}

	private void collectContributions(MenuManager menu, Set<ActionContributionItem> result) {
		for (IContributionItem item : menu.getItems()) {
			if (item instanceof SubContributionItem) {
				item = ((SubContributionItem) item).getInnerItem();
			}
			if (item instanceof MenuManager) {
				collectContributions((MenuManager) item, result);
			} else if (item instanceof ActionContributionItem && item.isEnabled()) {
				result.add((ActionContributionItem) item);
			}
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_OBJ_NODE);
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccess_Menus;
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
