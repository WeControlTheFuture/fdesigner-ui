/**
 * Copyright (c) 2008, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      IBM Corporation - initial API and implementation
 */
package org.fdesigner.e4.ui.model.application.ui.menu.impl;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.fdesigner.e4.ui.model.application.ui.MUIElement;
import org.fdesigner.e4.ui.model.application.ui.impl.ElementContainerImpl;
import org.fdesigner.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.fdesigner.e4.ui.model.application.ui.menu.MToolBar;
import org.fdesigner.e4.ui.model.application.ui.menu.MToolBarElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Bar</b></em>'.
 * <!-- end-user-doc -->
 *
 * @since 1.0
 * @generated
 */
public class ToolBarImpl extends ElementContainerImpl<MToolBarElement> implements MToolBar {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ToolBarImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MenuPackageImpl.Literals.TOOL_BAR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public List<MToolBarElement> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<MToolBarElement>(MToolBarElement.class, this,
					MenuPackageImpl.TOOL_BAR__CHILDREN, UiPackageImpl.UI_ELEMENT__PARENT) {
				private static final long serialVersionUID = 1L;

				@Override
				public Class<?> getInverseFeatureClass() {
					return MUIElement.class;
				}
			};
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific type known in this context.
	 * @generated
	 */
	@Override
	public void setSelectedElement(MToolBarElement newSelectedElement) {
		super.setSelectedElement(newSelectedElement);
	}

} //ToolBarImpl
