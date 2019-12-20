/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.fdesigner.ide.IDE;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.dialogs.SearchPattern;
import org.fdesigner.workbench.model.WorkbenchLabelProvider;
import org.fdesigner.workbench.quickaccess.IQuickAccessComputer;
import org.fdesigner.workbench.quickaccess.IQuickAccessComputerExtension;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * @since 3.16.100
 */
public class OpenResourceQuickAccessComputer implements IQuickAccessComputer, IQuickAccessComputerExtension {

	private static final long TIMEOUT_MS = 200;

	@Override
	public QuickAccessElement[] computeElements(String query, IProgressMonitor monitor) {
		SearchPattern searchPattern = new SearchPattern();
		searchPattern.setPattern(query);

		List<QuickAccessElement> res = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		WorkbenchLabelProvider labelProvider = new WorkbenchLabelProvider();
		try {
			ResourcesPlugin.getWorkspace().getRoot().accept(resourceProxy -> {
				if (resourceProxy.isDerived() || !resourceProxy.isAccessible()) {
					return false;
				}

				if (resourceProxy.getType() == IResource.FILE) {
					String name = resourceProxy.getName();
					if (searchPattern.matches(name)) {
						IFile file = (IFile) resourceProxy.requestResource();
						res.add(new ResourceElement(labelProvider, file));
					}
				}
				return !monitor.isCanceled() && System.currentTimeMillis() - startTime < TIMEOUT_MS;
			}, IResource.NONE);
		} catch (CoreException e) {
			IDEWorkbenchPlugin.log(e.getMessage(), e);
		}
		labelProvider.dispose();
		return res.toArray(new QuickAccessElement[res.size()]);
	}

	@Override
	public QuickAccessElement[] computeElements() {
		return new QuickAccessElement[0];
	}

	@Override
	public void resetState() {
		// stateless, nothing to do
	}

	@Override
	public boolean needsRefresh() {
		return false;
	}

	private static class ResourceElement extends QuickAccessElement {
		private final WorkbenchLabelProvider fLabelProvider;
		private final IFile fFile;

		private ResourceElement(WorkbenchLabelProvider labelProvider, IFile resource) {
			fLabelProvider = labelProvider;
			fFile = resource;
		}

		@Override
		public String getLabel() {
			return fLabelProvider.getText(fFile);
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return ImageDescriptor.createFromImageDataProvider(zoom -> fLabelProvider.getImage(fFile).getImageData());
		}

		@Override
		public String getId() {
			return fFile.getFullPath().toString();
		}

		@Override
		public void execute() {
			try {
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), fFile);
			} catch (PartInitException e) {
				IDEWorkbenchPlugin.log(e.getMessage(), e);
			}
		}
	}
}
