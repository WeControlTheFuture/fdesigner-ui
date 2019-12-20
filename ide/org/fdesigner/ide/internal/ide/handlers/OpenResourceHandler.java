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

package org.fdesigner.ide.internal.ide.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.commands.IHandler;
import org.fdesigner.commands.IHandlerListener;
import org.fdesigner.ide.IDE;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.ide.internal.ide.dialogs.OpenResourceDialog;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.runtime.common.runtime.ListenerList;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.action.IAction;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.IWorkbenchWindowActionDelegate;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.PlatformUI;

/**
 * Implements the open resource action. Opens a dialog prompting for a file and
 * opens the selected file in an editor.
 *
 * @since 2.1
 */
public final class OpenResourceHandler extends Action implements IHandler,
		IWorkbenchWindowActionDelegate {

	/**
	 * The identifier of the parameter storing the file path.
	 */
	private static final String PARAM_ID_FILE_PATH = "filePath"; //$NON-NLS-1$

	/**
	 * A collection of objects listening to changes to this manager. This
	 * collection is <code>null</code> if there are no listeners.
	 */
	private transient ListenerList<IHandlerListener> listenerList = null;

	/**
	 * Creates a new instance of the class.
	 */
	public OpenResourceHandler() {
		super();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IIDEHelpContextIds.OPEN_WORKSPACE_FILE_ACTION);
	}

	@Override
	public final void addHandlerListener(final IHandlerListener listener) {
		if (listenerList == null) {
			listenerList = new ListenerList<>(ListenerList.IDENTITY);
		}

		listenerList.add(listener);
	}

	@Override
	public final void dispose() {
		listenerList = null;
	}

	@Override
	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		final List<IFile> files = new ArrayList<>();

		if (event.getParameter(PARAM_ID_FILE_PATH) == null) {
			// Prompt the user for the resource to open.
			Object[] result = queryFileResource();

			if (result != null) {
				for (Object fileResource : result) {
					if (fileResource instanceof IFile) {
						files.add((IFile) fileResource);
					}
				}
			}

		} else {
			// Use the given parameter.
			final IResource resource = (IResource) event
					.getObjectParameterForExecution(PARAM_ID_FILE_PATH);
			if (!(resource instanceof IFile)) {
				throw new ExecutionException(
						"filePath parameter must identify a file"); //$NON-NLS-1$
			}
			files.add((IFile) resource);
		}

		if (files.size() > 0) {

			final IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window == null) {
				throw new ExecutionException("no active workbench window"); //$NON-NLS-1$
			}

			final IWorkbenchPage page = window.getActivePage();
			if (page == null) {
				throw new ExecutionException("no active workbench page"); //$NON-NLS-1$
			}

			try {
				for (Iterator<IFile> it = files.iterator(); it.hasNext();) {
					IDE.openEditor(page, it.next(), true);
				}
			} catch (final PartInitException e) {
				throw new ExecutionException("error opening file in editor", e); //$NON-NLS-1$
			}
		}

		return null;
	}

	@Override
	public final void init(final IWorkbenchWindow window) {
		// Do nothing.
	}

	/**
	 * Query the user for the resources that should be opened
	 *
	 * @return the resource that should be opened.
	 */
	private final Object[] queryFileResource() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		final Shell parent = window.getShell();
		final IContainer input = ResourcesPlugin.getWorkspace().getRoot();

		final OpenResourceDialog dialog = new OpenResourceDialog(parent, input,
				IResource.FILE);
		final int resultCode = dialog.open();
		if (resultCode != Window.OK) {
			return null;
		}

		return dialog.getResult();
	}

	@Override
	public final void removeHandlerListener(final IHandlerListener listener) {
		if (listenerList != null) {
			listenerList.remove(listener);

			if (listenerList.isEmpty()) {
				listenerList = null;
			}
		}
	}

	@Override
	public final void run(final IAction action) {
		try {
			execute(new ExecutionEvent());
		} catch (final ExecutionException e) {
			// TODO Do something meaningful and poignant.
		}
	}

	@Override
	public final void selectionChanged(final IAction action,
			final ISelection selection) {
		// Do nothing.
	}
}
