/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

package org.fdesigner.ide.views.bookmarkexplorer;

import java.util.ArrayList;
import java.util.Map;

import org.fdesigner.ide.extensions.part.MarkerTransfer;
import org.fdesigner.ide.undo.CreateMarkersOperation;
import org.fdesigner.ide.undo.WorkspaceUndoUtil;
import org.fdesigner.resources.IMarker;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.ui.jface.dialogs.ErrorDialog;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.ide.internal.views.bookmarkexplorer.BookmarkMessages;
/**
 * Pastes one or more bookmark(s) from the clipboard into the bookmark navigator.
 *
 * Marked for deletion, see Bug 550439
 * 
 * @noreference
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
@Deprecated
class PasteBookmarkAction extends BookmarkAction {

	private BookmarkNavigator view;

	/**
	 * The constructor.
	 *
	 * @param view the view
	 */
	public PasteBookmarkAction(BookmarkNavigator view) {
		super(view, BookmarkMessages.PasteBookmark_text);
		this.view = view;
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IBookmarkHelpContextIds.PASTE_BOOKMARK_ACTION);
		setEnabled(false);
	}

	/**
	 * Copies the marker(s) from the clipboard to the bookmark navigator view.
	 */
	@Override
	public void run() {
		// Get the markers from the clipboard
		MarkerTransfer transfer = MarkerTransfer.getInstance();
		final IMarker[] markerData = (IMarker[]) view.getClipboard()
				.getContents(transfer);

		if (markerData == null) {
			return;
		}
		final ArrayList newMarkerAttributes = new ArrayList();
		final ArrayList<IResource> newMarkerResources = new ArrayList<>();
		try {
			ResourcesPlugin.getWorkspace().run(monitor -> {
				for (IMarker markerData1 : markerData) {
					// Collect the info about the markers to be pasted.
					// Ignore any markers that aren't bookmarks.
					if (!markerData1.getType().equals(IMarker.BOOKMARK)) {
						continue;
					}
					newMarkerResources.add(markerData1.getResource());
					newMarkerAttributes.add(markerData1.getAttributes());
				}
			}, null);
		} catch (CoreException e) {
			ErrorDialog.openError(view.getShell(), BookmarkMessages.PasteBookmark_errorTitle,
					null, e.getStatus());
			return;
		}
		final Map [] attrs = (Map []) newMarkerAttributes.toArray(new Map [newMarkerAttributes.size()]);
		final IResource [] resources = newMarkerResources.toArray(new IResource [newMarkerResources.size()]);
		final CreateMarkersOperation op = new CreateMarkersOperation(IMarker.BOOKMARK, attrs,
				resources, BookmarkMessages.PasteBookmark_undoText);
		execute(op, BookmarkMessages.PasteBookmark_errorTitle, null,
				WorkspaceUndoUtil.getUIInfoAdapter(view.getShell()));

		// Need to do this in an asyncExec, even though we're in the UI thread here,
		// since the bookmark navigator updates itself with the addition in an asyncExec,
		// which hasn't been processed yet.
		// Must be done outside the create marker operation above since notification for add is
		// sent after the operation is executed.
		if (op.getMarkers() != null) {
			view.getShell().getDisplay().asyncExec(() -> {
				view.getViewer().setSelection(
						new StructuredSelection(op.getMarkers()));
				view.updatePasteEnablement();
			});
		}
	}

}
