import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scraper {
    private static final String INPUT_DIR = "/home/teinvdlugt/Documents/Russian Scraper/input/";
    private static final String OUTPUT_DIR = "/home/teinvdlugt/Documents/Russian Scraper/output/";

    public static void main(String[] args) {
        String inputFile = INPUT_DIR + "words.txt";
        List<String> words = getWordsArray(inputFile);
        List<String> failedWords = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            if (new File(OUTPUT_DIR + words.get(i) + ".ogg").exists())
                continue;

            String fileURL = findOggFile(words.get(i));
            if (fileURL != null) {
                System.out.println((i + 1) + " of " + words.size());
                downloadURLToFile(fileURL, "/home/teinvdlugt/Documents/" +
                        "Russian Scraper/output/" + words.get(i) + ".ogg");
            } else {
                failedWords.add(words.get(i));
            }
        }

        writeFailedWordsToFile(failedWords,
                "/home/teinvdlugt/Documents/Russian Scraper/output/log.txt");
    }

    private static List<String> getWordsArray(String inputFilePath) {
        List<String> words = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null)
                if (!line.isEmpty() && !line.startsWith("#"))
                    words.add(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Input file not found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't read input file");
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {/*ignored*/}
        }

        return words;
    }

    private static String findOggFile(String word) {
        String webPage = getWebPage("https://ru.wiktionary.org/wiki/" + word); // can also be https://en.wiktionary.org/...
        if (webPage == null) {
            System.out.println("Found no web page for " + word);
            return null;
        }

        // Only of use when in the English wiktionary version:
        /*int russianStartIndex = webPage.indexOf("id=\"Russian\"");
        if (russianStartIndex == -1) {
            System.out.println("Found no Russian segment in the wegpage for " + word);
        }*/

        int startIndex = webPage.indexOf("<source src="/*, russianStartIndex*/) + 13; // Index of the first character of the .ogg file path

        if (startIndex == 12) { // This means the indexOf function returned -1
            System.out.println("Found no audio file for " + word);
            return null;
        }

        int endIndex = webPage.indexOf("\"", startIndex + 14) - 1; // Index of the last character of the .ogg file path

        if (endIndex == -1) {
            System.out.println("Found no \"\"\" in the web page for " + word);
            return null;
        }

        String fileURL = webPage.substring(startIndex, endIndex + 1);

        if (!fileURL.substring(fileURL.length() - 4).equals(".ogg")) {
            System.out.println("The sound file of " + word + " wasn't a .ogg file");
        }

        return "https:" + fileURL;
    }

    private static String getWebPage(String urlStr) {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            is = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder file = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                file.append(line).append("\n");
            }
            return file.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {/*ignored*/}
        }
    }

    private static void downloadURLToFile(String urlStr, String filePath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            // Input: URL and HTTP connection
            URL url = new URL(urlStr);
            is = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // Output: Local file
            File file = new File(filePath);
            fos = new FileOutputStream(file);

            int currentByte;
            // String line;
            while ((currentByte = is.read()) != -1) {
                fos.write(currentByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't fetch url " + urlStr +
                    " or couldn't save file " + filePath);
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {/*ignored*/}
        }
    }

    private static void writeFailedWordsToFile(List<String> failedWords, String filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(("These " + failedWords.size() + " words couldn't be found:\n").getBytes());
            for (String word : failedWords) {
                fos.write((word + "\n").getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Couldn't save log file");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {/*ignored*/}
        }
    }
}