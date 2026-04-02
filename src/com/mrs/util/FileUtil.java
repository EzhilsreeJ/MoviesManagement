package com.mrs.util;

import com.mrs.model.Movie;
import com.mrs.exception.FileHandlingException;

import java.io.*;
import java.util.*;

public class FileUtil {

    public static List<Movie> loadMovies(String path) {
        List<Movie> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {


                if (line.trim().isEmpty()) continue;

                String[] d = line.split(",");


                if (d.length < 4) {
                    System.out.println(" Skipping invalid row: " + line);
                    continue;
                }

                try {
                    list.add(new Movie(
                            d[0].trim(),
                            d[1].trim(),
                            Double.parseDouble(d[2].trim()),
                            Integer.parseInt(d[3].trim())
                    ));
                } catch (Exception e) {
                    System.out.println(" Skipping corrupt row: " + line);
                }
            }

        } catch (Exception e) {
            throw new FileHandlingException("File Error: " + e.getMessage());
        }

        return list;
    }
}