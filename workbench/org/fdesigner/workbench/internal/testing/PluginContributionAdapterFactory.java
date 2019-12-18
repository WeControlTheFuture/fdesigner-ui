/*******************************************************************************
 * Copyright (c) 2010, 2016 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.testing;

import org.eclipse.ui.testing.ContributionInfo;
import org.fdesigner.framework.framework.Bundle;
import org.fdesigner.framework.framework.FrameworkUtil;
import org.fdesigner.runtime.common.runtime.IAdapterFactory;
import org.fdesigner.runtime.jobs.runtime.jobs.Job;
import org.fdesigner.workbench.IPluginContribution;
import org.fdesigner.workbench.internal.decorators.DecoratorDefinition;
import org.fdesigner.workbench.internal.dialogs.WizardCollectionElement;
import org.fdesigner.workbench.internal.dialogs.WorkbenchWizardElement;
import org.fdesigner.workbench.internal.preferences.WorkbenchPreferenceExpressionNode;
import org.fdesigner.workbench.internal.progress.JobInfo;
import org.fdesigner.workbench.internal.registry.ActionSetDescriptor;
import org.fdesigner.workbench.internal.registry.Category;
import org.fdesigner.workbench.internal.registry.EditorDescriptor;
import org.fdesigner.workbench.internal.registry.PerspectiveDescriptor;
import org.fdesigner.workbench.internal.registry.ViewDescriptor;
import org.fdesigner.workbench.internal.themes.ColorDefinition;
import org.fdesigner.workbench.internal.themes.ThemeElementCategory;
import org.fdesigner.workbench.views.IViewCategory;

/**
 * @since 3.6
 *
 */
public class PluginContributionAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adapterType != ContributionInfo.class) {
			return null;
		}
		if (adaptableObject instanceof IPluginContribution) {
			IPluginContribution contribution = (IPluginContribution) adaptableObject;

			String elementType;

			if (contribution instanceof EditorDescriptor) {
				elementType = ContributionInfoMessages.ContributionInfo_Editor;
			} else if (contribution instanceof ViewDescriptor) {
				elementType = ContributionInfoMessages.ContributionInfo_View;
			} else if (contribution instanceof ActionSetDescriptor) {
				elementType = ContributionInfoMessages.ContributionInfo_ActionSet;
			} else if (contribution instanceof Category) {
				elementType = ContributionInfoMessages.ContributionInfo_Category;
			} else if (contribution instanceof IViewCategory) {
				elementType = ContributionInfoMessages.ContributionInfo_Category;
			} else if (contribution instanceof ThemeElementCategory) {
				elementType = ContributionInfoMessages.ContributionInfo_Category;
			} else if (contribution instanceof WizardCollectionElement) {
				elementType = ContributionInfoMessages.ContributionInfo_Category;
			} else if (contribution instanceof ColorDefinition) {
				elementType = ContributionInfoMessages.ContributionInfo_ColorDefinition;
			} else if (contribution instanceof WorkbenchWizardElement) {
				elementType = ContributionInfoMessages.ContributionInfo_Wizard;
			} else if (contribution instanceof PerspectiveDescriptor) {
				elementType = ContributionInfoMessages.ContributionInfo_Perspective;
			} else if (contribution instanceof WorkbenchPreferenceExpressionNode) {
				elementType = ContributionInfoMessages.ContributionInfo_Page;
			} else if (contribution instanceof DecoratorDefinition) {
				elementType = ContributionInfoMessages.ContributionInfo_LabelDecoration;
			} else {
				elementType = ContributionInfoMessages.ContributionInfo_Unknown;
			}

			return adapterType.cast(new ContributionInfo(contribution.getPluginId(), elementType, null));
		}
		if (adaptableObject instanceof JobInfo) {
			JobInfo jobInfo = (JobInfo) adaptableObject;
			Job job = jobInfo.getJob();
			if (job != null) {
				Bundle bundle = FrameworkUtil.getBundle(job.getClass());
				if (bundle != null) {
					return adapterType.cast(new ContributionInfo(bundle.getSymbolicName(),
							ContributionInfoMessages.ContributionInfo_Job, null));
				}
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { ContributionInfo.class };
	}

}
