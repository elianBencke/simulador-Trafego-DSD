package br.udesc.simulador.trafego.model.singleton;

import java.util.ArrayList;
import java.util.List;

import br.udesc.simulador.trafego.model.GlobalConstants;
import br.udesc.simulador.trafego.model.factory.NodeFactory;
import br.udesc.simulador.trafego.model.node.AbstractNode;
import br.udesc.simulador.trafego.model.observer.ObserverNode;
import br.udesc.simulador.trafego.model.piece.*;

public class MeshRepository {
    private static MeshRepository instance;
    private int[][] roadMesh;
    private AbstractNode[][] nodeMesh;
    private List<AbstractNode> entranceNodes;
    private PieceModel[][] pieces;
    private NodeFactory factory;

    private MeshRepository() {
        entranceNodes = new ArrayList<>();
    }

    public synchronized static MeshRepository getInstance(){
        if (instance == null) {
            instance = new MeshRepository();
        }
        return instance;
    }

    public void setRoadMesh(int[][] mesh) {
        this.roadMesh = mesh;
    }

    public int[][] getRoadMesh(){
        return this.roadMesh;
    }

    public int getRowSize() {
        return roadMesh.length;
    }

    public int getColumnSize() {
        return roadMesh[0].length;
    }

    public List<AbstractNode> getEntranceNodes(){
        return this.entranceNodes;
    }

    public void addEntranceNode(AbstractNode node) {
        entranceNodes.add(node);
    }

    public NodeFactory getFactory() {
        return factory;
    }

    public void setFactory(NodeFactory factory) {
        this.factory = factory;
    }

    public AbstractNode[][] createAndLinkNodeMesh(ObserverNode observer) {
        AbstractNode[][] nodeMesh = new AbstractNode[roadMesh.length][roadMesh[0].length];
        NodeFactory nodeFactory = this.getFactory();

        for (int i = 0; i < roadMesh.length; i++) {
            for (int j = 0; j < roadMesh[0].length; j++) {
                int typeRoad = roadMesh[i][j];
                AbstractNode node = null;
                if (typeRoad >= 1 && typeRoad <= 4) {
                    node = nodeFactory.createNode(i, j, typeRoad, observer);
                } else if (typeRoad >= 5 && typeRoad <= 12) {
                    node = nodeFactory.createCrossNode(i, j, typeRoad, observer);
                }
                nodeMesh[i][j] = node;
            }
        }

        this.nodeMesh = nodeMesh;

        for (int row = 0; row < roadMesh.length; row++) {
            for (int column = 0; column < roadMesh[0].length; column++) {
                int typeRoad = roadMesh[row][column];
                switch (typeRoad) {
                    case GlobalConstants.UP:
                    case GlobalConstants.CROSSING_UP: {
                        setNextNodeUp(row, column, row-1, column);
                        break;
                    }
                    case GlobalConstants.RIGHT:
                    case GlobalConstants.CROSSING_RIGHT: {
                        setNextNodeRight(row, column, row, column+1);
                        break;
                    }
                    case GlobalConstants.DOWN:
                    case GlobalConstants.CROSSING_DOWN: {
                        setNextNodeDown(row, column, row+1, column);
                        break;
                    }
                    case GlobalConstants.LEFT:
                    case GlobalConstants.CROSSING_LEFT: {
                        setNextNodeLeft(row, column, row, column-1);
                        break;
                    }
                    case GlobalConstants.CROSSING_DOWN_LEFT: {
                        setNextNodeDown(row, column, row+1, column);
                        setNextNodeLeft(row, column, row, column-1);
                        break;
                    }
                    case GlobalConstants.CROSSING_RIGHT_DOWN: {
                        setNextNodeRight(row, column, row, column+1);
                        setNextNodeDown(row, column, row+1, column);
                        break;
                    }
                    case GlobalConstants.CROSSING_UP_LEFT: {
                        setNextNodeUp(row, column, row-1, column);
                        setNextNodeLeft(row, column, row, column-1);
                        break;
                    }
                    case GlobalConstants.CROSSING_UP_RIGHT: {
                        setNextNodeUp(row, column, row-1, column);
                        setNextNodeRight(row, column, row, column+1);
                        break;
                    }
                }
            }
        }
        return nodeMesh;
    }

    private boolean canMoveLeft(int column) {
        return column > 0;
    }

    private boolean canMoveRight(int column) {
        return column < roadMesh[0].length-1;
    }

    private boolean canMoveUp(int row) {
        return row > 0;
    }

    private boolean canMoveDown(int row) {
        return row < roadMesh.length-1;
    }

    private void setNextNodeRight(int row, int column, int nextRow, int nextColumn) {
        if(canMoveRight(column)) {
            nodeMesh[row][column].setMoveRight(nodeMesh[nextRow][nextColumn]);
        }
    }

    private void setNextNodeLeft(int row, int column, int nextRow, int nextColumn) {
        if(canMoveLeft(column)) {
            nodeMesh[row][column].setMoveLeft(nodeMesh[nextRow][nextColumn]);
        }
    }

    private void setNextNodeUp(int row, int column, int nextRow, int nextColumn) {
        if(canMoveUp(row)) {
            nodeMesh[row][column].setMoveUp(nodeMesh[nextRow][nextColumn]);
        }
    }

    private void setNextNodeDown(int row, int column, int nextRow, int nextColumn) {
        if(canMoveDown(row)) {
            nodeMesh[row][column].setMoveDown(nodeMesh[nextRow][nextColumn]);
        }
    }

    public PieceModel[][] initializePieces() {
        int row = roadMesh.length;
        int column = roadMesh[0].length;
        this.pieces = new PieceModel[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                pieces[i][j] = createPiece(roadMesh[i][j]);
            }
        }
        return pieces;
    }

    public PieceModel[][] getPieces(){
        return pieces;
    }

    private PieceModel createPiece(int type) {
        switch (type) {
            case GlobalConstants.UP: return new RoadUpPiece(type);
            case GlobalConstants.RIGHT: return new RoadRightPiece(type);
            case GlobalConstants.DOWN: return new RoadDownPiece(type);
            case GlobalConstants.LEFT: return new RoadLeftPiece(type);
            case GlobalConstants.CROSSING_DOWN: return new CrossingDownPiece(type);
            case GlobalConstants.CROSSING_UP: return new CrossingUpPiece(type);
            case GlobalConstants.CROSSING_LEFT: return new CrossingLeftPiece(type);
            case GlobalConstants.CROSSING_RIGHT: return new CrossingRightPiece(type);
            case GlobalConstants.CROSSING_DOWN_LEFT: return new CrossingDownLeftPiece(type);
            case GlobalConstants.CROSSING_RIGHT_DOWN: return new CrossingRightDownPiece(type);
            case GlobalConstants.CROSSING_UP_LEFT: return new CrossingUpLeftPiece(type);
            case GlobalConstants.CROSSING_UP_RIGHT: return new CrossingUpRightPiece(type);
            default:
                return null;
        }
    }
}