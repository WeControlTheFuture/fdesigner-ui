/*******************************************************************************
 * Copyright (c) 2006, 2014 Tom Schindl and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Tom Schindl - initial API and implementation
 *     IBM - Improvement for Bug 159625 [Snippets] Update Snippet011CustomTooltips to reflect new API
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 414565
 *******************************************************************************/

package org.fdesigner.jface.test.viewers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.fdesigner.ui.jface.viewers.ArrayContentProvider;
import org.fdesigner.ui.jface.viewers.CellLabelProvider;
import org.fdesigner.ui.jface.viewers.ColumnViewerToolTipSupport;
import org.fdesigner.ui.jface.viewers.TableViewer;
import org.fdesigner.ui.jface.viewers.TableViewerColumn;
import org.fdesigner.ui.jface.viewers.ViewerCell;
import org.fdesigner.ui.jface.window.ToolTip;

/**
 * Explore New API: JFace custom tooltips drawing.
 *
 */
public class Snippet011CustomTooltips {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		TableViewer v = new TableViewer(shell, SWT.FULL_SELECTION);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
		v.setContentProvider(ArrayContentProvider.getInstance());
		ColumnViewerToolTipSupport.enableFor(v,ToolTip.NO_RECREATE);

		CellLabelProvider labelProvider = new CellLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				return "Tooltip (" + element + ")";
			}

			@Override
			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				return 2000;
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				return 5000;
			}

			@Override
			public void update(ViewerCell cell) {
				cell.setText(cell.getElement().toString());

			}
		};

		TableViewerColumn column = new TableViewerColumn(v, SWT.NONE);
		column.setLabelProvider(labelProvider);
		column.getColumn().setText("Column 1");
		column.getColumn().setWidth(100);
		String[] values = new String[] { "one", "two", "three", "four", "five", "six",
				"seven", "eight", "nine", "ten" };
		v.setInput(values);

		shell.setSize(200, 200);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

}
