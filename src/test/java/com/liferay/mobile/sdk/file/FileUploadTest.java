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

import com.liferay.mobile.sdk.BaseTest;
import com.liferay.mobile.sdk.Call;
import com.liferay.mobile.sdk.Config;
import com.liferay.mobile.sdk.DLAppServiceTest;
import com.liferay.mobile.sdk.ServiceBuilder;
import com.liferay.mobile.sdk.TestCallback;
import com.liferay.mobile.sdk.util.PropertiesUtil;
import com.liferay.mobile.sdk.v7.dlapp.DLAppService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bruno Farache
 */
public class FileUploadTest extends BaseTest {

	public static final String FILE_NAME = "barto.jpg";

	public static final String MIME_TYPE = "image/jpg";

	public static JSONObject uploadPhoto(PropertiesUtil props)
		throws Exception {

		DLAppService service = ServiceBuilder.build(DLAppService.class);
		long repositoryId = props.getGroupId();
		long folderId = DLAppServiceTest.PARENT_FOLDER_ID;

		InputStream is = FileUploadTest.class.getResourceAsStream(
			"/" + FILE_NAME);

		FileProgressCallback callback = new FileProgressCallback() {

			@Override
			public void onProgress(int totalBytes) {
			}

		};

		UploadData data = new UploadData(is, MIME_TYPE, FILE_NAME, callback);

		JSONObject file = service.addFileEntry(
			repositoryId, folderId, FILE_NAME, MIME_TYPE, FILE_NAME, "", "",
			data, null).execute(Config.global());

		assertEquals(FILE_NAME, file.getString(DLAppServiceTest.TITLE));
		assertEquals(372434, callback.getTotal());

		return file;
	}

	public FileUploadTest() throws IOException {
		super();
	}

	@Test
	public void addFileEntry() throws Exception {
		DLAppService service = ServiceBuilder.build(DLAppService.class);
		long repositoryId = props.getGroupId();
		long folderId = DLAppServiceTest.PARENT_FOLDER_ID;
		String fileName = DLAppServiceTest.SOURCE_FILE_NAME;
		String mimeType = DLAppServiceTest.MIME_TYPE;

		InputStream is = new ByteArrayInputStream(
			"Hello".getBytes(StandardCharsets.UTF_8));

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		FileProgressCallback callback = new FileProgressCallback() {

			@Override
			public void onBytes(byte[] bytes) {
				try {
					baos.write(bytes);
				}
				catch (IOException ioe) {
					fail(ioe.getMessage());
				}
			}

			@Override
			public void onProgress(int totalBytes) {
				if (totalBytes == 5) {
					try {
						baos.flush();
					}
					catch (IOException ioe) {
						fail(ioe.getMessage());
					}
				}
			}

		};

		UploadData data = new UploadData(is, mimeType, fileName, callback);

		Call<JSONObject> call = service.addFileEntry(
			repositoryId, folderId, fileName, mimeType, fileName, "", "", data,
			null);

		file = call.execute();

		assertEquals(fileName, file.getString(DLAppServiceTest.TITLE));
		assertEquals(5, callback.getTotal());
		assertEquals(5, baos.size());
	}

	@Test
	public void addFileEntryAsync() throws Exception {
		DLAppService service = ServiceBuilder.build(DLAppService.class);
		long repositoryId = props.getGroupId();
		long folderId = DLAppServiceTest.PARENT_FOLDER_ID;
		String fileName = DLAppServiceTest.SOURCE_FILE_NAME;
		String mimeType = DLAppServiceTest.MIME_TYPE;

		InputStream is = new ByteArrayInputStream(
			"Hello".getBytes(StandardCharsets.UTF_8));

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		FileProgressCallback progressCallback = new FileProgressCallback() {

			@Override
			public void onBytes(byte[] bytes) {
				try {
					baos.write(bytes);
				}
				catch (IOException ioe) {
					fail(ioe.getMessage());
				}
			}

			@Override
			public void onProgress(int totalBytes) {
				if (totalBytes == 5) {
					try {
						baos.flush();
					}
					catch (IOException ioe) {
						fail(ioe.getMessage());
					}
				}
			}

		};

		UploadData data = new UploadData(
			is, mimeType, fileName, progressCallback);

		Call<JSONObject> call = service.addFileEntry(
			repositoryId, folderId, fileName, mimeType, fileName, "", "", data,
			null);

		final CountDownLatch lock = new CountDownLatch(1);

		TestCallback<JSONObject> callback = new TestCallback<>(lock);
		call.async(callback);

		await(lock);
		this.file = callback.result();
		assertEquals(fileName, file.getString(DLAppServiceTest.TITLE));
		assertEquals(5, progressCallback.getTotal());
		assertEquals(5, baos.size());
	}

	@Test
	public void cancel() throws Exception {
		DLAppService service = ServiceBuilder.build(DLAppService.class);

		long repositoryId = props.getGroupId();
		long folderId = DLAppServiceTest.PARENT_FOLDER_ID;

		InputStream is = getClass().getResourceAsStream("/" + FILE_NAME);

		final FileProgressCallback callback = new FileProgressCallback() {

			@Override
			public void onProgress(int totalBytes) {
				if (totalBytes > 2048) {
					setCancelled(true);
				}
			}

		};

		UploadData data = new UploadData(is, MIME_TYPE, FILE_NAME, callback);

		try {
			file = service.addFileEntry(
				repositoryId, folderId, FILE_NAME, MIME_TYPE, FILE_NAME, "", "",
				data, null).execute();

			fail("Should have thrown IOException");
		}
		catch (IOException ioe) {
			assertTrue(ioe.getMessage().contains("Socket closed"));
		}

		assertEquals(2048 * 2, callback.getTotal());
	}

	@After
	public void tearDown() throws Exception {
		if (file != null) {
			DLAppServiceTest test = new DLAppServiceTest();
			test.deleteFileEntry(file.getLong(DLAppServiceTest.FILE_ENTRY_ID));
			file = null;
		}
	}

	@Test
	public void uploadPhoto() throws Exception {
		file = uploadPhoto(props);
	}

	protected JSONObject file;

}