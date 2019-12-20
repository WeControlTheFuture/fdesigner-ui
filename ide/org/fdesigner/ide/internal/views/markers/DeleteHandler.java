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
import org.fdesigner.commands.operations.IUndoableOperation;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.undo.DeleteMarkersOperation;
import org.fdesigner.ide.undo.WorkspaceUndoUtil;
import org.fdesigner.ide.views.markers.MarkerSupportView;
import org.fdesigner.ide.views.markers.MarkerViewHandler;
import org.fdesigner.ide.views.markers.internal.MarkerMessages;
import org.fdesigner.resources.IMarker;
import org.fdesigner.resources.WorkspaceJob;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.ui.jface.dialogs.IDialogConstants;
import org.fdesigner.ui.jface.dialogs.MessageDialog;

/**
 * DeleteHandler is the handler for the deletion of a marker.
 *
 * @since 3.4
 *
 */
public class DeleteHandler extends MarkerViewHandler {

	@Override
	public Object execute(ExecutionEvent event) {

		final MarkerSupportView view = getView(event);
		if (view == null)
			return this;

		final IMarker[] selected = getSelectedMarkers(event);

		// Verify.
		MessageDialog dialog = new MessageDialog(
				view.getSite().getShell(),
				MarkerMessages.deleteActionConfirmTitle,
				null, // icon
				MarkerMessages.deleteActionConfirmMessage,
				MessageDialog.WARNING,
				0,
				MarkerMessages.deleteActionConfirm_buttonDeleteLabel, IDialogConstants.CANCEL_LABEL);

		if (dialog.open() != IDialogConstants.OK_ID) {
			return view;
		}

		WorkspaceJob deleteJob= new WorkspaceJob(IDEWorkbenchMessages.MarkerDeleteHandler_JobTitle) { //See Bug#250807
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				monitor.beginTask(IDEWorkbenchMessages.MarkerDeleteHandler_JobMessageLabel, 10 * selected.length);
				try {
					IUndoableOperation op= new DeleteMarkersOperation(selected, view.getDeleteOperationName(selected));
					op.addContext(view.getUndoContext());
					execute(op, MarkerMessages.deleteMarkers_errorMessage, monitor, WorkspaceUndoUtil.getUIInfoAdapter(view.getSite().getShell()));
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		deleteJob.setUser(true);
		deleteJob.schedule();

		return this;
	}
}
