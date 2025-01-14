package ca.nerret.emu.processor.dbg.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 *  A UI component that displays a byte as a series of bits with an
 * identification name and Dec/Hex values displayed.
 *
 * @author Ross Drew
 */
class ByteBox extends JPanel {
    protected final int bitSize = 40;
    protected final int byteSize = (bitSize*8);
    protected final int bitFontSize = 40;
    protected final int valueFontSize = 11;

    protected int byteValue;
    protected String byteName;
    protected Color highlightColor;

    public ByteBox(String byteName, int initialValue){
        this.byteValue = initialValue;
        this.byteName = byteName;
        this.highlightColor = null;
    }

    public ByteBox(String byteName, int initialValue, Color highlightColor){
        this.byteValue = initialValue;
        this.byteName = byteName;
        this.highlightColor = highlightColor;
    }

    public int getByteValue(){
        return this.byteValue;
    }

    public void setValue(int newValue){
        this.byteValue = newValue;

        refresh();
    }

    private void refresh() {
        invalidate();
        revalidate();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        turnOnClearText(g);

        paintByte(g, new Point(0, bitFontSize), getByteValue(), byteName);
    }

    private void turnOnClearText(Graphics g) {
        if (g instanceof Graphics2D){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    protected void paintByte(Graphics g, Point point, int byteValue, String name){
        char[] bitValues = to8BitString(byteValue).toCharArray();

        paintBits(g, point, bitValues);
        paintByteBorder(g, point);
        paintByteName(g, point, name);
        paintHighlightIndicator(g, point);
        paintByteValues(g, point, byteValue);
    }

    protected void paintHighlightIndicator(Graphics g, Point point){
        if (highlightColor == null)
            return;

        Color originalColor = g.getColor();
        g.setColor(highlightColor);

        Path2D arrow = new Path2D.Double();
        arrow.moveTo(point.x, point.y - 7);
        arrow.lineTo(point.x + 8, point.y - 7 - 5);
        arrow.lineTo(point.x + 8, point.y - 7 + 5);
        arrow.lineTo(point.x, point.y - 7);
        arrow.closePath();
        ((Graphics2D)g).fill(arrow);

        g.setColor(originalColor);
    }

    protected void paintBits(Graphics g, Point point, char[] bitValues) {
        g.setColor(Color.lightGray);
        for (int i=0; i<8; i++){
            paintBit(g, new Point(point.x + (i*bitSize), point.y), bitValues[i]);
        }
    }

    protected void paintBit(Graphics g, Point point, char val){
        final int padding = 5;

        g.setFont(new Font("Courier New", Font.PLAIN, bitFontSize));
        g.drawRect(point.x, point.y, bitSize, bitSize);
        g.drawString(""+val, point.x+padding, point.y+(bitSize-padding));
    }

    private void paintByteBorder(Graphics g, Point point) {
        g.setColor(Color.BLACK);
        g.drawRect(point.x, point.y, byteSize, bitSize);
    }

    private void paintByteName(Graphics g, Point point, String name) {
        g.setColor(Color.blue);
        g.setFont(new Font("Courier New", Font.PLAIN, valueFontSize));
        int xLoc = ((highlightColor == null) ? point.x : point.x + 10);
        g.drawString(name, xLoc, point.y-1);
    }

    private void paintByteValues(Graphics g, Point point, int byteValue) {
        g.setColor(Color.RED);
        g.setFont(new Font("Courier New", Font.PLAIN, valueFontSize));
        String values = "(" + fromSignedByte(byteValue) + ", 0x" + Integer.toHexString(byteValue) + ")";
        g.drawString(values, (point.x + byteSize - bitSize - (values.length() * (valueFontSize/2))), point.y-1);
    }

    private String to8BitString(int fakeByte){
        StringBuilder formattedByteString = new StringBuilder(Integer.toBinaryString(fakeByte));
        if (formattedByteString.length() < 8){
            for (int i=formattedByteString.length(); i<8; i++){
                formattedByteString.append("0" + formattedByteString);
            }
        }
        return formattedByteString.toString();
    }

    private int fromSignedByte(int signedByte){
        int signedByteByte = signedByte & 0xFF;
        if ((signedByteByte & 128) == 128)
            return -( ( (~signedByteByte) +1) & 0xFF); //Twos compliment compensation
        else
            return signedByteByte & 0b01111111;
    }
}
