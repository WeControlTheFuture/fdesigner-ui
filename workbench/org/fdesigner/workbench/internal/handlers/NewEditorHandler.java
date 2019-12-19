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

package org.fdesigner.workbench.internal.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.IPersistableEditor;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.XMLMemento;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.IWorkbenchConstants;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPage;
import org.fdesigner.workbench.internal.dialogs.DialogUtil;

/**
 * Open a new editor on the active editor's input.
 *
 * @since 3.4
 *
 */
public class NewEditorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		if (page == null) {
			return null;
		}
		IEditorPart editor = page.getActiveEditor();
		if (editor == null) {
			return null;
		}
		String editorId = editor.getSite().getId();
		if (editorId == null) {
			return null;
		}
		try {
			if (editor instanceof IPersistableEditor) {
				XMLMemento editorState = XMLMemento.createWriteRoot(IWorkbenchConstants.TAG_EDITOR_STATE);
				((IPersistableEditor) editor).saveState(editorState);
				((WorkbenchPage) page).openEditor(editor.getEditorInput(), editorId, true, IWorkbenchPage.MATCH_NONE,
						editorState, true);
			} else {
				page.openEditor(editor.getEditorInput(), editorId, true, IWorkbenchPage.MATCH_NONE);
			}
		} catch (PartInitException e) {
			DialogUtil.openError(activeWorkbenchWindow.getShell(), WorkbenchMessages.Error, e.getMessage(), e);
		}
		return null;
	}

}
