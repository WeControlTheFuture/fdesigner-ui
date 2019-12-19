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
 ******************************************************************************/

package org.fdesigner.e4.ui.workbench.renderers.swt;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.fdesigner.e4.core.contexts.ContextInjectionFactory;
import org.fdesigner.e4.core.contexts.EclipseContextFactory;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.core.services.contributions.IContributionFactory;
import org.fdesigner.e4.ui.model.application.ui.menu.MToolControl;
import org.fdesigner.e4.ui.workbench.modeling.EModelService;
import org.fdesigner.e4.ui.workbench.swt.internal.workbench.swt.AbstractPartRenderer;
import org.fdesigner.ui.jface.action.ControlContribution;

public class ToolControlContribution extends ControlContribution {

	private MToolControl model;

	@Inject
	private IContributionFactory contribFactory;

	@Inject
	private EModelService modelService;

	public ToolControlContribution() {
		super(null);
	}

	@Override
	protected Control createControl(Composite parent) {
		IEclipseContext localContext = EclipseContextFactory.create();

		final Composite newComposite = new Composite(parent, SWT.NONE);
		newComposite.setLayout(new FillLayout());
		localContext.set(Composite.class, newComposite);
		localContext.set(MToolControl.class, model);

		final IEclipseContext parentContext = modelService.getContainingContext(model);
		if (model.getObject() == null) {
			final Object tcImpl = contribFactory.create(model.getContributionURI(), parentContext, localContext);
			model.setObject(tcImpl);
			newComposite.addDisposeListener(e -> {
				ContextInjectionFactory.uninject(tcImpl, parentContext);
				model.setObject(null);
			});
		}

		model.setWidget(newComposite);
		newComposite.setData(AbstractPartRenderer.OWNING_ME, model);
		newComposite.setData(this);

		return newComposite;
	}

	public void setModel(MToolControl c) {
		model = c;
		setId(model.getElementId());
	}
}
