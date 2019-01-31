package com.ap.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ap.json.JsonComparer.compareJsonNodes;
import static com.ap.json.JsonComparer.readFileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonComparerTest {


    @Test
    public void sameJsonWithLessAttributes() throws Exception {
        compareWithExpectedResult("sameWithMissingElementsInArray.json", "complete.json", true);
    }

    @Test
    public void sameWithOtherOrderOfElements() throws Exception {
        compareWithExpectedResult("sameWithOtherOrderOfElements.json", "complete.json", true);
    }

    @Test
    public void sameWithoutArray() throws Exception {
        compareWithExpectedResult("sameWithoutArray.json", "complete.json", true);
    }

    @Test
    public void differentValueNode() throws Exception {
        compareWithExpectedResult("differentValueNode.json", "complete.json", false);
    }

    @Test
    public void differentWithExtraAray() throws Exception {
        compareWithExpectedResult("differentWithExtraAray.json", "complete.json", false);
    }

    private void compareWithExpectedResult(String expected, String actual, boolean matches) throws Exception {
        List<String> differences = new ArrayList<>();
        JsonNode expectedNode = new ObjectMapper().readTree(readFileAsString(expected));
        JsonNode actualNode = new ObjectMapper().readTree(readFileAsString(actual));
        compareJsonNodes(expectedNode, actualNode, differences);

        assertEquals(matches, differences.isEmpty());
    }
}