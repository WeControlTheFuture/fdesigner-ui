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

package org.fdesigner.workbench.internal.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.ui.jface.action.ContributionManager;
import org.fdesigner.ui.jface.action.IMenuListener2;
import org.fdesigner.ui.jface.action.IMenuManager;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.actions.QuickMenuCreator;
import org.fdesigner.workbench.menus.IMenuService;
import org.fdesigner.workbench.progress.UIJob;

/**
 * Support for a command based QuickMenuAction that can pop up a
 * menu-contribution based context menu.
 * <p>
 * This is experimental and should not be moved.
 * </p>
 *
 * @since 3.4
 */
public class QuickMenuHandler extends AbstractHandler implements IMenuListener2 {
	private QuickMenuCreator creator = new QuickMenuCreator() {

		@Override
		protected void fillMenu(IMenuManager menu) {
			if (!(menu instanceof ContributionManager)) {
				return;
			}
			IMenuService service = PlatformUI.getWorkbench().getService(IMenuService.class);
			service.populateContributionManager((ContributionManager) menu, locationURI);
			menu.addMenuListener(QuickMenuHandler.this);
		}

	};

	private String locationURI;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		locationURI = event.getParameter("org.eclipse.ui.window.quickMenu.uri"); //$NON-NLS-1$
		if (locationURI == null) {
			throw new ExecutionException("locatorURI must not be null"); //$NON-NLS-1$
		}
		creator.createMenu();
		return null;
	}

	@Override
	public void dispose() {
		if (creator != null) {
			creator.dispose();
			creator = null;
		}
	}

	@Override
	public void menuAboutToHide(final IMenuManager managerM) {
		new UIJob("quickMenuCleanup") { //$NON-NLS-1$

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				IMenuService service = PlatformUI.getWorkbench().getService(IMenuService.class);
				service.releaseContributions((ContributionManager) managerM);
				return Status.OK_STATUS;
			}

		}.schedule();
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		// no-op
	}
}
