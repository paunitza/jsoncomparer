package com.ap.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonComparer {

    public static void compareJsonNodes(JsonNode expectedNode, JsonNode actualNode, List<String> differences) throws Exception {

        Iterator<String> stringIterator = expectedNode.fieldNames();
        while (stringIterator.hasNext()) {
            String key = stringIterator.next();
            // System.out.println("Comparing " + key);
            JsonNode expected = expectedNode.get(key);

            JsonNode actual = actualNode != null ? actualNode.get(key) : null;
            if (expected.isValueNode()) {
                // compare the 2 value nodes
                boolean match = expected.asText() != null ? actual != null && expected.asText().equals(actual.asText()) : actual == null;
                if (!match) {
                    differences.add("For " + key + " expected: " + expected.textValue() + " but found: " + actual);
                }
            } else if (expected.isObject()) {
                compareJsonNodes(expected, actual, differences);
            } else if (expected.isArray()) {
                if (actual == null || !actual.isArray()) {
                    differences.add("Did not find and array correspondent for: " + key);
                    break;
                }
                for (int i = 0; i < expected.size(); i++) {
                    boolean found = false;
                    JsonNode expectedArrayItem = expected.get(i);
                    for (int j = 0; j < actual.size(); j++) {
                        JsonNode actualArrayItem = actual.get(j);
                        List<String> aa = new ArrayList<>();
                        compareJsonNodes(expectedArrayItem, actualArrayItem, aa);
                        if (aa.isEmpty()) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        continue;
                    }
                    differences.add("Mismatch in " + expectedArrayItem.toString());
                }
            }
        }
    }

    public static List<String> compareJsons(String expected, String actual) {
        try {
            JsonNode expectedNode = new ObjectMapper().readTree(expected);
            JsonNode actualNode = new ObjectMapper().readTree(actual);
            List<String> differences = new ArrayList<>();
            compareJsonNodes(expectedNode, actualNode, differences);
            return differences;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while comparing JSONs:", e);
        }
    }

    public static String readFileAsString(String fileName) {
        try {
            URI uri = Thread.currentThread().getContextClassLoader().getResource(fileName).toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(uri));
            return new String(bytes, Charset.defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
