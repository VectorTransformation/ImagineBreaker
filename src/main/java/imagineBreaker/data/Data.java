package imagineBreaker.data;

import imagineBreaker.system.ImagineBreaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Data {
    public static String dataFolderPath = ImagineBreaker.plugin.getDataFolder().getPath();
    public static Random random = new Random(System.currentTimeMillis());
    public static int MAXLENGTH = 32767;

    public static void loadAll() {
        load(Paths.get(dataFolderPath, "favicon"));
    }

    public static void load(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<String> faviconList(Path path) {
        load(Paths.get(dataFolderPath, "favicon"));
        try {
            return Files.walk(path)
                    .map(Path::toString)
                    .filter(p -> p.endsWith(".png"))
                    .toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static String pngToBase64(Path path) {
        load(Paths.get(dataFolderPath, "favicon"));
        try {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(Files.newInputStream(path).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String randomFavicon() {
        load(Paths.get(dataFolderPath, "favicon"));
        var faviconList = faviconList(Path.of(dataFolderPath, "favicon"));
        if (faviconList.isEmpty()) {
            return "data:image/png;base64,";
        } else {
            var path = faviconList.get(0);
            if (faviconList.size() > 1) {
                path = faviconList.get(random.nextInt(faviconList.size()));
            }
            return pngToBase64(Paths.get(path));
        }
    }

    public static String serverInfo(String json) {
        var result = json.substring(json.indexOf("\"favicon\":\"") + 11, json.length());
        result = result.substring(0, result.indexOf("\",\""));
        result = json.replace(result, Data.randomFavicon());
        if (result.length() > MAXLENGTH) {
            return json;
        }
        return result;
    }
}
