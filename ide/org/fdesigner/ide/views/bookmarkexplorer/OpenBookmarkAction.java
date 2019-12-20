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

import java.util.Iterator;

import org.fdesigner.ide.IDE;
import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.ui.jface.dialogs.ErrorDialog;
import org.fdesigner.ui.jface.dialogs.MessageDialog;
import org.fdesigner.ui.jface.util.OpenStrategy;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.ide.internal.views.bookmarkexplorer.BookmarkMessages;
/**
 * Action to open an editor on the selected bookmarks.
 *
 * Marked for deletion, see Bug 550439
 * 
 * @noreference
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
@Deprecated
class OpenBookmarkAction extends BookmarkAction {

	/**
	 * Create a new instance of this class.
	 *
	 * @param view the view
	 */
	public OpenBookmarkAction(BookmarkNavigator view) {
		super(view, BookmarkMessages.OpenBookmark_text);
		setToolTipText(BookmarkMessages.OpenBookmark_toolTip);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IBookmarkHelpContextIds.OPEN_BOOKMARK_ACTION);
		setEnabled(false);
	}

	@Override
	public void run() {
		IWorkbenchPage page = getView().getSite().getPage();
		for (Iterator i = getStructuredSelection().iterator(); i.hasNext();) {
			IMarker marker = (IMarker) i.next();
			try {
				IDE.openEditor(page, marker, OpenStrategy.activateOnOpen());
			} catch (PartInitException e) {
				// Open an error style dialog for PartInitException by
				// including any extra information from the nested
				// CoreException if present.

				// Check for a nested CoreException
				CoreException nestedException = null;
				IStatus status = e.getStatus();
				if (status != null
						&& status.getException() instanceof CoreException) {
					nestedException = (CoreException) status.getException();
				}

				if (nestedException != null) {
					// Open an error dialog and include the extra
					// status information from the nested CoreException
					ErrorDialog.openError(getView().getShell(),
							BookmarkMessages.OpenBookmark_errorTitle,
							e.getMessage(), nestedException.getStatus());
				} else {
					// Open a regular error dialog since there is no
					// extra information to display
					MessageDialog.openError(getView().getShell(),
							BookmarkMessages.OpenBookmark_errorTitle,
							e.getMessage());
				}
			}
		}
	}

	@Override
	public void selectionChanged(IStructuredSelection sel) {
		setEnabled(!sel.isEmpty());
	}
}
