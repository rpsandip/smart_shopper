package com.chillies.smartshopper.common.util;

/**
 * DirectoryFiles : is utility enum for All the folders in mindmaze_home
 * directory.
 * 
 * @author Jinen Kothari
 *
 */
public enum DirectoryFiles {

	CONFIG("config"), FILES("files");

	private final String path;

	private DirectoryFiles(final String path) {
		this.path = path;
	}

	public String getPath() {
		return AppUtils.PATH_SEPARATOR + path;
	}

	public String getCompletePath() {
		return AppUtils.getHOME() + AppUtils.PATH_SEPARATOR + path;
	}

}
