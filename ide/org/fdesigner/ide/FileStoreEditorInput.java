/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
package org.fdesigner.ide;

import java.net.URI;

import org.fdesigner.filesystem.IFileStore;
import org.fdesigner.ide.extensions.IURIEditorInput;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.IMemento;
import org.fdesigner.workbench.IPersistableElement;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.model.IWorkbenchAdapter;

/**
 * Implements an IEditorInput instance appropriate for
 * <code>IFileStore</code> elements that represent files
 * that are not part of the current workspace.
 *
 * @since 3.3
 */
public class FileStoreEditorInput implements IURIEditorInput, IPersistableElement {

	/**
	 * The workbench adapter which simply provides the label.
	 *
	 * @since 3.3
	 */
	private static class WorkbenchAdapter implements IWorkbenchAdapter {
		@Override
		public Object[] getChildren(Object o) {
			return null;
		}

		@Override
		public ImageDescriptor getImageDescriptor(Object object) {
			return null;
		}

		@Override
		public String getLabel(Object o) {
			return ((FileStoreEditorInput) o).getName();
		}

		@Override
		public Object getParent(Object o) {
			return null;
		}
	}

	private IFileStore fileStore;
	private WorkbenchAdapter workbenchAdapter = new WorkbenchAdapter();

	/**
	 * @param fileStore
	 */
	public FileStoreEditorInput(IFileStore fileStore) {
		Assert.isNotNull(fileStore);
		this.fileStore = fileStore;
		workbenchAdapter = new WorkbenchAdapter();
	}

	@Override
	public boolean exists() {
		return fileStore.fetchInfo().exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(getName());
	}

	@Override
	public String getName() {
		return fileStore.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return fileStore.toString();
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return adapter.cast(workbenchAdapter);
		}
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o instanceof FileStoreEditorInput) {
			FileStoreEditorInput input = (FileStoreEditorInput) o;
			return fileStore.equals(input.fileStore);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return fileStore.hashCode();
	}

	@Override
	public URI getURI() {
		return fileStore.toURI();
	}

	@Override
	public String getFactoryId() {
		return FileStoreEditorInputFactory.ID;
	}

	@Override
	public void saveState(IMemento memento) {
		FileStoreEditorInputFactory.saveState(memento, this);

	}

}
