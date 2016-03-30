package com.liferay.mobile.sdk.v62.mbban;

import com.liferay.mobile.sdk.Call;
import com.liferay.mobile.sdk.annotation.Param;
import com.liferay.mobile.sdk.annotation.ParamObject;
import com.liferay.mobile.sdk.annotation.Path;
import com.liferay.mobile.sdk.http.Response;
import org.json.JSONObject;

@Path("/mbban")
public interface MBBanService {
  @Path("/add-ban")
  Call<JSONObject> addBan(@Param("banUserId") long banUserId, @ParamObject(name = "serviceContext", className = "com.liferay.portal.service.ServiceContext") JSONObject serviceContext);

  @Path("/delete-ban")
  Call<Response> deleteBan(@Param("banUserId") long banUserId, @ParamObject(name = "serviceContext", className = "com.liferay.portal.service.ServiceContext") JSONObject serviceContext);
}