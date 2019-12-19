/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.quickaccess.providers;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.IEditorReference;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * @since 3.3
 *
 */
public class EditorProvider extends QuickAccessProvider {

	private Map<String, EditorElement> idToElement;

	@Override
	public QuickAccessElement findElement(String id, String filterText) {
		getElements();
		return idToElement.get(id);
	}

	@Override
	public QuickAccessElement[] getElements() {
		if (idToElement == null) {
			idToElement = new HashMap<>();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (activePage == null) {
				return new QuickAccessElement[0];
			}
			for (IEditorReference editor : activePage.getEditorReferences()) {
				EditorElement editorElement = new EditorElement(editor);
				idToElement.put(editorElement.getId(), editorElement);
			}
		}
		return idToElement.values().toArray(new QuickAccessElement[idToElement.values().size()]);
	}

	@Override
	public String getId() {
		return "org.eclipse.ui.editors"; //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_OBJ_NODE);
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccess_Editors;
	}

	@Override
	protected void doReset() {
		idToElement = null;
	}

	@Override
	public boolean requiresUiAccess() {
		return true;
	}
}
