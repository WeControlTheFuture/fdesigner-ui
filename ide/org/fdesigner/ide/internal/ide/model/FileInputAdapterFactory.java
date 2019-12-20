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
 *     Andrey Loskutov <loskutov@gmx.de> - generified interface, bug 461762
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.model;

import org.fdesigner.ide.extensions.IFileEditorInput;
import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.IAdapterFactory;

/**
 * FileInputAdapterFactory is the adapter factory for the
 * IFileEditorInput.
 * @since 3.2
 *
 */

public class FileInputAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (IFile.class.equals(adapterType)) {
			return adapterType.cast(((IFileEditorInput) adaptableObject).getFile());
		}
		if (IResource.class.equals(adapterType)) {
			return adapterType.cast(((IFileEditorInput) adaptableObject).getFile());
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IFile.class, IResource.class };
	}
}
