/*******************************************************************************
 * Copyright (c) 2015 Red Hat Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - extracted from IDE.getEditorDescription
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.registry;

import org.fdesigner.ide.IUnassociatedEditorStrategy;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.workbench.IEditorDescriptor;
import org.fdesigner.workbench.IEditorRegistry;

/**
 * @since 3.12
 *
 */
public final class TextEditorStrategy implements IUnassociatedEditorStrategy {

	@Override
	public IEditorDescriptor getEditorDescriptor(String name, IEditorRegistry editorReg) {
		return editorReg.findEditor(IDEWorkbenchPlugin.DEFAULT_TEXT_EDITOR_ID);
	}
}