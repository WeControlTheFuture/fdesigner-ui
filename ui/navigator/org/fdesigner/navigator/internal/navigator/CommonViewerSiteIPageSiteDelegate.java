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
 ******************************************************************************/

package org.fdesigner.navigator.internal.navigator;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.navigator.ICommonViewerSite;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.workbench.part.IPageSite;

/**
 * Provides a delegate implementation of {@link ICommonViewerSite}.
 *
 * @since 3.2
 *
 */
public class CommonViewerSiteIPageSiteDelegate implements ICommonViewerSite {

	private IPageSite pageSite;

	private String viewerId;

	/**
	 *
	 * @param aViewerId
	 * @param aPageSite
	 */
	public CommonViewerSiteIPageSiteDelegate(String aViewerId,
			IPageSite aPageSite) {
		viewerId = aViewerId;
		pageSite = aPageSite;
	}

	@Override
	public String getId() {
		return viewerId;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return Adapters.adapt(pageSite, adapter);
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return pageSite.getSelectionProvider();
	}

	@Override
	public void setSelectionProvider(ISelectionProvider aSelectionProvider) {
		pageSite.setSelectionProvider(aSelectionProvider);
	}

	@Override
	public Shell getShell() {
		return pageSite.getShell();
	}

}
