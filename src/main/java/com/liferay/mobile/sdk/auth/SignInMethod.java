/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.sdk.auth;

import com.liferay.mobile.sdk.util.Validator;

/**
 * @author Bruno Farache
 */
public enum SignInMethod {

	USER_ID, EMAIL, SCREEN_NAME;

	public static SignInMethod fromUsername(String username) {
		if (Validator.isEmailAddress(username)) {
			return EMAIL;
		}
		else if (isUserId(username)) {
			return USER_ID;
		}

		return SCREEN_NAME;
	}

	protected static boolean isUserId(String username) {
		boolean isUserId = true;

		try {
			Long.parseLong(username);
		}
		catch (Exception e) {
			isUserId = false;
		}

		return isUserId;
	}

}