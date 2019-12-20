/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat Inc. and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - initial API and implementation
 *     Patrik Suzzi <psuzzi@gmail.com> - Bug 485201
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.registry;

import org.fdesigner.ide.IUnassociatedEditorStrategy;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.runtime.common.runtime.OperationCanceledException;
import org.fdesigner.ui.jface.dialogs.IDialogConstants;
import org.fdesigner.workbench.IEditorDescriptor;
import org.fdesigner.workbench.IEditorRegistry;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.dialogs.EditorSelectionDialog;

/**
 * @since 3.12
 *
 */
public class AskUserViaPopupUnassociatedEditorStrategy implements IUnassociatedEditorStrategy {

	@Override
	public IEditorDescriptor getEditorDescriptor(String fileName, IEditorRegistry editorRegistry) {
		EditorSelectionDialog dialog = new EditorSelectionDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.setFileName(fileName);
		dialog.setBlockOnOpen(true);

		if (IDialogConstants.CANCEL_ID == dialog.open()) {
			throw new OperationCanceledException(IDEWorkbenchMessages.IDE_noFileEditorSelectedUserCanceled);
		}

		return dialog.getSelectedEditor();
	}

}
