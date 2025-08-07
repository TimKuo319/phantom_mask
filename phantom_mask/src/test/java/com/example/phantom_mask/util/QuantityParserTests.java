package com.example.phantom_mask.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityParserTests {
    @Test
    void testParseQuantityWithNumber() {
        assertEquals(3, QuantityParser.parseQuantity("True Barrier (green) (3 per pack)"));
        assertEquals(10, QuantityParser.parseQuantity("MaskT (green) (10 per pack)"));
        assertEquals(6, QuantityParser.parseQuantity("Masquerade (blue) (6 per pack)"));
        assertEquals(1, QuantityParser.parseQuantity("MaskT (green)"));
    }

    @Test
    void testParseQuantityWithoutNumber() {
        assertEquals(1, QuantityParser.parseQuantity("Cotton Kiss (blue)"));
        assertEquals(1, QuantityParser.parseQuantity("Second Smile (black)"));
    }

    @Test
    void testParseQuantityVariousCases() {
        assertEquals(3, QuantityParser.parseQuantity("Second Smile (black) (3 per pack)"));
        assertEquals(10, QuantityParser.parseQuantity("Cotton Kiss (green) (10 per pack)"));
        assertEquals(6, QuantityParser.parseQuantity("True Barrier (blue) (6 per pack)"));
        assertEquals(1, QuantityParser.parseQuantity("Masquerade (black)"));
    }
}

