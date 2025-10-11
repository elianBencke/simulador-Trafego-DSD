package br.udesc.simulador.trafego.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadFileMesh {

    public static int[][] generateRoadMesh(File file) throws Exception {
        List<String> fileLines = Files.readAllLines(Path.of(file.getPath()));

        int meshRows = Integer.parseInt(fileLines.get(0));
        int meshColumns = Integer.parseInt(fileLines.get(1));
        int[][] roadMesh = new int[meshRows][meshColumns];

        fileLines = fileLines.subList(2, fileLines.size());

        for (int row = 0; row < fileLines.size(); row++) {
            String[] columns = fileLines.get(row).split("\t");
            for (int column = 0; column < columns.length; column++) {
                roadMesh[row][column] = Integer.parseInt(columns[column]);
            }
        }

        return roadMesh;
    }
}