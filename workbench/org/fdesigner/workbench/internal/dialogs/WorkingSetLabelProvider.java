/*******************************************************************************
 * Copyright (c) 2005, 2014 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.dialogs;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.resource.LocalResourceManager;
import org.fdesigner.ui.jface.resource.ResourceManager;
import org.fdesigner.ui.jface.viewers.LabelProvider;
import org.fdesigner.workbench.IWorkingSet;

public class WorkingSetLabelProvider extends LabelProvider {
	private ResourceManager images;

	/**
	 * Create a new instance of the receiver.
	 */
	public WorkingSetLabelProvider() {
		images = new LocalResourceManager(JFaceResources.getResources());
	}

	@Override
	public void dispose() {
		images.dispose();

		super.dispose();
	}

	@Override
	public Image getImage(Object object) {
		Assert.isTrue(object instanceof IWorkingSet);
		IWorkingSet workingSet = (IWorkingSet) object;
		ImageDescriptor imageDescriptor = workingSet.getImageDescriptor();

		if (imageDescriptor == null) {
			return null;
		}

		return (Image) images.get(imageDescriptor);
	}

	@Override
	public String getText(Object object) {
		Assert.isTrue(object instanceof IWorkingSet);
		IWorkingSet workingSet = (IWorkingSet) object;
		return workingSet.getLabel();
	}
}
