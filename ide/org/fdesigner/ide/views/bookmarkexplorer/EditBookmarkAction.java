/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IMarker;
import org.fdesigner.resources.IResource;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.ide.internal.views.bookmarkexplorer.BookmarkMessages;
/**
 * Opens a properties dialog allowing the user to edit the bookmark's description.
 *
 * Marked for deletion, see Bug 550439
 *
 * @noreference
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
@Deprecated
class EditBookmarkAction extends BookmarkAction {

	protected EditBookmarkAction(BookmarkNavigator view) {
		super(view, BookmarkMessages.Properties_text);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IBookmarkHelpContextIds.BOOKMARK_PROPERTIES_ACTION);
		setEnabled(false);
	}

	private IMarker marker;

	@Override
	public void run() {
		if (marker != null) {
			editBookmark();
		}
	}

	/**
	 * Sets marker to the current selection if the selection is an instance of
	 * <code>org.eclipse.core.resources.IMarker</code> and the selected marker's
	 * resource is an instance of <code>org.eclipse.core.resources.IFile</code>.
	 * Otherwise sets marker to null.
	 */
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		marker = null;
		setEnabled(false);

		if (selection.size() != 1) {
			return;
		}

		Object o = selection.getFirstElement();
		if (!(o instanceof IMarker)) {
			return;
		}

		IMarker selectedMarker = (IMarker) o;
		IResource resource = selectedMarker.getResource();
		if (resource instanceof IFile) {
			marker = selectedMarker;
			setEnabled(true);
		}
	}

	private void editBookmark() {
		BookmarkPropertiesDialog dialog = new BookmarkPropertiesDialog(
				getView().getSite().getShell());
		dialog.setMarker(marker);
		dialog.open();
	}
}
