/*******************************************************************************
 * Copyright (c) 2009, 2018 IBM Corporation and others.
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

import org.eclipse.swt.widgets.Label;
import org.fdesigner.navigator.ICommonFilterDescriptor;
import org.fdesigner.navigator.INavigatorContentDescriptor;
import org.fdesigner.navigator.internal.navigator.CommonNavigatorMessages;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.viewers.ISelectionChangedListener;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.SelectionChangedEvent;

/**
 * @since 3.2
 */
public class FilterDialogSelectionListener implements ISelectionChangedListener {


	private Label descriptionText;

	protected FilterDialogSelectionListener(Label aDescriptionText) {
		descriptionText = aDescriptionText;

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection structuredSelection = event.getStructuredSelection();
		Object element = structuredSelection.getFirstElement();
		if (element instanceof INavigatorContentDescriptor) {
			INavigatorContentDescriptor ncd = (INavigatorContentDescriptor) element;
			String desc = NLS
					.bind(
							CommonNavigatorMessages.CommonFilterSelectionDialog_Hides_all_content_associated,
							new Object[] { ncd.getName() });
			descriptionText.setText(desc);
		} else if (element instanceof ICommonFilterDescriptor) {
			ICommonFilterDescriptor cfd = (ICommonFilterDescriptor) element;
			String description = 	cfd.getDescription();
			if(description != null)
				descriptionText.setText(description);
			else
				descriptionText.setText(NLS.bind(CommonNavigatorMessages.FilterDialogSelectionListener_Enable_the_0_filter_, cfd.getName()));
		}

	}
}
