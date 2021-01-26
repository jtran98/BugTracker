package com.jtran98.BugTracker.util;

/**
 * Constants for user controller mappings
 * @author Jacky Tran
 *
 */
public final class UserEndpointConstants {
	public static final String BASE = "/users";
	public static final String USER_DETAILS = "/user-details";
	public static final String REDIRECT_VIEW_USERS = "/view-users";
	public static final String SETTINGS = "/settings";
	public static final String UPDATE_PASSWORD = SETTINGS+"/update-password";
	public static final String UPDATE_USERNAME = SETTINGS+"/update-username";
	public static final String PROJECT_MEMBERS = "/my-project-members";
	public static final String DISABLE_ACCOUNT = "/disable-account";
	public static final String ENABLE_ACCOUNT = "/enable-account";
	public static final String ASSIGN_USER_TO_PROJECT = "/assign-user-to-project";
	public static final String ASSIGN_USER_TO_ROLE = "/assign-user-to-role";
	public static final String REDIRECT_VIEW_USER = "/view-user";
	public static final String CREATE_NEW_USER = "/create-new-user";
	public static final String SAVE_USER = "/save-user";
	public static final String MANAGE_USERS = "/manage-users";
	private UserEndpointConstants() {}
}
