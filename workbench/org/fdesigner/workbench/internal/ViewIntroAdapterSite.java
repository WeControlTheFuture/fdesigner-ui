/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
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
package org.fdesigner.workbench.internal;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.workbench.IActionBars;
import org.fdesigner.workbench.IKeyBindingService;
import org.fdesigner.workbench.IViewSite;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.internal.intro.IntroDescriptor;
import org.fdesigner.workbench.intro.IIntroSite;

/**
 * Simple <code>IIntroSite</code> that wraps a <code>IViewSite</code>. For use
 * in conjunction with <code>ViewIntroAdapterPart</code>.
 *
 * @since 3.0
 */
final class ViewIntroAdapterSite implements IIntroSite {
	private IntroDescriptor descriptor;

	private IViewSite viewSite;

	public ViewIntroAdapterSite(IViewSite viewSite, IntroDescriptor descriptor) {
		this.viewSite = viewSite;
		this.descriptor = descriptor;
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
	public String getId() {
		return descriptor.getId();
	}

	@Override
	public IKeyBindingService getKeyBindingService() {
		return viewSite.getKeyBindingService();
	}

	@Override
	public IWorkbenchPage getPage() {
		return viewSite.getPage();
	}

	@Override
	public String getPluginId() {
		return descriptor.getPluginId();
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewSite.getSelectionProvider();
	}

	@Override
	public <T> T getService(final Class<T> key) {
		return viewSite.getService(key);
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
	public boolean hasService(final Class<?> key) {
		return viewSite.hasService(key);
	}

	@Override
	public void setSelectionProvider(ISelectionProvider provider) {
		viewSite.setSelectionProvider(provider);
	}

	@Override
	public String toString() {
		return viewSite.toString();
	}
}
