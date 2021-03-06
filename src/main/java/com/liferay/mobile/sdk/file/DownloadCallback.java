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

package com.liferay.mobile.sdk.file;

import static com.liferay.mobile.sdk.file.FileTransfer.transfer;

import com.liferay.mobile.sdk.Callback;
import com.liferay.mobile.sdk.http.Response;

/**
 * @author Bruno Farache
 */
public class DownloadCallback extends Callback {

	public DownloadCallback(
		Callback callback, FileProgressCallback progressCallback, Object tag) {

		this.callback = callback;
		this.progressCallback = progressCallback;
		this.tag = tag;
	}

	@Override
	public void inBackground(Response response) {
		try {
			transfer(response.bodyAsStream(), progressCallback, tag, null);
			callback.inBackground(response);
		}
		catch (Exception e) {
			doFailure(e);
		}
	}

	@Override
	public void onFailure(Exception exception) {
		callback.onFailure(exception);
	}

	@Override
	public void onSuccess(Object result) {
		callback.onSuccess(result);
	}

	protected Callback callback;
	protected FileProgressCallback progressCallback;
	protected Object tag;

}