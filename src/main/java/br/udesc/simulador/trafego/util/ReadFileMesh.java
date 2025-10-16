package br.udesc.simulador.trafego.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadFileMesh {

    public static int[][] generateRoadMesh(File file) throws Exception {
        List<String> fileLines = Files.readAllLines(Path.of(file.getPath()));

<<<<<<< HEAD
        String rowStr = fileLines.get(0).trim();
        int meshRows = Integer.parseInt(rowStr);

        String colStr = fileLines.get(1).trim();
        int meshColumns = Integer.parseInt(colStr);

=======
        int meshRows = Integer.parseInt(fileLines.get(0).trim());
        int meshColumns = Integer.parseInt(fileLines.get(1).trim());
>>>>>>> 61bb0818a490d984dbc7207e373a287c52d53da6
        int[][] roadMesh = new int[meshRows][meshColumns];

        fileLines = fileLines.subList(2, fileLines.size());

        for (int row = 0; row < fileLines.size(); row++) {
            String[] columns = fileLines.get(row).trim().split("\\s+");

            for (int column = 0; column < columns.length; column++) {
<<<<<<< HEAD
                String cellValue = columns[column].trim();

                if (!cellValue.isEmpty()) {
                    try {
                        roadMesh[row][column] = Integer.parseInt(cellValue);
                    } catch (NumberFormatException e) {
                        throw new Exception("O valor '" + cellValue + "' não é um inteiro válido na malha.", e);
                    }
=======
                if (columns[column].isEmpty()) {
                    continue;
                }
                if (column < meshColumns) {
                    roadMesh[row][column] = Integer.parseInt(columns[column]);
>>>>>>> 61bb0818a490d984dbc7207e373a287c52d53da6
                }
            }
        }

        return roadMesh;
    }
}