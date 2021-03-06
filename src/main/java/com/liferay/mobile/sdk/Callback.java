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

package com.liferay.mobile.sdk;

import static com.liferay.mobile.sdk.Callback.ThreadRunner.run;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.liferay.mobile.sdk.callback.OnFailure;
import com.liferay.mobile.sdk.callback.OnSuccess;
import com.liferay.mobile.sdk.http.Response;
import com.liferay.mobile.sdk.http.ResponseValidator;
import com.liferay.mobile.sdk.json.JSONParser;

import java.lang.reflect.Type;

/**
 * @author Bruno Farache
 */
public abstract class Callback<T> implements OnSuccess<T>, OnFailure {

	public void doFailure(final Exception e) {
		run(new Runnable() {

			@Override
			public void run() {
				onFailure(e);
			}

		});
	}

	public T doInBackground(T result) throws Exception {
		return result;
	}

	public void doSuccess(final T result) {
		run(new Runnable() {

			@Override
			public void run() {
				onSuccess(result);
			}

		});
	}

	public void inBackground(Response response) {
		try {
			ResponseValidator validator = config.responseValidator();

			validator.validateStatusCode(response);

			if (type == Response.class) {
				doSuccess(doInBackground((T)response));
				return;
			}

			String json = validator.validateBody(response.bodyAsString());
			T result = JSONParser.fromJSON(json, type);
			doInBackground(result);
			doSuccess(result);
		}
		catch (Exception e) {
			doFailure(e);
		}
	}

	public void init(Config config, Type type) {
		this.config = config;
		this.type = type;
	}

	public static class ThreadRunner {

		public synchronized static void handler(Handler handler) {
			ThreadRunner.handler = handler;
		}

		public static void run(Runnable runnable) {
			if (handler == null) {
				runnable.run();
				return;
			}

			handler.post(runnable);
		}

		protected static Handler handler;

		static {
			try {
				Class.forName("android.os.Build");

				if (Build.VERSION.SDK_INT != 0) {
					handler = new Handler(Looper.getMainLooper());
				}
			}
			catch (ClassNotFoundException cnfe) {
			}
		}

	}

	protected Config config;
	protected Type type;

}