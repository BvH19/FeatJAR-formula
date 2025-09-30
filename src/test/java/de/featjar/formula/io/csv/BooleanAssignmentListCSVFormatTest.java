/*
 * Copyright (C) 2025 FeatJAR-Development-Team
 *
 * This file is part of FeatJAR-formula.
 *
 * formula is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * formula is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-formula> for further information.
 */
package de.featjar.formula.io.csv;

import static org.junit.jupiter.api.Assertions.*;

import de.featjar.Common;
import de.featjar.base.data.Result;
import de.featjar.base.io.input.FileInputMapper;
import de.featjar.base.io.output.StreamOutputMapper;
import de.featjar.formula.assignment.BooleanAssignmentList;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BooleanAssignmentListCSVFormatTest extends Common {

    private final Charset charset = StandardCharsets.UTF_8;

    /*
    adds the normal file path, as well as a ".csv" file extension.
     */
    private String extendFilePath(String fileName) {
        return "src\\test\\resources\\csvTestData\\" + fileName + ".csv";
    }

    /*
    Reads input CSV file and returns it as byte array. Does not use FeatJAR methods.
     */
    private byte[] getInputStream(String inputFilePath) {
        File file = new File(inputFilePath);

        byte[] byteArray = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            byteArray = new byte[(int) file.length()];
            if (fis.read(byteArray) < 0) {
                throw new IOException("Incorrect file length");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return byteArray;
    }

    /*
    Uses FeatJAR's FileInputMapper to read an input CSV file and returns it as byte array
     */
    private byte[] getOutputStream(String inputFilePath) {
        BooleanAssignmentListCSVFormat csvObject = new BooleanAssignmentListCSVFormat();

        FileInputMapper fileInputMapper = null;
        try {
            fileInputMapper = new FileInputMapper(Path.of(inputFilePath), charset);

        } catch (java.io.IOException e) {
            System.out.println("Failed to create FileInputMapper.");
        }
        Result<BooleanAssignmentList> parseResult = csvObject.parse(fileInputMapper);

        // set up streamOutputMapper
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        StreamOutputMapper streamOutputMapper = new StreamOutputMapper(bAOS, charset);

        // write inputs to streamOutputMapper
        try {
            csvObject.write(parseResult.get(), streamOutputMapper);
        } catch (java.io.IOException e) {
            System.out.println("Failed to write parseResult to streamOutputMapper");
        }

        return bAOS.toByteArray(); // convert output stream to byte array
    }

    @Test
    void testMinimalFile() {
        String file = extendFilePath("minimal");

        byte[] inputStream = getInputStream(file);
        byte[] outputStream = getOutputStream(file);

        assertArrayEquals(inputStream, outputStream);
    }

    @Test
    void testWrongFormat() {
        String file = extendFilePath("wrong_format");

        byte[] inputStream = getInputStream(file);
        byte[] outputStream = getOutputStream(file);

        assertFalse(Arrays.equals(inputStream, outputStream));
    }

    @Test
    void testMissingBlankLine() {
        String file = extendFilePath("no_blank");

        byte[] inputStream = getInputStream(file);
        byte[] outputStream = getOutputStream(file);

        assertFalse(Arrays.equals(inputStream, outputStream));
    }

    @Test
    void testEmptyFile() {
        String file = extendFilePath("empty");

        boolean noElementThrown = false;
        try {
            getOutputStream(file);
        } catch (java.util.NoSuchElementException e) {
            noElementThrown = true;
        }

        assertTrue(noElementThrown);
    }
}
