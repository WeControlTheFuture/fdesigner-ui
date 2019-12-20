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
package org.fdesigner.ide.internal.ide.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.fdesigner.filesystem.EFS;
import org.fdesigner.filesystem.IFileInfo;
import org.fdesigner.filesystem.IFileStore;
import org.fdesigner.ide.IDE;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.runtime.common.runtime.Path;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.action.IAction;
import org.fdesigner.ui.jface.dialogs.MessageDialog;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.IWorkbenchWindowActionDelegate;
import org.fdesigner.workbench.PartInitException;


/**
 * Standard action for opening an editor on local file(s).
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class OpenLocalFileAction extends Action implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private String filterPath;

	/**
	 * Creates a new action for opening a local file.
	 */
	public OpenLocalFileAction() {
		setEnabled(true);
	}

	@Override
	public void dispose() {
		window =  null;
		filterPath =  null;
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window =  window;
		filterPath =  System.getProperty("user.home"); //$NON-NLS-1$
	}

	@Override
	public void run(IAction action) {
		run();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void run() {
		FileDialog dialog =  new FileDialog(window.getShell(), SWT.OPEN | SWT.MULTI | SWT.SHEET);
		dialog.setText(IDEWorkbenchMessages.OpenLocalFileAction_title);
		dialog.setFilterPath(filterPath);
		dialog.open();
		String[] names =  dialog.getFileNames();

		if (names != null) {
			filterPath =  dialog.getFilterPath();

			int numberOfFilesNotFound =  0;
			StringBuilder notFound =  new StringBuilder();
			for (String name : names) {
				IFileStore fileStore =  EFS.getLocalFileSystem().getStore(new Path(filterPath));
				fileStore =  fileStore.getChild(name);
				IFileInfo fetchInfo = fileStore.fetchInfo();
				if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
					IWorkbenchPage page =  window.getActivePage();
					try {
						IDE.openEditorOnFileStore(page, fileStore);
					} catch (PartInitException e) {
						String msg =  NLS.bind(IDEWorkbenchMessages.OpenLocalFileAction_message_errorOnOpen, fileStore.getName());
						IDEWorkbenchPlugin.log(msg,e.getStatus());
						MessageDialog.open(MessageDialog.ERROR,window.getShell(), IDEWorkbenchMessages.OpenLocalFileAction_title, msg, SWT.SHEET);
					}
				} else {
					if (++numberOfFilesNotFound > 1)
						notFound.append('\n');
					notFound.append(fileStore.getName());
				}
			}

			if (numberOfFilesNotFound > 0) {
				String msgFmt =  numberOfFilesNotFound == 1 ? IDEWorkbenchMessages.OpenLocalFileAction_message_fileNotFound : IDEWorkbenchMessages.OpenLocalFileAction_message_filesNotFound;
				String msg =  NLS.bind(msgFmt, notFound.toString());
				MessageDialog.open(MessageDialog.ERROR, window.getShell(), IDEWorkbenchMessages.OpenLocalFileAction_title, msg, SWT.SHEET);
			}
		}
	}
}
