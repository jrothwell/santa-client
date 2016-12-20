import alexh.Fluent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class SantaClientApp {
    public static String MY_ID = "CHANGEME"; // TODO: change me to a valid string
    public static String TARGET_ADDRESS = "http://localhost:8080"; // TODO: change me to the server address

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        final Map parcel =
                OBJECT_MAPPER.readValue(
                        Request.Get(TARGET_ADDRESS + "/play/" + MY_ID + "/get-parcel")
                            .execute()
                            .returnContent().asString(),
                        Map.class);


        final String parcelId = (String) parcel.get("id");
        final String parcelLabel = (String) parcel.get("label");

        // TODO: make a more sensible decision here than...
        final String jsonAnswer = OBJECT_MAPPER.writeValueAsString(new Fluent.HashMap<String, Object>()
                .append("latitude", new Fluent.HashMap<String, Object>()
                        .append("degrees", 0)
                        .append("minutes", 0)
                        .append("direction", "NORTH"))
                .append("longitude", new Fluent.HashMap<String, Object>()
                        .append("degrees", 0)
                        .append("minutes", 0)
                        .append("direction", "WEST")));

        final String uri = TARGET_ADDRESS + "/play/" + MY_ID + "/parcels/" + parcelId;
        System.out.println("uri = " + uri);
        Request.Post(uri)
                .bodyString(jsonAnswer, ContentType.APPLICATION_JSON)
                .execute();
    }
}
