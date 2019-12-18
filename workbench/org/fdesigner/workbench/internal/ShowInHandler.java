/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 440810
 ******************************************************************************/

package org.fdesigner.workbench.internal;

import java.util.Map;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.workbench.ISourceProvider;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IViewPart;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.commands.IElementUpdater;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.services.WorkbenchSourceProvider;
import org.fdesigner.workbench.menus.UIElement;
import org.fdesigner.workbench.part.IShowInTarget;
import org.fdesigner.workbench.part.ShowInContext;
import org.fdesigner.workbench.services.ISourceProviderService;
import org.fdesigner.workbench.views.IViewDescriptor;
import org.fdesigner.workbench.views.IViewRegistry;

/**
 * The show in command, which only needs a target id.
 *
 * @since 3.4
 */
public class ShowInHandler extends AbstractHandler implements IElementUpdater {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage p = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		WorkbenchPartReference r = (WorkbenchPartReference) p.getActivePartReference();
		if (p != null && r != null && r.getModel() != null) {
			((WorkbenchPage) p).updateShowInSources(r.getModel());
		}

		String targetId = event.getParameter(IWorkbenchCommandConstants.NAVIGATE_SHOW_IN_PARM_TARGET);
		if (targetId == null) {
			throw new ExecutionException("No targetId specified"); //$NON-NLS-1$
		}

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISourceProviderService sps = activeWorkbenchWindow.getService(ISourceProviderService.class);
		if (sps != null) {
			ISourceProvider sp = sps.getSourceProvider(ISources.SHOW_IN_SELECTION);
			if (sp instanceof WorkbenchSourceProvider) {
				((WorkbenchSourceProvider) sp).checkActivePart(true);
			}
		}

		ShowInContext context = getContext(HandlerUtil.getShowInSelection(event), HandlerUtil.getShowInInput(event));
		if (context == null) {
			return null;
		}

		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();

		try {
			IViewPart view = page.showView(targetId);
			IShowInTarget target = getShowInTarget(view);
			if (!(target != null && target.show(context))) {
				page.getWorkbenchWindow().getShell().getDisplay().beep();
			}
			((WorkbenchPage) page).performedShowIn(targetId); // TODO: move
			// back up
		} catch (PartInitException e) {
			throw new ExecutionException("Failed to show in", e); //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * Returns the <code>ShowInContext</code> to show in the selected target, or
	 * <code>null</code> if there is no valid context to show.
	 * <p>
	 * This implementation obtains the context from global variables provide.
	 * showInSelection and showInInput should be available.
	 * <p>
	 *
	 * @return the <code>ShowInContext</code> to show or <code>null</code>
	 */
	private ShowInContext getContext(ISelection showInSelection, Object input) {
		if (input == null && showInSelection == null) {
			return null;
		}
		return new ShowInContext(input, showInSelection);
	}

	/**
	 * Returns the <code>IShowInTarget</code> for the given part, or
	 * <code>null</code> if it does not provide one.
	 *
	 * @param targetPart the target part
	 * @return the <code>IShowInTarget</code> or <code>null</code>
	 */
	private IShowInTarget getShowInTarget(IWorkbenchPart targetPart) {
		return Adapters.adapt(targetPart, IShowInTarget.class);
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		String targetId = (String) parameters.get(IWorkbenchCommandConstants.NAVIGATE_SHOW_IN_PARM_TARGET);
		if (targetId == null || targetId.length() == 0) {
			return;
		}
		IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
		IViewDescriptor desc = reg.find(targetId);
		if (desc != null) {
			element.setIcon(desc.getImageDescriptor());
			element.setText(desc.getLabel());
		}
	}
}
