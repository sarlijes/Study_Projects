
import static spark.Spark.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import com.google.common.hash.Hashing;

public class SparkTest {

    public static void main(String[] args) {
        post("/hash", (req, res) -> {

            // Validate the request input
            if (!req.body().startsWith("{\"text\": \"")) {
                res.status(400);
                return "Error 400: Bad request";
            }

            // If validation passes, return the hash:
            res.status(200);
            res.type("application/json");
            res.body(hashTheText(req.body()));
            return "{\"text\":\"" + hashTheText(req.body()) + "\"}";

        });
    }

    public static String hashTheText(String originalString) throws NoSuchAlgorithmException {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }
}
