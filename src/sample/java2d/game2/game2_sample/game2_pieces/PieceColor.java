package sample.java2d.game2.game2_sample.game2_pieces;

public enum PieceColor {
    white, black;


    public static PieceColor swapColor(PieceColor PieceColor){
        switch (PieceColor){
            case white:
                return black;
            case black:
                return white;
            default:
                throw new IllegalStateException();
        }
    }
}
