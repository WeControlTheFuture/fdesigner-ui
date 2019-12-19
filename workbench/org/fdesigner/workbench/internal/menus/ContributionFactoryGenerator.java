/*******************************************************************************
 * Copyright (c) 2012, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.menus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.fdesigner.e4.core.contexts.ContextFunction;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.ui.model.application.ui.MCoreExpression;
import org.fdesigner.e4.ui.model.application.ui.MUIElement;
import org.fdesigner.e4.ui.model.application.ui.impl.UiFactoryImpl;
import org.fdesigner.e4.ui.model.application.ui.menu.MMenuItem;
import org.fdesigner.e4.ui.model.application.ui.menu.MToolItem;
import org.fdesigner.e4.ui.workbench.internal.workbench.OpaqueElementUtil;
import org.fdesigner.expressions.Expression;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.ui.jface.action.IContributionItem;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.services.ServiceLocator;
import org.fdesigner.workbench.menus.AbstractContributionFactory;
import org.fdesigner.workbench.menus.IMenuService;

public class ContributionFactoryGenerator extends ContextFunction {
	private AbstractContributionFactory factoryImpl;
	private IConfigurationElement configElement;
	private int type;

	public ContributionFactoryGenerator(AbstractContributionFactory factory, int type) {
		this.factoryImpl = factory;
		this.type = type;
	}

	public ContributionFactoryGenerator(IConfigurationElement element, int type) {
		configElement = element;
		this.type = type;
	}

	private AbstractContributionFactory getFactory() {
		if (factoryImpl == null && configElement != null) {
			try {
				factoryImpl = (AbstractContributionFactory) configElement.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException e) {
				WorkbenchPlugin.log(e);
				return null;
			}
		}
		return factoryImpl;
	}

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		AbstractContributionFactory factory = getFactory();
		final IMenuService menuService = context.get(IMenuService.class);
		final ContributionRoot root = new ContributionRoot(menuService, new HashSet<>(), null, factory);
		ServiceLocator sl = new ServiceLocator();
		sl.setContext(context);
		factory.createContributionItems(sl, root);
		final List contributionItems = root.getItems();
		final Map<IContributionItem, Expression> itemsToExpression = root.getVisibleWhen();
		List<MUIElement> menuElements = new ArrayList<>();
		for (Object obj : contributionItems) {
			if (obj instanceof IContributionItem) {
				IContributionItem ici = (IContributionItem) obj;
				MUIElement opaqueItem = createUIElement(ici);
				if (opaqueItem != null) {
					if (itemsToExpression.containsKey(ici)) {
						final Expression ex = itemsToExpression.get(ici);
						MCoreExpression exp = UiFactoryImpl.eINSTANCE.createCoreExpression();
						exp.setCoreExpressionId("programmatic." + ici.getId()); //$NON-NLS-1$
						exp.setCoreExpression(ex);
						opaqueItem.setVisibleWhen(exp);
					}
					menuElements.add(opaqueItem);
				}
			}
		}
		context.set(List.class, menuElements);

		// return something disposable
		return (Runnable) () -> root.release();
	}

	private MUIElement createUIElement(IContributionItem ici) {
		switch (type) {
		case 0:
			return createMenuItem(ici);
		case 1:
			return createToolItem(ici);
		}
		return null;
	}

	private MUIElement createMenuItem(IContributionItem ici) {
		MMenuItem opaqueItem = OpaqueElementUtil.createOpaqueMenuItem();
		opaqueItem.setElementId(ici.getId());
		OpaqueElementUtil.setOpaqueItem(opaqueItem, ici);
		return opaqueItem;
	}

	private MUIElement createToolItem(IContributionItem ici) {
		MToolItem opaqueItem = OpaqueElementUtil.createOpaqueToolItem();
		opaqueItem.setElementId(ici.getId());
		OpaqueElementUtil.setOpaqueItem(opaqueItem, ici);
		return opaqueItem;
	}
}
