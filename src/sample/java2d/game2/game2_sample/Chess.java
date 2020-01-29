package sample.java2d.game2.game2_sample;

import javafx.scene.SubScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.java2d.game2.game2_sample.game2_pieces.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Chess {

    private final Image board;
    private final double oneFieldSize, surroundingGap;
    public List<Piece> pieces;
    private PieceColor turn;
    public boolean moved = false;
    private int selectedPiece = -1;
    private boolean gameOver = false;

    public Chess(Image image) {
        board = image;
        double fieldSize = 755 - 45;
        oneFieldSize = fieldSize / 8;
        surroundingGap = (game2_Main.SIZE - fieldSize) / 2;
    }

    public void initialize(GraphicsContext gc) {
        pieces = new ArrayList<>();
        pieces.add(new King(5, 8, PieceColor.white, this));
        pieces.add(new Queen(4, 8, PieceColor.white, this));
        pieces.add(new Bishop(3, 8, PieceColor.white, this));
        pieces.add(new Bishop(6, 8, PieceColor.white, this));
        pieces.add(new Knight(2, 8, PieceColor.white, this));
        pieces.add(new Knight(7, 8, PieceColor.white, this));
        pieces.add(new Rook(8, 8, PieceColor.white, this));
        pieces.add(new Rook(1, 8, PieceColor.white, this));
        for (int i = 1; i < 9; i++) {
            pieces.add(new Pawn(i, 7, PieceColor.white, this));
        }
        pieces.add(new King(5, 1, PieceColor.black, this));
        pieces.add(new Queen(4, 1, PieceColor.black, this));
        pieces.add(new Bishop(3, 1, PieceColor.black, this));
        pieces.add(new Bishop(6, 1, PieceColor.black, this));
        pieces.add(new Knight(2, 1, PieceColor.black, this));
        pieces.add(new Knight(7, 1, PieceColor.black, this));
        pieces.add(new Rook(8, 1, PieceColor.black, this));
        pieces.add(new Rook(1, 1, PieceColor.black, this));
        for (int i = 1; i < 9; i++) {
            pieces.add(new Pawn(i, 2, PieceColor.black, this));
        }

        selectedPiece = -1;
        gameOver = false;
        turn = PieceColor.white;

        gc.drawImage(board, 0, 0, 800, 800);
        pieces.stream().forEach(piece -> gc.drawImage(piece.image, getChessFieldPixel(piece.x), getChessFieldPixel(piece.y), oneFieldSize, oneFieldSize));
    }

    public void movePiece(double x, double y, GraphicsContext gc) {
        if (selectedPiece == -1) {
            selectPiece(x, y);


            if (selectedPiece != -1) {
                pieces.stream().filter(piece -> piece == pieces.get(selectedPiece)).forEach(piece -> gc.drawImage(piece.image, getChessFieldPixel(piece.x), getChessFieldPixel(piece.y), oneFieldSize, oneFieldSize));
                pieces.stream().filter(piece -> piece != pieces.get(selectedPiece)).forEach(piece -> gc.drawImage(piece.image, getChessFieldPixel(piece.x), getChessFieldPixel(piece.y), oneFieldSize, oneFieldSize));
            }

            selectedPiece = -1;
            return;
        }

        if (!gameOver) {
            pieces.get(selectedPiece).move(getChessFieldValue(x), getChessFieldValue(y));
            if (moved) {
                pieces.get(selectedPiece).chuck();
                moved = false;
                pieces.addAll(pieces.stream().filter(piece -> piece.getClass() == Pawn.class && (piece.y == 8 || piece.y == 1)).map(this::promotion).collect(Collectors.toList()));
                pieces.removeIf(piece -> piece.getClass() == Pawn.class && (piece.y == 8 || piece.y == 1));
                pieces.stream().filter(piece -> piece.getClass() == King.class).map(piece -> (King) piece).filter(King::checkChess).forEach(this::chess);
                selectedPiece = -1;
                turn = PieceColor.swapColor(turn);
            } else
                selectedPiece = -1;
            gc.drawImage(board, 0, 0, 800, 800);
            pieces.stream().forEach(piece -> gc.drawImage(piece.image, getChessFieldPixel(piece.x), getChessFieldPixel(piece.y), oneFieldSize, oneFieldSize));
        }
    }

    public void movePieceUpdate(double x, double y, GraphicsContext gc) {
        if (!gameOver)
            if (selectedPiece == -1) {
                selectPiece(x, y);
            } else {
                gc.drawImage(board, 0, 0, 800, 800);
                pieces.stream().filter(piece -> pieces.get(selectedPiece) != piece).forEach(piece -> gc.drawImage(piece.image, getChessFieldPixel(piece.x), getChessFieldPixel(piece.y), oneFieldSize, oneFieldSize));
                pieces.stream().filter(piece -> pieces.get(selectedPiece) == piece).forEach(piece -> gc.drawImage(piece.image, x - oneFieldSize / 2, y - oneFieldSize / 2, oneFieldSize, oneFieldSize));
            }
    }


    //moves
    private void chess(King king) {
        if (king.pieceColor == turn) {
            gameOver = true;
            if (turn == PieceColor.white)
                game2_AlertBox.display("Black wins.");
            else
                game2_AlertBox.display("White wins.");
        } else
            game2_AlertBox.display("Check.");
    }

    private Piece promotion(Piece pawn) {
        return game2_AlertBox.display(pawn, this);
    }

    //game2_pieces
    private void selectPiece(double x, double y) {
        for (int i = 0; i < pieces.size(); i++)
            if (pieces.get(i).x == getChessFieldValue(x) && pieces.get(i).y == getChessFieldValue(y) && pieces.get(i).pieceColor == turn)
                selectedPiece = i;
    }

    public Piece getPiece(int x, int y) {
        return pieces.stream().filter(piece -> piece.x == x && piece.y == y).reduce(null, (piece1, piece2) -> piece2);
    }

    //board
    private double getChessFieldPixel(int var) {
        if (var > 8 || var < 1)
            return 0;
        return oneFieldSize * (var - 1) + surroundingGap;
    }

    private int getChessFieldValue(double var) {
        if (var < surroundingGap || var > game2_Main.SIZE - surroundingGap)
            return 0;
        return (int) Math.floor((var - surroundingGap) / oneFieldSize) + 1;
    }
}
