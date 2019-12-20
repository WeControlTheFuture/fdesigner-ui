/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
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
package org.fdesigner.navigator.internal.navigator;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.navigator.ICommonViewerWorkbenchSite;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.action.MenuManager;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.workbench.IActionBars;
import org.fdesigner.workbench.IViewSite;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchPartSite;
import org.fdesigner.workbench.IWorkbenchWindow;

/**
 * Provides a delegate implementation of {@link ICommonViewerWorkbenchSite}.
 *
 * @since 3.2
 *
 */
public class CommonViewerSiteIViewSiteDelegate implements ICommonViewerWorkbenchSite {

	private IViewSite viewSite;

	/**
	 *
	 * @param aViewSite
	 */
	public CommonViewerSiteIViewSiteDelegate(IViewSite aViewSite) {
		viewSite = aViewSite;
	}

	@Override
	public String getId() {
		return viewSite.getId();
	}

	@Override
	public IActionBars getActionBars() {
		return viewSite.getActionBars();
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return Adapters.adapt(viewSite, adapter);
	}

	@Override
	public IWorkbenchPage getPage() {
		return viewSite.getPage();
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewSite.getSelectionProvider();
	}

	@Override
	public void setSelectionProvider(ISelectionProvider aSelectionProvider) {
		viewSite.setSelectionProvider(aSelectionProvider);
	}

	@Override
	public Shell getShell() {
		return viewSite.getShell();
	}

	@Override
	public IWorkbenchWindow getWorkbenchWindow() {
		return viewSite.getWorkbenchWindow();
	}

	@Override
	public void registerContextMenu(String menuId, MenuManager menuManager,
			ISelectionProvider selectionProvider) {
		viewSite.registerContextMenu(menuId, menuManager, selectionProvider);
	}

	@Override
	public IWorkbenchPart getPart() {
		return viewSite.getPart();
	}

	@Override
	public IWorkbenchPartSite getSite() {
		return viewSite;
	}

}
