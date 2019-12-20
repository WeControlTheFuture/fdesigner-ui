/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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

package org.fdesigner.navigator.internal.navigator.filters;

import java.util.Arrays;

import org.fdesigner.commands.operations.AbstractOperation;
import org.fdesigner.navigator.CommonViewer;
import org.fdesigner.navigator.INavigatorContentDescriptor;
import org.fdesigner.navigator.INavigatorContentService;
import org.fdesigner.navigator.internal.navigator.CommonNavigatorMessages;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;

/**
 * Ensures that a given set of content extensions is <i>active</i> and a second
 * non-intersecting set of content extensions are not <i>active</i>.
 *
 * <p>
 * This operation is smart enough not to force any change if each id in each set
 * is already in its desired state (<i>active</i> or <i>inactive</i>).
 * </p>
 *
 * @since 3.2
 *
 */
public class UpdateActiveExtensionsOperation extends AbstractOperation {

	private String[] contentExtensionsToActivate;

	private final CommonViewer commonViewer;

	private final INavigatorContentService contentService;

	/**
	 * Create an operation to activate extensions and refresh the viewer.
	 *
	 * <p> To use only one part of this operation (either "activate" or
	 * "deactivate", but not both), then supply <b>null</b> for the array state
	 * you are not concerned with.
	 * </p>
	 *
	 * @param aCommonViewer
	 *            The CommonViewer instance to update
	 * @param theExtensionsToActivate
	 *            An array of ids that correspond to the extensions that should
	 *            be in the <i>active</i> state after this operation executes.
	 */
	public UpdateActiveExtensionsOperation(CommonViewer aCommonViewer,
			String[] theExtensionsToActivate) {
		super(
				CommonNavigatorMessages.UpdateFiltersOperation_Update_CommonViewer_Filter_);
		commonViewer = aCommonViewer;
		contentService = commonViewer.getNavigatorContentService();
		contentExtensionsToActivate = theExtensionsToActivate;

	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {

		boolean updateExtensionActivation = false;

		// we sort the array in order to use Array.binarySearch();
		Arrays.sort(contentExtensionsToActivate);

		IStructuredSelection selection = null;

		try {
			commonViewer.getControl().setRedraw(false);

			selection = commonViewer.getStructuredSelection();

			INavigatorContentDescriptor[] visibleContentDescriptors = contentService
					.getVisibleExtensions();

			int indexofContentExtensionIdToBeActivated;
			/* is there a delta? */
			for (int i = 0; i < visibleContentDescriptors.length
					&& !updateExtensionActivation; i++) {
				indexofContentExtensionIdToBeActivated = Arrays.binarySearch(
						contentExtensionsToActivate,
						visibleContentDescriptors[i].getId());
				/*
				 * Either we have a filter that should be active that isn't XOR
				 * a filter that shouldn't be active that is currently
				 */
				if (indexofContentExtensionIdToBeActivated >= 0
						^ contentService.isActive(visibleContentDescriptors[i]
								.getId())) {
					updateExtensionActivation = true;
				}
			}

			/* If so, update */
			if (updateExtensionActivation) {

				contentService.getActivationService().activateExtensions(
						contentExtensionsToActivate, true);
				contentService.getActivationService()
						.persistExtensionActivations();


				Object[] expandedElements = commonViewer.getExpandedElements();

				contentService.update();

				commonViewer.refresh();

				Object[] originalObjects = selection.toArray();

				commonViewer.setExpandedElements(expandedElements);

				IStructuredSelection newSelection = new StructuredSelection(originalObjects);
				commonViewer.setSelection(newSelection, true);
			}

		} finally {
			commonViewer.getControl().setRedraw(true);
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) {
		return null;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) {
		return null;
	}
}
