/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.about;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.core.IProduct;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.window.IShellProvider;
import org.fdesigner.workbench.ISharedImages;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.about.InstallationPage;
import org.fdesigner.workbench.handlers.IHandlerActivation;
import org.fdesigner.workbench.handlers.IHandlerService;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.swt.IFocusService;

/**
 * Abstract superclass of about dialog installation pages. The ProductInfoPage
 * is set up so that the page can be hosted as one of many pages in the
 * InstallationDialog, or as the only page in a ProductInfoDialog.
 */

public abstract class ProductInfoPage extends InstallationPage implements IShellProvider {

	private IProduct product;

	private String productName;

	protected IProduct getProduct() {
		if (product == null)
			product = Platform.getProduct();
		return product;
	}

	public String getProductName() {
		if (productName == null) {
			if (getProduct() != null) {
				productName = getProduct().getName();
			}
			if (productName == null) {
				productName = WorkbenchMessages.AboutDialog_defaultProductName;
			}
		}
		return productName;
	}

	public void setProductName(String name) {
		productName = name;
	}

	abstract String getId();

	protected Composite createOuterComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		return composite;
	}

	protected final void addCopySupport(Table table) {
		CopyTableSelectionHandler handler = new CopyTableSelectionHandler();
		Menu menu = new Menu(table);
		MenuItem copyItem = new MenuItem(menu, SWT.NONE);
		copyItem.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> handler.copySelection(table)));
		copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
		copyItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_COPY));

		table.setMenu(menu);

		IFocusService fs = PlatformUI.getWorkbench().getService(IFocusService.class);
		IHandlerService hs = PlatformUI.getWorkbench().getService(IHandlerService.class);
		if (fs != null && hs != null) {
			fs.addFocusTracker(table, getClass().getName() + ".table"); //$NON-NLS-1$
			IHandlerActivation handlerActivation = hs.activateHandler(IWorkbenchCommandConstants.EDIT_COPY, handler,
					controlFocusedExpression(table));
			table.addDisposeListener(e -> hs.deactivateHandler(handlerActivation));
		}
	}

	private static Expression controlFocusedExpression(Control control) {
		return new Expression() {
			@Override
			public EvaluationResult evaluate(IEvaluationContext context) {
				return context.getVariable(ISources.ACTIVE_FOCUS_CONTROL_NAME) == control ? EvaluationResult.TRUE
						: EvaluationResult.FALSE;
			}

			@Override
			public void collectExpressionInfo(ExpressionInfo info) {
				info.addVariableNameAccess(ISources.ACTIVE_FOCUS_CONTROL_NAME);
			}
		};
	}
}
