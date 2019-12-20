/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
 ******************************************************************************/
package org.fdesigner.ide.internal.views.markers;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.commands.operations.IUndoContext;
import org.fdesigner.ide.internal.ide.IDEInternalWorkbenchImages;
import org.fdesigner.ide.undo.WorkspaceUndoUtil;
import org.fdesigner.ide.views.markers.MarkerSupportView;
import org.fdesigner.ide.views.markers.internal.MarkerMessages;
import org.fdesigner.ide.views.markers.internal.MarkerSupportRegistry;
import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.workbench.internal.WorkbenchPlugin;


/**
 * The Problems view is supplied by the IDE to show problems.
 *
 * @since 3.4
 */
public class ProblemsView extends MarkerSupportView {

	/**
	 * Create a new instance of the receiver.
	 */
	public ProblemsView() {
		super(MarkerSupportRegistry.PROBLEMS_GENERATOR);

	}

	@Override
	void updateTitleImage(Integer[] counts) {
		Image image= WorkbenchPlugin.getDefault().getSharedImages().getImage(IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW);
		if (counts[0].intValue() > 0) {
			image= WorkbenchPlugin.getDefault().getSharedImages().getImage(IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_ERROR);
		} else if (counts[1].intValue() > 0) {
			image= WorkbenchPlugin.getDefault().getSharedImages().getImage(IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_WARNING);
		} else if (counts[2].intValue() > 0) {
			image= WorkbenchPlugin.getDefault().getSharedImages().getImage(IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_INFO);
		}
		setTitleImage(image);
	}

	@Override
	protected IUndoContext getUndoContext() {
		return WorkspaceUndoUtil.getProblemsUndoContext();
	}

	@Override
	protected String getDeleteOperationName(IMarker[] markers) {
		Assert.isLegal(markers.length > 0);
		return markers.length == 1 ? MarkerMessages.deleteProblemMarker_operationName : MarkerMessages.deleteProblemMarkers_operationName;
	}

}
