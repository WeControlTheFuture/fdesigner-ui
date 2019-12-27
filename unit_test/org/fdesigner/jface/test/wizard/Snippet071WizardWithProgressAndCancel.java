/*******************************************************************************
 * Copyright (c) 2016 Remain Software and others,
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Wim Jongman - Wizard with cancelable progress monitor
 *******************************************************************************/
package org.fdesigner.jface.test.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.ui.jface.operation.IRunnableWithProgress;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.wizard.Wizard;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.ui.jface.wizard.WizardPage;

/**
 * Example Wizard with Progress Monitor and Cancel
 *
 * @author Wim Jongman &lt;wim.jongman@remainsoftware.com&gt;
 */
public class Snippet071WizardWithProgressAndCancel {

	private static final class MyWizard extends Wizard {
		private int fTasks;

		private MyPage fPage;

		private final class MyPage extends WizardPage {
			private Button fCloseButton;

			private MyPage(String pPageName, String pTitle, ImageDescriptor pTitleImage) {
				super(pPageName, pTitle, pTitleImage);
			}

			@Override
			public void createControl(Composite pParent) {
				fCloseButton = new Button(pParent, SWT.CHECK);
				fCloseButton.setText("Close wizard after progress is complete");
				setControl(fCloseButton);
			}

			public boolean isCloseWanted() {
				return fCloseButton.getSelection();
			}
		}

		public MyWizard(int pTasks) {
			this.fTasks = pTasks;
		}

		@Override
		public boolean performFinish() {
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor pMonitor) throws InvocationTargetException, InterruptedException {
						pMonitor.beginTask("Starting", fTasks);
						for (int j = 1; j <= fTasks; j++) {
							pMonitor.setTaskName("Task " + j);
							pMonitor.worked(1);
							doWork();
							if (pMonitor.isCanceled()) {
								doCancel(pMonitor);
								break;
							}
						}
					}

					private void doWork() throws InterruptedException {
						Thread.sleep(500);
					}

					private void doCancel(IProgressMonitor pMonitor) throws InterruptedException {
						pMonitor.setTaskName("Canceling. Please wait ...");
						Thread.sleep(2000);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			return fPage.isCloseWanted();

		}

		@Override
		public void addPages() {
			fPage = new MyPage("Page1", "First and final page", null);
			addPage(fPage);
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		openWizard(shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void openWizard(final Shell pShell) {
		Wizard testWizard = new MyWizard(10);
		testWizard.setNeedsProgressMonitor(true);
		WizardDialog dialog = new WizardDialog(pShell, testWizard);
		dialog.open();
		pShell.dispose();
	}
}
