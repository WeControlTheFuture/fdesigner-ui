/*******************************************************************************
 * Copyright (c) 2005, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM - Initial API and implementation
 * Red Hat, Inc - WizardProjectsImportPage[_ArchiveSelectTitle, _SelectArchiveDialogTitle]
 *******************************************************************************/
package org.fdesigner.ide.internal.wizards.datatransfer;

import org.fdesigner.supplement.util.NLS;

public class DataTransferMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.wizards.datatransfer.messages";//$NON-NLS-1$

	// ==============================================================================
	// Data Transfer Wizards
	// ==============================================================================
	public static String DataTransfer_fileSystemTitle;
	public static String ZipExport_exportTitle;
	public static String ArchiveExport_exportTitle;

	public static String DataTransfer_browse;
	public static String DataTransfer_selectTypes;
	public static String DataTransfer_selectAll;
	public static String DataTransfer_deselectAll;
	public static String DataTransfer_refresh;
	public static String DataTransfer_cannotOverwrite;
	public static String DataTransfer_emptyString;
	public static String DataTransfer_scanningMatching;
	public static String DataTransfer_information;

	// --- Import Wizards ---
	public static String DataTransfer_importTitle;

	public static String DataTransfer_importTask;
	public static String ImportOperation_cannotCopy;
	public static String ImportOperation_importProblems;
	public static String ImportOperation_openStreamError;
	public static String ImportOperation_closeStreamError;
	public static String ImportOperation_coreImportError;
	public static String ImportOperation_targetSameAsSourceError;
	public static String ImportPage_filterSelections;
	public static String ImportOperation_cannotReadError;

	public static String FileImport_selectSource;
	public static String FileImport_selectSourceTitle;
	public static String FileImport_fromDirectory;
	public static String FileImport_importFileSystem;
	public static String FileImport_overwriteExisting;
	public static String FileImport_createTopLevel;
	public static String FileImport_createVirtualFolders;
	public static String FileImport_importElementsAs;
	public static String FileImport_createVirtualFoldersTooltip;
	public static String FileImport_createLinksInWorkspace;
	public static String FileImport_advanced;
	public static String FileImport_noneSelected;
	public static String FileImport_cannotImportFilesUnderAVirtualFolder;
	public static String FileImport_haveToCreateLinksUnderAVirtualFolder;
	public static String FileImport_invalidSource;
	public static String FileImport_sourceEmpty;
	public static String FileImport_importProblems;
	public static String ZipImport_description;
	public static String ZipImport_couldNotClose;
	public static String ZipImport_badFormat;
	public static String ZipImport_couldNotRead;
	public static String ZipImport_fromFile;
	public static String ZipImportSource_title;

	public static String ArchiveImport_description;
	public static String ArchiveImport_fromFile;
	public static String ArchiveImportSource_title;
	public static String TarImport_badFormat;

	public static String WizardExternalProjectImportPage_locationError;
	public static String WizardExternalProjectImportPage_projectLocationEmpty;
	public static String WizardExternalProjectImportPage_projectExistsMessage;
	public static String WizardExternalProjectImportPage_projectContentsLabel;
	public static String WizardExternalProjectImportPage_nameLabel;
	public static String WizardExternalProjectImportPage_title;
	public static String WizardExternalProjectImportPage_description;
	public static String WizardExternalProjectImportPage_notAProject;
	public static String WizardProjectsImportPage_ProjectsListTitle;
	public static String WizardProjectsImportPage_ProcessingMessage;
	public static String WizardProjectsImportPage_SelectDialogTitle;
	public static String WizardProjectsImportPage_SearchingMessage;
	public static String WizardExternalProjectImportPage_errorMessage;
	public static String WizardProjectsImportPage_ImportProjectsTitle;
	public static String WizardExternalProjectImportPage_caseVariantExistsError;
	public static String WizardExternalProjectImportPage_directoryLabel;
	public static String WizardProjectsImportPage_RootSelectTitle;
	public static String WizardProjectsImportPage_ImportProjectsDescription;
	public static String WizardProjectsImportPage_CheckingMessage;
	public static String WizardProjectsImportPage_ArchiveSelectTitle;
	public static String WizardProjectsImportPage_SelectArchiveDialogTitle;
	public static String WizardProjectsImportPage_CreateProjectsTask;
	public static String WizardProjectsImportPage_SearchForNestedProjects;
	public static String WizardProjectsImportPage_CopyProjectsIntoWorkspace;
	public static String WizardProjectsImportPage_projectsInWorkspace;
	public static String WizardProjectsImportPage_projectsInvalid;
	public static String WizardProjectsImportPage_projectsInWorkspaceAndInvalid;
	public static String WizardProjectsImportPage_noProjectsToImport;
	public static String WizardProjectsImportPage_projectLabel;
	public static String WizardProjectsImportPage_hideExistingProjects;
	public static String WizardProjectsImportPage_closeProjectsAfterImport;
	public static String WizardProjectsImportPage_invalidProjectName;

	// --- Export Wizards ---
	public static String DataTransfer_export;

	public static String DataTransfer_exportingTitle;
	public static String DataTransfer_createTargetDirectory;
	public static String DataTransfer_directoryCreationError;
	public static String DataTransfer_errorExporting;
	public static String DataTransfer_exportProblems;

	public static String ExportFile_overwriteExisting;
	public static String ExportFile_resolveLinkedResources;

	public static String FileExport_selectDestinationTitle;
	public static String FileExport_selectDestinationMessage;
	public static String FileExport_exportLocalFileSystem;
	public static String FileExport_destinationEmpty;
	public static String FileExport_createDirectoryStructure;
	public static String FileExport_createSelectedDirectories;
	public static String FileExport_noneSelected;
	public static String FileExport_directoryExists;
	public static String FileExport_conflictingContainer;
	public static String FileExport_rootName;
	public static String FileSystemExportOperation_problemsExporting;
	public static String FileExport_toDirectory;
	public static String FileExport_damageWarning;

	public static String ZipExport_compressContents;
	public static String ZipExport_destinationLabel;
	public static String ZipExport_mustBeFile;
	public static String ZipExport_alreadyExists;
	public static String ZipExport_alreadyExistsError;
	public static String ZipExport_cannotOpen;
	public static String ZipExport_cannotClose;
	public static String ZipExport_selectDestinationTitle;
	public static String ZipExport_destinationEmpty;

	public static String ArchiveExport_description;
	public static String ArchiveExport_destinationLabel;
	public static String ArchiveExport_selectDestinationTitle;
	public static String ArchiveExport_destinationEmpty;
	public static String ArchiveExport_saveInZipFormat;
	public static String ArchiveExport_saveInTarFormat;

	public static String TarImport_invalid_tar_format;

	// Smart import
	public static String SmartImportWizardPage_selectFolderOrArchiveToImport;
	public static String SmartImportWizardPage_browseForFolder;
	public static String SmartImportProposals_alreadyImportedAsProject_title;
	public static String SmartImportProposals_anotherProjectWithSameNameExists_title;
	public static String SmartImportProposals_anotherProjectWithSameNameExists_description;

	public static String SmartImportWizardPage_importProjectsInFolderTitle;
	public static String SmartImportWizardPage_importProjectsInFolderDescription;
	public static String SmartImportWizardPage_selectRootDirectory;
	public static String SmartImportWizardPage_incorrectRootDirectory;
	public static String SmartImportWizardPage_browse;
	public static String SmartImportWizardPage_workingSets;
	public static String SmartImportWizardPage_detectNestedProjects;
	public static String SmartImportWizardPage_configureProjects;
	public static String SmartImportWizardPage_showAvailableDetectors;
	public static String SmartImportWizardPage_availableDetectors_title;
	public static String SmartImportWizardPage_availableDetectors_description;
	public static String SmartImportWizardPage_selectArchiveButton;
	public static String SmartImportWizardPage_selectArchiveTitle;
	public static String SmartImportWizardPage_allSupportedArchives;
	public static String SmartImportWizardPage_expandingArchive;
	public static String SmartImportWizardPage_overwriteArchiveDirectory_title;
	public static String SmartImportWizardPage_overwriteArchiveDirectory_message;
	public static String SmartImportWizardPage_incompleteExpand_title;
	public static String SmartImportWizardPage_incompleteExpand_message;
	public static String SmartImportWizardPage_scanProjectsFailed;
	public static String SmartImportWizardPage_selectAtLeastOneFolderToOpenAsProject;
	public static String SmartImportWizardPage_showOtherSpecializedImportWizard;
	public static String SmartImportWizardPage_closeProjectsAfterImport;

	public static String SmartImportJob_discardRootProject_title;
	public static String SmartImportJob_discardRootProject_description;
	public static String SmartImportProposals_selectionSummary;
	public static String SmartImportProposals_folder;
	public static String SmartImportProposals_importAs;
	public static String SmartImportProposals_hideExistingProjects;
	public static String SmartImportProposals_inspecitionCanceled;
	public static String SmartImportProposals_errorWhileInspecting;

	public static String SmartImportReport_importedProjects;
	public static String SmartImportReport_importedProjectsWithCount;
	public static String SmartImportReport_relativePath;
	public static String SmartImportReport_project;
	public static String SmartImportReport_natures;
	public static String SmartImportReport_importErrors;
	public static String SmartImportReport_error;

	public static String SmartImportJob_detectAndConfigureProjects;
	public static String SmartImportJob_configuringSelectedDirectories;
	public static String SmartImportJob_configuring;
	public static String SmartImportJob_crawling;
	public static String SmartImportJob_continuingConfiguration;
	public static String SmartImportJob_inspecting;
	public static String SmartImportJob_importingProjectIntoWorkspace;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, DataTransferMessages.class);
	}
}
