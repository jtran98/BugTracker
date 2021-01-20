package com.jtran98.BugTracker.exception;

import java.io.IOException;

public class FileStorageException extends Exception {
	public FileStorageException(String error) {
		super(error);
	}

	public FileStorageException(String string, IOException ex) {
		super(string, ex);
	}
}
