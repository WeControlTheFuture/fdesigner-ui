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
 *******************************************************************************/
package org.fdesigner.workbench.model;

import java.util.HashMap;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.ui.jface.resource.DeviceResourceException;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.resource.LocalResourceManager;
import org.fdesigner.ui.jface.resource.ResourceManager;
import org.fdesigner.ui.jface.viewers.ITableLabelProvider;
import org.fdesigner.ui.jface.viewers.LabelProvider;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.Saveable;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * A table label provider implementation for showing workbench views and editors
 * (objects of type <code>IWorkbenchPart</code>) in tree- and table-structured
 * viewers.
 * <p>
 * Clients may instantiate this class. It is not intended to be subclassed.
 * </p>
 *
 * @since 3.0
 */
public final class WorkbenchPartLabelProvider extends LabelProvider implements ITableLabelProvider {

	private ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
	private HashMap images = new HashMap();

	/**
	 * Creates a new label provider for workbench parts.
	 */
	public WorkbenchPartLabelProvider() {
		super();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IWorkbenchPart) {
			return ((IWorkbenchPart) element).getTitleImage();
		}
		if (element instanceof Saveable) {
			Saveable model = (Saveable) element;
			ImageDescriptor imageDesc = model.getImageDescriptor();
			// convert from ImageDescriptor to Image
			if (imageDesc == null) {
				return null;
			}
			Image image = (Image) images.get(imageDesc);
			if (image == null) {
				try {
					image = resourceManager.createImage(imageDesc);
					images.put(imageDesc, image);
				} catch (DeviceResourceException e) {
					WorkbenchPlugin.log(getClass(), "getImage", e); //$NON-NLS-1$
				}
			}
			return image;
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IWorkbenchPart) {
			IWorkbenchPart part = (IWorkbenchPart) element;
			String path = part.getTitleToolTip();
			if (path == null || path.trim().length() == 0) {
				return part.getTitle();
			}
			return part.getTitle() + "  [" + path + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (element instanceof Saveable) {
			Saveable model = (Saveable) element;
			String path = model.getToolTipText();
			if (path == null || path.trim().length() == 0) {
				return model.getName();
			}
			return model.getName() + "  [" + path + "]"; //$NON-NLS-1$ //$NON-NLS-2$

		}
		return null;
	}

	/**
	 * @see ITableLabelProvider#getColumnImage
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return getImage(element);
	}

	/**
	 * @see ITableLabelProvider#getColumnText
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		return getText(element);
	}

	@Override
	public void dispose() {
		resourceManager.dispose();
		super.dispose();
	}
}
