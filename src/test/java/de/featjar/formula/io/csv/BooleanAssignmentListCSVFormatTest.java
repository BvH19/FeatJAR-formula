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
import de.featjar.base.io.output.FileOutputMapper;
import de.featjar.base.io.output.StreamOutputMapper;
import de.featjar.formula.assignment.BooleanAssignmentList;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BooleanAssignmentListCSVFormatTest extends Common {

    private final String general_path = "src\\test\\resources\\csvTestData\\";

    private final String[] csvFiles =
            new String[] {"minimal_csv.csv", "wrong_format.csv", "no_blank.csv", "empty_csv.csv"};

    private final Charset charset = StandardCharsets.UTF_8;

    private String fetchCSVFile(int csvIndex) {
        return general_path + csvFiles[csvIndex];
    }

    /*
    Reads input CSV file and returns it as byte array
     */
    public byte[] byteStreamReader(int csvIndex) {
        File file = new File(fetchCSVFile(csvIndex));

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

    public void writeOutputFile(BooleanAssignmentListCSVFormat csvObject, Result<BooleanAssignmentList> parseResult) {
        FileOutputMapper fileOutputMapper = null;
        try {
            fileOutputMapper = new FileOutputMapper(
                    Path.of(
                            "D:\\Indigo\\TUBS\\ISF Teamprojekt\\git\\FeatJAR\\formula\\src\\test\\java\\de\\featjar\\formula\\io\\csv\\testcsv-OUTPUT.csv"),
                    charset);
        } catch (java.io.IOException e) {
            System.out.println("Caught");
        }

        if (fileOutputMapper == null) {
            System.out.println("Failed to intitialize FileOutputMapper");
            return;
        }

        try {
            csvObject.write(parseResult.get(), fileOutputMapper);
        } catch (java.io.IOException e) {
            System.out.println("Failed to write parseResult to fileOutputMapper");
        }

        try {
            fileOutputMapper.close();
        } catch (java.io.IOException e) {
            System.out.println("Failed to close fileOutputMapper");
        }
    }

    @Test
    void actualTest() {

        for (int csvIndex = 0; csvIndex < this.csvFiles.length; csvIndex++) {
            someTest(csvIndex);
            System.out.println("Test Complete " + csvIndex);
        }
    }

    void getInputStream(int csvIndex) {
        BooleanAssignmentListCSVFormat csvObject = new BooleanAssignmentListCSVFormat();

        // start parsing input CSV
        byte[] byteInput = byteStreamReader(csvIndex);

        if (csvIndex == 3) {
            assertEquals(0, byteInput.length);
        }

        FileInputMapper fileInputMapper = null;
        try {
            fileInputMapper = new FileInputMapper(Path.of(fetchCSVFile(csvIndex)), charset);

        } catch (java.io.IOException e) {
            System.out.println("Caught");
        }

        Result<BooleanAssignmentList> parseResult = csvObject.parse(fileInputMapper);
    }

    void someTest(int csvIndex) {

        BooleanAssignmentListCSVFormat csvObject = new BooleanAssignmentListCSVFormat();

        // start parsing input CSV
        byte[] byteInput = byteStreamReader(csvIndex);

        if (csvIndex == 3) {
            assertEquals(0, byteInput.length);
        }

        FileInputMapper fileInputMapper = null;
        try {
            fileInputMapper = new FileInputMapper(Path.of(fetchCSVFile(csvIndex)), charset);

        } catch (java.io.IOException e) {
            System.out.println("Caught");
        }

        Result<BooleanAssignmentList> parseResult = csvObject.parse(fileInputMapper);

        // hand over parse result to output mapper
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        StreamOutputMapper streamOutputMapper = new StreamOutputMapper(baos, charset);

        // write inputs to outputmapper
        try {
            csvObject.write(parseResult.get(), streamOutputMapper);
        } catch (java.io.IOException e) {
            System.out.println("Failed to write parseresult to streamoutputmapper");
        }

        byte[] byteOutput = baos.toByteArray(); // convert output stream to byte array

        // Test if output stream equals input stream

        if (csvIndex == 0) {
            assertArrayEquals(byteInput, byteOutput);
        } else {
            assertFalse(Arrays.equals(byteInput, byteOutput));
        }
        

    }
}
