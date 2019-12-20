/*******************************************************************************
 * Copyright (C) 2015 Google Inc and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Sergey Prigogin (Google) - initial API and implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.model;

import org.fdesigner.filesystem.EFS;
import org.fdesigner.filesystem.IFileStore;
import org.fdesigner.ide.FileStoreEditorInput;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IAdapterFactory;

/**
 * The adapter factory for {@link FileStoreEditorInput}.
 * @since 3.11
 */
public class FileStoreInputAdapterFactory implements IAdapterFactory {
	private static final Class<?>[] ADAPTERS = new Class[] { IFileStore.class };

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof FileStoreEditorInput && adapterType.isAssignableFrom(IFileStore.class)) {
			FileStoreEditorInput editorInput = (FileStoreEditorInput) adaptableObject;
			try {
				return adapterType.cast(EFS.getStore(editorInput.getURI()));
			} catch (CoreException e) {
				// Ignore to return null.
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return ADAPTERS;
	}
}
