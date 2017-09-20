package libraries;

import android.net.Uri;
import android.support.v4.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ReSTRequest {

    protected String mEndpoint;
    protected String mMethod;
    protected ArrayMap<String, String> mParameters;
    protected ArrayMap<String, String> mFields;
    protected boolean mRawFields;
    protected JSONObject mRawBody = null;

    public static final int REST_REQUEST_METHOD_GET = 0;
    public static final int REST_REQUEST_METHOD_POST = 1;

    public static final int REST_REQUEST_QUERY_PARAMETERS = 0;
    public static final int REST_REQUEST_QUERY_FIELDS = 1;

    public ReSTRequest(int method, String endpoint) {

        switch (method) {
            case REST_REQUEST_METHOD_POST:
                mMethod = "POST";
            break;
            case REST_REQUEST_METHOD_GET:
            default:
                mMethod = "GET";
            break;
        }
        mEndpoint = endpoint;
        mParameters = new ArrayMap<String, String>();
        mFields = new ArrayMap<String, String>();
        mRawBody = null;
    }

    /**
     * Mehtod to add GET parameters
     * @param name: String parameter name
     * @param value: String parameter value
     **/
    public void addParameter(String name, String value) {
        mParameters.put(name, value);
    }

    /**
     * Mehtod to add POST parameters
     * @param name: String parameter name
     * @param value: String parameter value
     **/
    public void addField(String name, String value) {
        mFields.put(name, value);
    }

    /**
     * Method to set if the fields[POST] have to be send as JSONObject
     * This implementation it's because, some apis needs the information in this way
     * By default the values are send as form data
     * @param raw: boolean state
     **/
    public void setRawFields(boolean raw) {
        this.mRawFields = raw;
    }

    public void setRawBody(JSONObject body) {
        this.mRawBody = body;
    }

    public String buildQuery(int type) throws JSONException {

        String query = "";
        Uri.Builder builder = new Uri.Builder();
        ArrayMap<String, String> map = null;
        JSONObject body = new JSONObject();

        switch (type) {

            case REST_REQUEST_QUERY_PARAMETERS:

                map = mParameters;
                if (map != null && map.size() > 0) {
                    String name, value;
                    for (int i = 0; i < map.size(); i++) {
                        name = map.keyAt(i);
                        value = map.valueAt(i);
                        builder.appendQueryParameter(name, value);
                    }
                    query = builder.build().getEncodedQuery();
                }
                //Log.d("AB_DEV", "GET: " + query);

            break;

            case REST_REQUEST_QUERY_FIELDS:

                map = mFields;

                if (this.mRawBody == null) {
                    if (map != null && map.size() > 0) {

                        String name, value;
                        for (int i = 0; i < map.size(); i++) {

                            name = map.keyAt(i);
                            value = map.valueAt(i);
                            builder.appendQueryParameter(name, value);
                            body.put(name, value);
                        }

                        if (this.mRawFields) {
                            query = body.toString();
                        } else {
                            query = builder.build().getEncodedQuery();
                        }
                    }
                } else {
                    query = this.mRawBody.toString();
                }

                //Log.d("AB_DEV", "POST: " + query);
            break;
        }
        return query;
    }
}
