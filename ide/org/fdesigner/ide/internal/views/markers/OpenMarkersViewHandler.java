/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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

package org.fdesigner.ide.internal.views.markers;

import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.ide.views.markers.MarkerViewHandler;
import org.fdesigner.ide.views.markers.internal.MarkerMessages;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.dialogs.IInputValidator;
import org.fdesigner.ui.jface.dialogs.InputDialog;
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.workbench.IViewPart;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.PartInitException;

/**
 * OpenMarkersViewHandler is used to open another markers view.
 *
 * @since 3.4
 *
 */
public class OpenMarkersViewHandler extends MarkerViewHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ExtendedMarkersView part = getView(event);
		if (part == null)
			return null;
		try {
			String count = ExtendedMarkersView.newSecondaryID(part);
				String defaultName = NLS.bind(MarkerMessages.newViewTitle,
					new Object[] { part.getSite().getRegisteredName(), count });
			InputDialog dialog = new InputDialog(part.getSite().getShell(),
					NLS
							.bind(MarkerMessages.NewViewHandler_dialogTitle,
									new String[] { part.getSite()
											.getRegisteredName() }),
					MarkerMessages.NewViewHandler_dialogMessage, defaultName,
					getValidator());

			if (dialog.open() != Window.OK)
				return this;

			IViewPart newPart = part.getSite().getPage()
					.showView(part.getSite().getId(), count,
							IWorkbenchPage.VIEW_ACTIVATE);
			if (newPart instanceof ExtendedMarkersView) {
				((ExtendedMarkersView) newPart).initializeTitle(dialog
						.getValue());
			}
		} catch (PartInitException e) {
			throw new ExecutionException(e.getLocalizedMessage(), e);
		}
		return this;

	}

	/**
	 * Get the input validator for the receiver.
	 *
	 * @return IInputValidator
	 */
	private IInputValidator getValidator() {
		return newText -> {
			if (newText.length() > 0)
				return null;
			return MarkerMessages.MarkerFilterDialog_emptyMessage;
		};
	}
}
