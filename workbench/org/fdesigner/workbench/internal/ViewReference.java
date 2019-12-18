/*******************************************************************************
 * Copyright (c) 2005, 2019 IBM Corporation and others.
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
 *     Friederike Schertel <friederike@schertel.org> - Bug 478336
 *******************************************************************************/
package org.fdesigner.workbench.internal;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.ui.model.application.ui.basic.MPart;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.workbench.IMemento;
import org.fdesigner.workbench.IViewPart;
import org.fdesigner.workbench.IViewReference;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.WorkbenchException;
import org.fdesigner.workbench.XMLMemento;
import org.fdesigner.workbench.internal.registry.ViewDescriptor;

public class ViewReference extends WorkbenchPartReference implements IViewReference {

	private ViewDescriptor descriptor;
	private IMemento memento;

	public ViewReference(IEclipseContext windowContext, IWorkbenchPage page, MPart part, ViewDescriptor descriptor) {
		super(windowContext, page, part);
		this.descriptor = descriptor;

		String mementoString = getModel().getPersistedState().get(MEMENTO_KEY);
		if (mementoString != null) {
			try {
				memento = XMLMemento.createReadRoot(new StringReader(mementoString));
			} catch (WorkbenchException e) {
				WorkbenchPlugin.log(e);
			}
		}
	}

	public void persist() {
		IViewPart view = getView(false);
		if (view != null) {
			XMLMemento root = XMLMemento.createWriteRoot("view"); //$NON-NLS-1$
			view.saveState(root);
			StringWriter writer = new StringWriter();
			try {
				root.save(writer);
				getModel().getPersistedState().put(MEMENTO_KEY, writer.toString());
			} catch (IOException e) {
				WorkbenchPlugin.log(e);
			}
		}
	}

	@Override
	public String getPartName() {
		return descriptor.getLabel();
	}

	@Override
	public String getSecondaryId() {
		MPart part = getModel();

		int colonIndex = part.getElementId().indexOf(':');
		if (colonIndex == -1 || colonIndex == (part.getElementId().length() - 1))
			return null;

		return part.getElementId().substring(colonIndex + 1);
	}

	@Override
	public IViewPart getView(boolean restore) {
		return (IViewPart) getPart(restore);
	}

	@Override
	public boolean isFastView() {
		return false;
	}

	@Override
	public IWorkbenchPart createPart() throws PartInitException {
		try {
			if (descriptor == null) {
				return createErrorPart();
			}

			return descriptor.createView();
		} catch (CoreException e) {
			IStatus status = e.getStatus();
			throw new PartInitException(
					new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, status.getCode(), status.getMessage(), e));
		}
	}

	@Override
	IWorkbenchPart createErrorPart() {
		IStatus status = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH,
				NLS.bind(WorkbenchMessages.ViewFactory_initException, getModel().getElementId()), new Exception());
		return createErrorPart(status);
	}

	@Override
	public IWorkbenchPart createErrorPart(IStatus status) {
		return new ErrorViewPart(status);
	}

	@Override
	public void initialize(IWorkbenchPart part) throws PartInitException {
		ViewSite viewSite = new ViewSite(getModel(), part, this,
				descriptor == null ? null : descriptor.getConfigurationElement());
		IViewPart view = (IViewPart) part;
		view.init(viewSite, memento);

		if (view.getSite() != viewSite || view.getViewSite() != viewSite) {
			String id = descriptor == null ? getModel().getElementId() : descriptor.getId();
			throw new PartInitException(NLS.bind(WorkbenchMessages.ViewFactory_siteException, id));
		}

		legacyPart = part;
		addPropertyListeners();
	}

	@Override
	public PartSite getSite() {
		if (legacyPart != null) {
			return (PartSite) legacyPart.getSite();
		}
		return null;
	}

	public ViewDescriptor getDescriptor() {
		return descriptor;
	}
}
