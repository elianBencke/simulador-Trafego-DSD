package br.udesc.simulador.trafego.ui.component;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import br.udesc.simulador.trafego.model.piece.PieceModel;

public class TrafficPieceCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    private final Integer SQUARE_SIZE;

    public TrafficPieceCellRenderer(Integer squareSize) {
        this.SQUARE_SIZE = squareSize;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setIcon(value, row, column);
        this.defineColumnSize(table, column);
        return this;
    }

    private void setIcon(Object value, int row, int column) {
        if (value != null && value instanceof PieceModel) {
            PieceModel newPiece = (PieceModel) value;
            PieceIconRenderer customIcon = new PieceIconRenderer(newPiece);
            this.setIcon(customIcon);
        } else {
            this.setIcon(null);
        }
    }

    protected void defineColumnSize(JTable table, int column) {
        if (this.SQUARE_SIZE != null) {
            int columnSize = this.SQUARE_SIZE;
            TableColumnModel tableColumnModel = table.getColumnModel();
            TableColumn tableColumn = tableColumnModel.getColumn(column);
            tableColumn.setWidth(columnSize);
            tableColumn.setMinWidth(columnSize);
            tableColumn.setMaxWidth(columnSize);
            tableColumn.setPreferredWidth(columnSize);
        }
    }

}