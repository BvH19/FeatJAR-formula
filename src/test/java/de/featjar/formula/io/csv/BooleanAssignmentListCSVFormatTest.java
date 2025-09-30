package de.featjar.formula.io.csv;

import java.io.*;

import de.featjar.Common;
import de.featjar.base.data.Result;
import de.featjar.base.io.output.FileOutputMapper;
import de.featjar.base.io.output.StreamOutputMapper;
import de.featjar.formula.assignment.BooleanAssignmentList;
import org.junit.jupiter.api.Test;
import de.featjar.base.io.input.FileInputMapper;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


public class BooleanAssignmentListCSVFormatTest extends Common {

    final private String minimal_CSV_path = "src\\test\\resources\\csvTestData\\minimal_csv.csv";
    final private Charset charset = StandardCharsets.UTF_8;

    /*
    Reads input CSV file and returns it as byte array
     */
    public byte[] byteStreamReader() {
        File file = new File(this.minimal_CSV_path);

        byte[] byteArray = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            byteArray = new byte[(int) file.length()];
            if (fis.read(byteArray) <= 0) {
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
                    Path.of("D:\\Indigo\\TUBS\\ISF Teamprojekt\\git\\FeatJAR\\formula\\src\\test\\java\\de\\featjar\\formula\\io\\csv\\testcsv-OUTPUT.csv"),
                    charset
            );
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
    void someTest() {
        BooleanAssignmentListCSVFormat csvObject = new BooleanAssignmentListCSVFormat();

        // start parsing input CSV
        byte[] byteInput = byteStreamReader();
        FileInputMapper fileInputMapper = null;
        try {
            fileInputMapper = new FileInputMapper(
                    Path.of(this.minimal_CSV_path),
                    charset
            );

        } catch (java.io.IOException e) {
            System.out.println("Caught");
        }

        Result<BooleanAssignmentList> parseResult = csvObject.parse(fileInputMapper);

        // hand over parse result to output mapper
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        StreamOutputMapper streamOutputMapper = new StreamOutputMapper(
                baos,
                charset
        );

        // write inputs to outputmapper
        try {
            csvObject.write(parseResult.get(), streamOutputMapper);
        } catch (java.io.IOException e) {
            System.out.println("Failed to write parseresult to streamoutputmapper");
        }

        byte[] byteOutput = baos.toByteArray(); // convert output stream to byte array

        // Test if output stream equals input stream

        assertArrayEquals(byteInput, byteOutput);


    }
}
