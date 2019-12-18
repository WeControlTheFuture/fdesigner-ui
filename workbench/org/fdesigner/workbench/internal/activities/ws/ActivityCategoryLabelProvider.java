/*******************************************************************************
 * Copyright (c) 2003, 2018 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.activities.ws;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.ui.jface.resource.DeviceResourceException;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.resource.LocalResourceManager;
import org.fdesigner.ui.jface.viewers.LabelProvider;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.activities.IActivity;
import org.fdesigner.workbench.activities.ICategory;
import org.fdesigner.workbench.activities.NotDefinedException;

/**
 * Provides labels for elements drawn from <code>IActivityManagers</code>. Ie:
 * <code>IActivity</code> and <code>ICategory</code> objects.
 *
 * @since 3.0
 */
public class ActivityCategoryLabelProvider extends LabelProvider {

	private LocalResourceManager manager;
	private Map<Object, ImageDescriptor> descriptorMap = new HashMap<>();

	/**
	 * Create a new instance of this class.
	 */
	public ActivityCategoryLabelProvider() {
		manager = new LocalResourceManager(JFaceResources.getResources());
	}

	@Override
	public Image getImage(Object element) {
		try {
			ImageDescriptor descriptor = getDescriptor(element);
			if (descriptor != null) {
				return manager.createImage(descriptor);
			}
		} catch (DeviceResourceException e) {
			// ignore
		}
		return null;
	}

	private ImageDescriptor getDescriptor(Object element) {
		ImageDescriptor descriptor = descriptorMap.get(element);
		if (descriptor != null) {
			return descriptor;
		}

		if (element instanceof ICategory) {
			ICategory category = (ICategory) element;
			descriptor = PlatformUI.getWorkbench().getActivitySupport().getImageDescriptor(category);
			if (descriptor != null) {
				descriptorMap.put(element, descriptor);
			}
		} else if (element instanceof IActivity) {
			IActivity activity = (IActivity) element;
			descriptor = PlatformUI.getWorkbench().getActivitySupport().getImageDescriptor(activity);
			if (descriptor != null) {
				descriptorMap.put(element, descriptor);
			}
		}
		return descriptor;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IActivity) {
			IActivity activity = (IActivity) element;
			try {
				return activity.getName();
			} catch (NotDefinedException e) {
				return activity.getId();
			}
		} else if (element instanceof ICategory) {
			ICategory category = ((ICategory) element);
			try {
				return category.getName();
			} catch (NotDefinedException e) {
				return category.getId();
			}
		}
		return super.getText(element);
	}

	@Override
	public void dispose() {
		manager.dispose();
		descriptorMap.clear();
	}
}
