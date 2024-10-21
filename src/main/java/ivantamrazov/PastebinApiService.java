package ivantamrazov;

import com.esotericsoftware.kryo.kryo5.minlog.Log;
import groovy.util.logging.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class PastebinApiService {

    //Let's take my API key as an example
    private final String API_DEV_KEY = "YVqsovuMFbJ3FCxJaC97zdqMcj9gmm0v";

    public String uploadCodeToPastebin(String code) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType,
                "api_dev_key=" + API_DEV_KEY + "&api_option=paste&api_paste_code=" + code);

        Request request = new Request.Builder()
                .url("https://pastebin.com/api/api_post.php")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            Log.info("Paste created successfully!");
            return responseBody;
        } else {
            System.out.println("Error creating paste: " + response.code() + " " + response.body().string());
        }

        return code;
    }

}
