package name.trifon.example.web.rest.common;

import org.springframework.http.MediaType;

import java.util.Map;

//@Trifon
public class RestMediaType extends MediaType {

	private static final long serialVersionUID = 1L;

	public static final RestMediaType APPLICATION_MERGE_PATCH_JSON;
	public static final RestMediaType APPLICATION_PATCH_JSON;

	public static final String APPLICATION_MERGE_PATCH_JSON_VALUE = "application/merge-patch+json";
	public static final String APPLICATION_PATCH_JSON_VALUE = "application/json-patch+json";


	static {
		APPLICATION_MERGE_PATCH_JSON = new RestMediaType(valueOf(APPLICATION_MERGE_PATCH_JSON_VALUE));
		APPLICATION_PATCH_JSON = new RestMediaType(valueOf(APPLICATION_PATCH_JSON_VALUE));
	}


	public RestMediaType(MediaType type) {
		super(type, (Map<String, String>) null);
	}
}
