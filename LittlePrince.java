import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

public class LittlePrince extends JPanel implements ActionListener {
    private Timer timer;
    private double globalTime = 0;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    public LittlePrince() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(245, 243, 238));
        // 60 FPS
        timer = new Timer(16, this); 
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        globalTime += 0.025; // ความเร็วฉากละ ~2.5 วินาที
        if (globalTime > 12.5) { // รวม 5 ฉาก ~12.5 วินาที
            globalTime = 0; // วนลูป
        }
        repaint();
    }

    // Midpoint Ellipse Algorithm
    private void drawMidpointEllipse(Graphics2D g2d, int xc, int yc, int rx, int ry, Color color) {
        g2d.setColor(color);
        long rx2 = (long) rx * rx;
        long ry2 = (long) ry * ry;
        long twoRx2 = 2 * rx2;
        long twoRy2 = 2 * ry2;

        long x = 0;
        long y = ry;
        long px = 0;
        long py = twoRx2 * y;

        drawEllipsePixels(g2d, xc, yc, (int) x, (int) y);

        double p1 = ry2 - (rx2 * ry) + (0.25 * rx2);
        while (px < py) {
            x++;
            px += twoRy2;
            if (p1 < 0) {
                p1 += ry2 + px;
            } else {
                y--;
                py -= twoRx2;
                p1 += ry2 + px - py;
            }
            drawEllipsePixels(g2d, xc, yc, (int) x, (int) y);
        }

        double p2 = (ry2 * (x + 0.5) * (x + 0.5)) + (rx2 * (y - 1) * (y - 1)) - (rx2 * ry2);
        while (y > 0) {
            y--;
            py -= twoRx2;
            if (p2 > 0) {
                p2 += rx2 - py;
            } else {
                x++;
                px += twoRy2;
                p2 += rx2 - py + px;
            }
            drawEllipsePixels(g2d, xc, yc, (int) x, (int) y);
        }
    }

    private void drawEllipsePixels(Graphics2D g2d, int xc, int yc, int x, int y) {
        g2d.fillRect(xc + x, yc + y, 2, 2);
        g2d.fillRect(xc - x, yc + y, 2, 2);
        g2d.fillRect(xc + x, yc - y, 2, 2);
        g2d.fillRect(xc - x, yc - y, 2, 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (globalTime < 2.5) {
            drawBookOpening(g2d, globalTime / 2.5);
        } else if (globalTime < 5.0) {
            double t = (globalTime - 2.5) / 2.5;
            drawPlanetScene(g2d, t);
        } else if (globalTime < 7.5) {
            double t = (globalTime - 5.0) / 2.5;
            drawRiverScene(g2d, t);
        } else if (globalTime < 10.0) {
            double t = (globalTime - 7.5) / 2.5;
            drawFlyingScene(g2d, t);
        } else {
            double t = (globalTime - 10.0) / 2.5;
            drawRoseAndClosingBook(g2d, t);
        }

        drawFloatingGlowParticles(g2d);
    }

    private void drawFloatingGlowParticles(Graphics2D g2d) {
        for (int i = 0; i < 20; i++) {
            double px = (Math.sin(globalTime * 0.8 + i * 1.3) * 0.5 + 0.5) * 600;
            double py = (Math.cos(globalTime * 0.5 + i * 0.7) * 0.5 + 0.5) * 600;
            int size = (int) (Math.sin(globalTime * 2 + i) * 3 + 4);
            g2d.setColor(new Color(255, 245, 200, 120));
            g2d.fillOval((int) px, (int) py, size, size);
        }
    }

    private void drawBookOpening(Graphics2D g2d, double t) {
        g2d.setColor(new Color(20, 30, 55));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        int cx = 300;
        int cy = 340;

        double openProgress = Math.sin(t * Math.PI / 2);
        int maxW = (int) (210 * openProgress);
        int maxH = 240;

        if (maxW < 10) return;

        g2d.setColor(new Color(55, 33, 22));
        g2d.fillRoundRect(cx - maxW - 12, cy - maxH / 2 - 6, (maxW + 12) * 2, maxH + 30, 20, 20);

        g2d.setColor(new Color(210, 185, 140));
        g2d.fillRoundRect(cx - maxW - 6, cy - maxH / 2 + 10, maxW * 2 + 12, maxH, 8, 8);
        g2d.setColor(new Color(140, 115, 80));
        g2d.drawRoundRect(cx - maxW - 6, cy - maxH / 2 + 10, maxW * 2 + 12, maxH, 8, 8);

        GeneralPath leftPage = new GeneralPath();
        leftPage.moveTo(cx, cy + maxH / 2);
        leftPage.curveTo(cx - maxW * 0.5, cy + maxH / 2 - 10, cx - maxW + 10, cy + maxH / 2 - 25, cx - maxW, cy + maxH / 2 - 35);
        leftPage.lineTo(cx - maxW, cy - maxH / 2 - 15);
        leftPage.curveTo(cx - maxW * 0.5, cy - maxH / 2 + 5, cx, cy - maxH / 2 - 10, cx, cy - maxH / 2 + 10);
        leftPage.closePath();

        g2d.setColor(new Color(242, 226, 196));
        g2d.fill(leftPage);
        g2d.setColor(new Color(180, 155, 120));
        g2d.draw(leftPage);

        GeneralPath rightPage = new GeneralPath();
        rightPage.moveTo(cx, cy + maxH / 2);
        rightPage.curveTo(cx + maxW * 0.5, cy + maxH / 2 - 10, cx + maxW - 10, cy + maxH / 2 - 25, cx + maxW, cy + maxH / 2 - 35);
        rightPage.lineTo(cx + maxW, cy - maxH / 2 - 15);
        rightPage.curveTo(cx + maxW * 0.5, cy - maxH / 2 + 5, cx, cy - maxH / 2 - 10, cx, cy - maxH / 2 + 10);
        rightPage.closePath();

        g2d.setColor(new Color(238, 220, 188));
        g2d.fill(rightPage);
        g2d.setColor(new Color(180, 155, 120));
        g2d.draw(rightPage);

        g2d.setColor(new Color(90, 65, 45, 140));
        g2d.fillRect(cx - 5, cy - maxH / 2 - 10, 10, maxH + 15);

        if (t > 0.45) {
            g2d.setColor(new Color(80, 60, 45, 180));
            for (int i = 0; i < 7; i++) {
                g2d.drawLine(cx - maxW + 25, cy - 70 + (i * 16), cx - 25, cy - 70 + (i * 16));
            }
            for (int i = 0; i < 7; i++) {
                g2d.drawLine(cx + 25, cy - 70 + (i * 16), cx + maxW - 25, cy - 70 + (i * 16));
            }

            g2d.setColor(new Color(110, 40, 35));
            g2d.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 22));
            g2d.drawString("The Little Prince", cx - 80, cy + 65);
        }
    }

    private void drawPlanetScene(Graphics2D g2d, double t) {
        g2d.setColor(new Color(18, 24, 48));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        drawMidpointEllipse(g2d, 300, 680, 340, 280, new Color(45, 60, 40));
        
        g2d.setColor(new Color(75, 110, 70));
        g2d.fillOval(-50, 420, 700, 300);

        for (int i = 0; i < 40; i++) {
            int fx = (i * 17 + 30) % 550 + 25;
            int fy = 450 + (int) (Math.sin(i + globalTime * 2) * 10) + (i % 5) * 15;
            g2d.setColor(new Color(245, 180, 200, 200));
            g2d.fillOval(fx, fy, 8, 8);
        }

        drawPrinceSitting(g2d, 220, 410);

        double foxMove = Math.min(1.0, t * 1.8);
        int foxX = (int) (480 - foxMove * 140);
        drawFoxSitting(g2d, foxX, 400);
    }

    private void drawRiverScene(Graphics2D g2d, double t) {
        g2d.setColor(new Color(110, 140, 185));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        GeneralPath bank = new GeneralPath();
        bank.moveTo(0, 0);
        bank.curveTo(200, 150, 400, 50, 600, 200);
        bank.lineTo(600, 0);
        bank.closePath();
        g2d.setColor(new Color(210, 200, 180));
        g2d.fill(bank);

        double boatPath = t * 400;
        int bx = (int) (100 + boatPath * 0.9);
        int by = (int) (280 + Math.sin(t * Math.PI * 3) * 20);

        g2d.setColor(new Color(245, 215, 120));
        GeneralPath leaf = new GeneralPath();
        leaf.moveTo(bx - 60, by);
        leaf.curveTo(bx - 20, by + 40, bx + 50, by + 30, bx + 80, by);
        leaf.curveTo(bx + 20, by - 15, bx - 30, by - 10, bx - 60, by);
        leaf.closePath();
        g2d.fill(leaf);

        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillOval(bx - 30, by + 10, 12, 4);
        g2d.fillOval(bx + 20, by + 15, 16, 5);

        drawPrinceSitting(g2d, bx, by - 25);
        drawFoxSitting(g2d, bx - 30, by - 20);
    }

    // ==========================================
    // ฉากบินพร้อมนกที่ปรับรูปทรงและจังหวะขยับปีก
    // ==========================================
    private void drawFlyingScene(Graphics2D g2d, double t) {
        g2d.setColor(new Color(24, 68, 92));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        int flyX = (int) (180 + t * 220);
        int flyY = (int) (400 - Math.sin(t * Math.PI) * 100);

        int holdX = flyX + 16;
        int holdY = flyY - 26;

        int numBirds = 6;
        for (int i = 0; i < numBirds; i++) {
            int birdX = flyX - 120 + i * 45;
            int birdY = flyY - 240 + (int) (Math.sin(globalTime * 2 + i) * 12);

            // เชือกเชื่อม
            g2d.setColor(new Color(210, 225, 230, 180));
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawLine(birdX, birdY, holdX, holdY);

            // ปรับจังหวะขยับปีกให้นุ่มนวล ช้าลง (3.5)
            double wingFlap = Math.sin(globalTime * 3.5 + i * 0.6);
            drawProportionalFlappingBird(g2d, birdX, birdY, wingFlap);
        }

        drawPrinceStandingFlight(g2d, flyX, flyY, holdX, holdY);
    }

    // นกที่มีปีกสั้นสมส่วน ปากแหลมกระชับ และกระพือปีกอย่างนุ่มนวล
    private void drawProportionalFlappingBird(Graphics2D g2d, int x, int y, double flap) {
        g2d.setColor(Color.WHITE);

        // 1. หัวและปากนกสั้นแหลม (Short & Sharp Beak)
        GeneralPath headAndBeak = new GeneralPath();
        headAndBeak.moveTo(x - 6, y - 2);
        headAndBeak.curveTo(x - 9, y - 5, x - 12, y - 3, x - 13, y - 1); // หัวกลม
        headAndBeak.lineTo(x - 16, y); // ปากแหลมสั้น
        headAndBeak.lineTo(x - 13, y + 2); // มุมปากล่าง
        headAndBeak.curveTo(x - 10, y + 4, x - 6, y + 4, x - 4, y + 2); // อกนก
        headAndBeak.closePath();
        g2d.fill(headAndBeak);

        // 2. ลำตัวและหาง (Body & Tail)
        GeneralPath bodyAndTail = new GeneralPath();
        bodyAndTail.moveTo(x - 6, y - 2);
        bodyAndTail.curveTo(x, y - 3, x + 6, y - 1, x + 10, y + 1); // หลัง
        bodyAndTail.lineTo(x + 18, y + 4); // ปลายหาง
        bodyAndTail.lineTo(x + 16, y + 7);
        bodyAndTail.lineTo(x + 8, y + 4); // ท้อง
        bodyAndTail.curveTo(x + 2, y + 5, x - 4, y + 3, x - 6, y + 1);
        bodyAndTail.closePath();
        g2d.fill(bodyAndTail);

        // 3. ปีกทรงสวย สั้นกะทัดรัด (Flapping Wings)
        // ระยะยกปีก (จำกัดความสูงไม่ให้ยาวเกินไป)
        double wingOffsetY = flap * 10.0; 

        // ปีกไกล (Background Wing)
        GeneralPath wingBack = new GeneralPath();
        wingBack.moveTo(x - 2, y - 2);
        wingBack.curveTo(x - 4, y - 8 + wingOffsetY * 0.5, x - 8, y - 13 + wingOffsetY, x - 11, y - 15 + wingOffsetY);
        wingBack.curveTo(x - 7, y - 10 + wingOffsetY, x - 2, y - 5 + wingOffsetY * 0.5, x + 3, y - 1);
        wingBack.closePath();
        g2d.fill(wingBack);

        // ปีกใกล้ (Foreground Wing)
        GeneralPath wingFront = new GeneralPath();
        wingFront.moveTo(x - 3, y - 1);
        wingFront.curveTo(x - 6, y - 10 + wingOffsetY * 0.6, x - 10, y - 17 + wingOffsetY, x - 14, y - 19 + wingOffsetY);
        wingFront.curveTo(x - 9, y - 12 + wingOffsetY, x - 3, y - 6 + wingOffsetY * 0.5, x + 4, y);
        wingFront.closePath();
        g2d.fill(wingFront);
    }

    private void drawPrinceStandingFlight(Graphics2D g2d, int x, int y, int holdX, int holdY) {
        g2d.setColor(new Color(110, 70, 45));
        g2d.fillOval(x - 6, y + 26, 11, 7);
        g2d.fillOval(x + 2, y + 26, 11, 7);

        g2d.setColor(new Color(160, 205, 95));
        g2d.fillRoundRect(x - 5, y + 16, 7, 13, 6, 6);
        g2d.fillRoundRect(x + 3, y + 16, 7, 13, 6, 6);

        g2d.fillRoundRect(x - 8, y + 2, 22, 18, 14, 14);

        g2d.setColor(new Color(210, 50, 55));
        g2d.fillRect(x - 8, y + 7, 22, 3);

        g2d.setColor(new Color(160, 205, 95));
        g2d.setStroke(new BasicStroke(6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x + 5, y + 4, holdX, holdY);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(holdX - 3, holdY - 3, 6, 6);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 2, y - 6, 10, 10);

        g2d.fillOval(x - 12, y - 26, 28, 24);

        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x - 14, y - 31, 32, 20);
        g2d.fillOval(x - 12, y - 34, 18, 14);
        g2d.fillOval(x + 2, y - 32, 14, 12);
        g2d.fillOval(x + 10, y - 22, 8, 12);

        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x + 8, y - 14, 4, 4);
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x + 7, y - 11, 6, 4, 200, 140);

        g2d.setColor(new Color(245, 160, 160, 140));
        g2d.fillOval(x + 10, y - 11, 5, 4);

        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x - 10, y - 4, 24, 8, 6, 6);

        double wave1 = Math.sin(globalTime * 4) * 6;
        double wave2 = Math.cos(globalTime * 4) * 8;

        GeneralPath scarf = new GeneralPath();
        scarf.moveTo(x - 8, y - 2);
        scarf.curveTo(x - 35, y - 2 + wave1, x - 70, y - 10 + wave2, x - 105, y - 6 + wave1);
        scarf.lineTo(x - 105, y + 2 + wave1);
        scarf.curveTo(x - 70, y + 2 + wave2, x - 35, y + 5 + wave1, x - 8, y + 4);
        scarf.closePath();
        g2d.fill(scarf);

        g2d.setColor(new Color(245, 200, 50));
        drawMiniStar(g2d, (int) (x - 98), (int) (y - 2 + wave1), 3);

        int foxOnHeadX = x - 12;
        int foxOnHeadY = y - 22;
        
        g2d.setColor(new Color(235, 165, 85));
        g2d.fillOval(foxOnHeadX - 6, foxOnHeadY - 4, 15, 13);
        
        g2d.setColor(new Color(100, 50, 25));
        g2d.fillOval(foxOnHeadX - 3, foxOnHeadY - 11, 4, 8);
        g2d.fillOval(foxOnHeadX + 5, foxOnHeadY - 11, 4, 8);

        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(foxOnHeadX - 3, foxOnHeadY + 1, 10, 7);

        g2d.setColor(new Color(40, 35, 35));
        g2d.fillOval(foxOnHeadX - 2, foxOnHeadY + 2, 2, 3);
        g2d.fillOval(foxOnHeadX + 4, foxOnHeadY + 2, 2, 3);
    }

    private void drawRoseAndClosingBook(Graphics2D g2d, double t) {
        if (t < 0.6) {
            g2d.setColor(new Color(250, 225, 200));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            drawMidpointEllipse(g2d, 300, 650, 360, 220, new Color(190, 120, 80));

            int rx = 300;
            int ry = 360;

            drawDetailedRoseWithDome(g2d, rx, ry);

            drawPrinceSitting(g2d, rx - 100, ry + 75);
            drawFoxSitting(g2d, rx + 80, ry + 70);
        } else {
            double closeT = (t - 0.6) / 0.4;
            g2d.setColor(new Color(25, 35, 60));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            drawBookOpening(g2d, 1.0 - closeT);
        }
    }

    private void drawDetailedRoseWithDome(Graphics2D g2d, int cx, int cy) {
        int baseY = cy + 65;

        g2d.setColor(new Color(100, 65, 40));
        g2d.fillOval(cx - 55, baseY + 4, 110, 22);
        g2d.setColor(new Color(140, 95, 55));
        g2d.fillOval(cx - 50, baseY, 100, 18);
        g2d.setColor(new Color(80, 50, 30));
        g2d.drawOval(cx - 50, baseY, 100, 18);

        g2d.setColor(new Color(65, 115, 60));
        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        GeneralPath stem = new GeneralPath();
        stem.moveTo(cx, baseY + 5);
        stem.curveTo(cx - 5, cy + 35, cx + 5, cy + 15, cx, cy - 5);
        g2d.draw(stem);

        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(cx - 3, cy + 30, cx - 8, cy + 26);
        g2d.drawLine(cx + 3, cy + 18, cx + 8, cy + 14);

        GeneralPath leafLeft = new GeneralPath();
        leafLeft.moveTo(cx - 3, cy + 32);
        leafLeft.curveTo(cx - 18, cy + 22, cx - 25, cy + 30, cx - 3, cy + 37);
        g2d.fill(leafLeft);

        GeneralPath leafRight = new GeneralPath();
        leafRight.moveTo(cx + 3, cy + 20);
        leafRight.curveTo(cx + 18, cy + 10, cx + 25, cy + 18, cx + 3, cy + 25);
        g2d.fill(leafRight);

        g2d.setColor(new Color(50, 95, 45));
        int[] sepalX = {cx - 10, cx - 4, cx, cx + 4, cx + 10};
        int[] sepalY = {cy - 4, cy + 4, cy - 2, cy + 4, cy - 4};
        g2d.fillPolygon(sepalX, sepalY, 5);

        g2d.setColor(new Color(160, 20, 35));
        g2d.fillOval(cx - 18, cy - 26, 36, 26);
        g2d.fillOval(cx - 24, cy - 20, 22, 18);
        g2d.fillOval(cx + 2, cy - 20, 22, 18);

        g2d.setColor(new Color(215, 35, 50));
        g2d.fillOval(cx - 15, cy - 24, 30, 22);
        
        GeneralPath petalFront = new GeneralPath();
        petalFront.moveTo(cx - 16, cy - 14);
        petalFront.curveTo(cx - 12, cy - 2, cx + 12, cy - 2, cx + 16, cy - 14);
        petalFront.curveTo(cx + 8, cy - 8, cx - 8, cy - 8, cx - 16, cy - 14);
        petalFront.closePath();
        g2d.fill(petalFront);

        g2d.setColor(new Color(240, 70, 85));
        g2d.fillOval(cx - 8, cy - 22, 16, 12);
        g2d.setColor(new Color(140, 15, 25));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawArc(cx - 6, cy - 20, 12, 8, 30, 220);
        g2d.drawArc(cx - 4, cy - 18, 8, 5, 200, 200);

        int domeW = 86;
        int domeH = 110;
        int domeX = cx - domeW / 2;
        int domeY = baseY - domeH + 5;

        g2d.setColor(new Color(220, 240, 255, 45));
        g2d.fillRoundRect(domeX, domeY, domeW, domeH, domeW, domeW);

        g2d.setColor(new Color(160, 190, 215, 180));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(domeX, domeY, domeW, domeH, domeW, domeW);

        g2d.setColor(new Color(255, 255, 255, 140));
        g2d.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(domeX + 8, domeY + 10, 25, 70, 100, 75);
        g2d.drawArc(domeX + 14, domeY + 16, 15, 30, 110, 60);
    }

    private void drawPrinceSitting(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(110, 70, 45));
        g2d.fillOval(x - 12, y + 25, 11, 7);
        g2d.fillOval(x + 1, y + 25, 11, 7);

        g2d.setColor(new Color(160, 205, 95));
        g2d.fillRoundRect(x - 13, y + 5, 26, 23, 16, 16);

        g2d.setColor(new Color(210, 50, 55));
        g2d.fillRect(x - 13, y + 10, 26, 4);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 2, y + 1, 4, 4);
        g2d.fillOval(x - 2, y + 5, 4, 4);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 6, y - 6, 12, 10);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 16, y - 28, 32, 28);

        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x - 18, y - 33, 36, 22);
        g2d.fillOval(x - 14, y - 36, 20, 16);
        g2d.fillOval(x - 2, y - 35, 18, 14);
        g2d.fillOval(x - 19, y - 25, 10, 15);
        g2d.fillOval(x + 9, y - 25, 10, 15);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 18, y - 17, 5, 7);
        g2d.fillOval(x + 13, y - 17, 5, 7);

        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x - 8, y - 15, 4, 4);
        g2d.fillOval(x + 4, y - 15, 4, 4);

        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x - 4, y - 12, 8, 5, 200, 140);

        g2d.setColor(new Color(245, 160, 160, 140));
        g2d.fillOval(x - 13, y - 12, 6, 4);
        g2d.fillOval(x + 7, y - 12, 6, 4);

        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x - 14, y - 5, 28, 9, 8, 8);

        double scarfWave1 = Math.sin(globalTime * 3.5) * 8;
        double scarfWave2 = Math.cos(globalTime * 3.5) * 12;

        GeneralPath scarfTail = new GeneralPath();
        scarfTail.moveTo(x - 10, y - 2);
        scarfTail.curveTo(x - 25, y - 2 + scarfWave1, x - 45, y + scarfWave2, x - 65, y - 5 + scarfWave1);
        scarfTail.lineTo(x - 63, y + 4 + scarfWave1);
        scarfTail.curveTo(x - 45, y + 8 + scarfWave2, x - 25, y + 6 + scarfWave1, x - 10, y + 6);
        scarfTail.closePath();
        g2d.fill(scarfTail);

        g2d.setColor(new Color(245, 200, 50));
        drawMiniStar(g2d, (int) (x - 58), (int) (y - 1 + scarfWave1), 3);
    }

    private void drawMiniStar(Graphics2D g2d, int cx, int cy, int r) {
        GeneralPath star = new GeneralPath();
        for (int i = 0; i < 5; i++) {
            double angle = i * 4 * Math.PI / 5 - Math.PI / 2;
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            if (i == 0) star.moveTo(x, y);
            else star.lineTo(x, y);
        }
        star.closePath();
        g2d.fill(star);
    }

    private void drawFoxSitting(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(110, 60, 30));
        g2d.fillOval(x - 9, y + 22, 7, 5);
        g2d.fillOval(x + 2, y + 22, 7, 5);

        g2d.setColor(new Color(235, 165, 85));
        g2d.fillOval(x - 12, y + 6, 24, 20);

        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(x - 7, y + 8, 14, 16);

        g2d.setColor(new Color(210, 130, 50));
        g2d.fillOval(x - 12, y + 8, 5, 9);
        g2d.fillOval(x + 7, y + 8, 5, 9);

        g2d.setColor(new Color(100, 50, 25));
        int[] earX1 = {x - 14, x - 8, x - 2};
        int[] earY1 = {y - 4, y - 22, y - 4};
        g2d.fillPolygon(earX1, earY1, 3);

        int[] earX2 = {x + 2, x + 8, x + 14};
        int[] earY2 = {y - 4, y - 22, y - 4};
        g2d.fillPolygon(earX2, earY2, 3);

        g2d.setColor(new Color(235, 165, 85));
        int[] innerEarX1 = {x - 12, x - 8, x - 4};
        int[] innerEarY1 = {y - 4, y - 17, y - 4};
        g2d.fillPolygon(innerEarX1, innerEarY1, 3);

        int[] innerEarX2 = {x + 4, x + 8, x + 12};
        int[] innerEarY2 = {y - 4, y - 17, y - 4};
        g2d.fillPolygon(innerEarX2, innerEarY2, 3);

        g2d.setColor(new Color(235, 165, 85));
        GeneralPath head = new GeneralPath();
        head.moveTo(x, y - 18);
        head.curveTo(x + 18, y - 12, x + 22, y + 2, x, y + 8);
        head.curveTo(x - 22, y + 2, x - 18, y - 12, x, y - 18);
        head.closePath();
        g2d.fill(head);

        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(x - 8, y - 5, 16, 11);

        g2d.setColor(new Color(40, 35, 35));
        g2d.fillOval(x - 11, y - 4, 3, 4);
        g2d.fillOval(x + 8, y - 4, 3, 4);
        g2d.fillOval(x - 2, y - 4, 4, 3);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Little Prince - Smooth Bird Flapping");
        LittlePrince anim = new LittlePrince();
        frame.add(anim);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
