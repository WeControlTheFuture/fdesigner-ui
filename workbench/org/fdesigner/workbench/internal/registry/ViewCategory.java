/*******************************************************************************
 * Copyright (c) 2010, 2015 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 472654
 ******************************************************************************/

package org.fdesigner.workbench.internal.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fdesigner.runtime.common.runtime.IPath;
import org.fdesigner.runtime.common.runtime.Path;
import org.fdesigner.workbench.activities.WorkbenchActivityHelper;
import org.fdesigner.workbench.views.IViewCategory;
import org.fdesigner.workbench.views.IViewDescriptor;

public class ViewCategory implements IViewCategory {

	private String id;
	private String label;
	private IPath path;
	private List<IViewDescriptor> descriptors = new ArrayList<>();

	public ViewCategory(String id, String label) {
		this.id = id;
		this.label = label;
		this.path = new Path(id);
	}

	void addDescriptor(IViewDescriptor descriptor) {
		descriptors.add(descriptor);
	}

	@Override
	public IViewDescriptor[] getViews() {
		Collection<?> allowedViews = WorkbenchActivityHelper.restrictCollection(descriptors, new ArrayList<>());
		return allowedViews.toArray(new IViewDescriptor[allowedViews.size()]);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public IPath getPath() {
		return path;
	}

}
