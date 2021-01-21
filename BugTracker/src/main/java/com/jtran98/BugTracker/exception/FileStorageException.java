package com.jtran98.BugTracker.exception;

import java.io.IOException;

/**
 * Custom exception for when file upload/download fails
 * @author Jacky Tran
 *
 */
public class FileStorageException extends Exception {
	public FileStorageException(String error) {
		super(error);
	}

	public FileStorageException(String string, IOException ex) {
		super(string, ex);
	}
}
