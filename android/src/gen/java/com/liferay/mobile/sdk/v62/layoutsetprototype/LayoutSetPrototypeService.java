package com.liferay.mobile.sdk.v62.layoutsetprototype;

import com.liferay.mobile.sdk.Call;
import com.liferay.mobile.sdk.annotation.Param;
import com.liferay.mobile.sdk.annotation.ParamObject;
import com.liferay.mobile.sdk.annotation.Path;
import com.liferay.mobile.sdk.http.Response;
import java.lang.String;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/layoutsetprototype")
public interface LayoutSetPrototypeService {
  @Path("/add-layout-set-prototype")
  Call<JSONObject> addLayoutSetPrototype(@ParamObject(name = "nameMap", className = "") JSONObject nameMap, @Param("description") String description, @Param("active") boolean active, @Param("layoutsUpdateable") boolean layoutsUpdateable, @ParamObject(name = "serviceContext", className = "com.liferay.portal.service.ServiceContext") JSONObject serviceContext);

  @Path("/delete-layout-set-prototype")
  Call<Response> deleteLayoutSetPrototype(@Param("layoutSetPrototypeId") long layoutSetPrototypeId);

  @Path("/get-layout-set-prototype")
  Call<JSONObject> getLayoutSetPrototype(@Param("layoutSetPrototypeId") long layoutSetPrototypeId);

  @Path("/search")
  Call<JSONArray> search(@Param("companyId") long companyId, @Param("active") boolean active, @ParamObject(name = "obc", className = "com.liferay.portal.kernel.util.OrderByComparator") JSONObject obc);

  @Path("/update-layout-set-prototype")
  Call<JSONObject> updateLayoutSetPrototype(@Param("layoutSetPrototypeId") long layoutSetPrototypeId, @Param("settings") String settings);

  @Path("/update-layout-set-prototype")
  Call<JSONObject> updateLayoutSetPrototype(@Param("layoutSetPrototypeId") long layoutSetPrototypeId, @ParamObject(name = "nameMap", className = "") JSONObject nameMap, @Param("description") String description, @Param("active") boolean active, @Param("layoutsUpdateable") boolean layoutsUpdateable, @ParamObject(name = "serviceContext", className = "com.liferay.portal.service.ServiceContext") JSONObject serviceContext);
}
