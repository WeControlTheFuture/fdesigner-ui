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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.fdesigner.ide.extensions.IMarkerResolution;
import org.fdesigner.ide.internal.ide.IDEInternalWorkbenchImages;
import org.fdesigner.ide.internal.ide.StatusUtil;
import org.fdesigner.ide.views.markers.internal.MarkerMessages;
import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.SubMonitor;
import org.fdesigner.ui.jface.operation.IRunnableWithProgress;
import org.fdesigner.ui.jface.wizard.IWizardPage;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.workbench.IWorkbenchPartSite;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * QuickFixWizard is the wizard for quick fixes.
 *
 * @since 3.4
 *
 */
class QuickFixWizard extends Wizard {

	private IMarker[] selectedMarkers;
	private Map<IMarkerResolution, Collection<IMarker>> resolutionMap;
	private String description;
	private IWorkbenchPartSite partSite;

	/**
	 * Create the wizard with the map of resolutions.
	 *
	 * @param description the description of the problem
	 * @param selectedMarkers the markers that were selected
	 * @param resolutions Map key {@link IMarkerResolution} value {@link IMarker} []
	 * @param site the {@link IWorkbenchPartSite} to open the markers in
	 */
	public QuickFixWizard(String description, IMarker[] selectedMarkers, Map<IMarkerResolution, Collection<IMarker>> resolutions, IWorkbenchPartSite site) {
		this.selectedMarkers= selectedMarkers;
		this.resolutionMap = resolutions;
		this.description = description;
		partSite = site;
		setDefaultPageImageDescriptor(IDEInternalWorkbenchImages
				.getImageDescriptor(IDEInternalWorkbenchImages.IMG_DLGBAN_QUICKFIX_DLG));
		setNeedsProgressMonitor(true);

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(new QuickFixPage(description, selectedMarkers, resolutionMap, partSite));
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress finishRunnable = mon -> {
			IWizardPage[] pages = getPages();
			SubMonitor subMonitor = SubMonitor.convert(mon, MarkerMessages.MarkerResolutionDialog_Fixing,
					(10 * pages.length) + 1);
			subMonitor.worked(1);
			for (IWizardPage page : pages) {
				// Allow for cancel event processing
				getShell().getDisplay().readAndDispatch();
				QuickFixPage wizardPage = (QuickFixPage) page;
				wizardPage.performFinish(subMonitor.split(10));
			}
		};

		try {
			getContainer().run(false, true, finishRunnable);
		} catch (InvocationTargetException | InterruptedException e) {
			StatusManager.getManager().handle(StatusUtil.newError(e));
			return false;
		}

		return true;
	}

}
