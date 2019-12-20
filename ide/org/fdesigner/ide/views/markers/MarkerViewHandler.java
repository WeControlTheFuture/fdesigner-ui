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
 ******************************************************************************/

package org.fdesigner.ide.views.markers;

import org.eclipse.swt.widgets.Display;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.commands.operations.IUndoableOperation;
import org.fdesigner.ide.internal.ide.StatusUtil;
import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * MarkerViewHandler is the abstract class of the handlers for the
 * {@link MarkerSupportView}
 *
 * @since 3.4
 *
 */
public abstract class MarkerViewHandler extends AbstractHandler {

	private static final IMarker[] EMPTY_MARKER_ARRAY = new IMarker[0];

	/**
	 * Get the view this event occurred on.
	 *
	 * @param event
	 * @return {@link MarkerSupportView} or <code>null</code>
	 */
	public MarkerSupportView getView(ExecutionEvent event) {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (!(part instanceof MarkerSupportView))
			return null;
		return (MarkerSupportView) part;
	}

	/**
	 * Execute the specified undoable operation
	 *
	 * @param operation
	 * @param title
	 * @param monitor
	 * @param uiInfo
	 */
	public void execute(IUndoableOperation operation, String title,
			IProgressMonitor monitor, IAdaptable uiInfo) {
		try {
			PlatformUI.getWorkbench().getOperationSupport()
					.getOperationHistory().execute(operation, monitor, uiInfo);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof CoreException) {
				StatusManager.getManager().handle(
						StatusUtil
								.newStatus(IStatus.ERROR, title, e.getCause()),
						StatusManager.SHOW);

			} else {
				StatusManager.getManager().handle(StatusUtil.newError(e));
			}
		}
	}

	/**
	 * Get the selected markers for the receiver in the view from event. If the
	 * view cannot be found then return an empty array.
	 *
	 * This is run using {@link Display#syncExec(Runnable)} so that it can be called
	 * outside of the UI {@link Thread}.
	 *
	 * @param event
	 * @return {@link IMarker}[]
	 */
	public IMarker[] getSelectedMarkers(ExecutionEvent event) {
		final MarkerSupportView view = getView(event);
		if (view == null)
			return EMPTY_MARKER_ARRAY;

		final IMarker[][] result = new IMarker[1][];
		view.getSite().getShell().getDisplay().syncExec(() -> result[0] = view.getSelectedMarkers());
		return result[0];
	}
}
