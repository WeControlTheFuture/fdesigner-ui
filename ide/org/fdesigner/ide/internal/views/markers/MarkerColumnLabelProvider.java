/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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

package org.fdesigner.ide.internal.views.markers;

import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ide.views.markers.MarkerField;
import org.fdesigner.ui.jface.fieldassist.FieldDecorationRegistry;
import org.fdesigner.ui.jface.resource.LocalResourceManager;
import org.fdesigner.ui.jface.resource.ResourceManager;
import org.fdesigner.ui.jface.viewers.ColumnLabelProvider;
import org.fdesigner.ui.jface.viewers.ViewerCell;

/**
 * The MarkerColumnLabelProvider is a label provider for an individual column.
 *
 * @since 3.4
 *
 */
public class MarkerColumnLabelProvider extends ColumnLabelProvider {

	MarkerField field;
	private ResourceManager imageManager;

	/**
	 * Create a MarkerViewLabelProvider on a field.
	 *
	 * @param field
	 */
	MarkerColumnLabelProvider(MarkerField field) {
		FieldDecorationRegistry.getDefault();
		this.field = field;
		imageManager = new LocalResourceManager(IDEWorkbenchPlugin.getDefault()
				.getResourceManager());
		field.setImageManager(imageManager);
	}

	@Override
	public void dispose() {
		super.dispose();
		imageManager.dispose();
	}

	@Override
	public void update(ViewerCell cell) {
		field.update(cell);
	}
}
