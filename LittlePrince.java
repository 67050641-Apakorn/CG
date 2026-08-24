package Assignment1;
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

        int bWidth = (int) (380 * Math.sin(t * Math.PI / 2));
        int bHeight = 280;

        g2d.setColor(new Color(230, 220, 200));
        g2d.fillRoundRect(300 - bWidth / 2, 300 - bHeight / 2, bWidth, bHeight, 15, 15);

        if (t > 0.3) {
            g2d.setColor(new Color(90, 80, 100));
            g2d.setFont(new Font("Serif", Font.ITALIC, 24));
            g2d.drawString("The Little Prince", 220, 290);
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

    private void drawFlyingScene(Graphics2D g2d, double t) {
        g2d.setColor(new Color(30, 80, 100));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        int flyX = (int) (150 + t * 250);
        int flyY = (int) (380 - Math.sin(t * Math.PI) * 120);

        g2d.setStroke(new BasicStroke(1.2f));
        for (int i = 0; i < 7; i++) {
            int birdX = flyX - 100 + i * 35;
            int birdY = flyY - 180 + (int) (Math.sin(globalTime * 4 + i) * 15);

            g2d.setColor(new Color(250, 250, 255));
            GeneralPath bird = new GeneralPath();
            bird.moveTo(birdX, birdY);
            bird.curveTo(birdX - 15, birdY - 20, birdX - 30, birdY - 10, birdX - 40, birdY - 25);
            bird.curveTo(birdX - 20, birdY - 5, birdX, birdY, birdX, birdY);
            g2d.fill(bird);

            g2d.setColor(new Color(240, 230, 210, 150));
            g2d.drawLine(birdX, birdY, flyX + 10, flyY - 20);
        }

        drawPrinceFlying(g2d, flyX, flyY);
        drawFoxHeld(g2d, flyX - 15, flyY + 5);
    }

    private void drawRoseAndClosingBook(Graphics2D g2d, double t) {
        if (t < 0.6) {
            g2d.setColor(new Color(250, 225, 200));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            drawMidpointEllipse(g2d, 300, 650, 360, 220, new Color(190, 120, 80));

            int rx = 300;
            int ry = 380;
            g2d.setColor(new Color(70, 120, 60));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawLine(rx, ry, rx, ry + 50);

            g2d.setColor(new Color(220, 40, 60));
            g2d.fillOval(rx - 12, ry - 15, 24, 24);

            drawMidpointEllipse(g2d, rx, ry + 10, 35, 45, new Color(180, 220, 240));

            drawPrinceSitting(g2d, rx - 70, ry + 15);
            drawFoxSitting(g2d, rx + 40, ry + 20);
        } else {
            double closeT = (t - 0.6) / 0.4;
            int bWidth = (int) (380 * (1.0 - Math.pow(closeT, 2)));
            int bHeight = 280;

            g2d.setColor(new Color(25, 35, 60));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            if (bWidth > 5) {
                g2d.setColor(new Color(230, 220, 200));
                g2d.fillRoundRect(300 - bWidth / 2, 300 - bHeight / 2, Math.max(10, bWidth), bHeight, 15, 15);
            }
        }
    }

    // ==========================================
    // โมเดลเจ้าชายน้อย (3D Figure Style)
    // ==========================================
    private void drawPrinceSitting(Graphics2D g2d, int x, int y) {
        // 1. รองเท้าสีน้ำตาล
        g2d.setColor(new Color(110, 70, 45));
        g2d.fillOval(x - 12, y + 26, 11, 7);
        g2d.fillOval(x + 1, y + 26, 11, 7);

        // 2. ขาและชุดกางเกงพองมนสีเขียวมะนาว (Green Jumpsuit)
        g2d.setColor(new Color(160, 205, 95));
        g2d.fillRoundRect(x - 13, y + 6, 26, 22, 14, 14);

        // เข็มขัดสีแดง
        g2d.setColor(new Color(210, 50, 55));
        g2d.fillRect(x - 13, y + 10, 26, 3);

        // กระดุมสีขาว 2 เม็ด
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 2, y + 1, 4, 4);
        g2d.fillOval(x - 2, y + 5, 4, 4);

        // 3. หัวกลมมนโทนสีเนื้ออ่อน
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 16, y - 26, 32, 28);

        // 4. ทรงผมสีทองสไตล์ 3D Molded Hair
        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x - 18, y - 31, 36, 22);
        g2d.fillOval(x - 14, y - 34, 20, 16);
        g2d.fillOval(x - 2, y - 33, 18, 14);
        g2d.fillOval(x - 18, y - 24, 10, 14);
        g2d.fillOval(x + 8, y - 24, 10, 14);

        // หูสีเนื้อเล็กๆ สองข้าง
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 18, y - 16, 5, 7);
        g2d.fillOval(x + 13, y - 16, 5, 7);

        // 5. ใบหน้ามินิมอล (ตาจุดดำ + ยิ้มเส้นบาง + แก้มระเรื่อ)
        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x - 8, y - 14, 4, 4);
        g2d.fillOval(x + 4, y - 14, 4, 4);

        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x - 4, y - 11, 8, 5, 200, 140);

        g2d.setColor(new Color(245, 160, 160, 130));
        g2d.fillOval(x - 13, y - 11, 6, 4);
        g2d.fillOval(x + 7, y - 11, 6, 4);

        // 6. ผ้าพันคอสีแดง (Red Scarf) + ชายผ้าติดดาวสีทอง
        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x - 14, y - 5, 28, 8, 6, 6);
        g2d.fillRoundRect(x - 13, y + 1, 8, 18, 5, 5);

        // ดาวสีทองที่ปลายผ้าพันคอ
        g2d.setColor(new Color(245, 200, 50));
        drawMiniStar(g2d, x - 9, y + 14, 3);
    }

    private void drawPrinceFlying(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(160, 205, 95));
        GeneralPath body = new GeneralPath();
        body.moveTo(x - 12, y + 4);
        body.lineTo(x + 18, y + 16);
        body.lineTo(x + 10, y + 26);
        body.lineTo(x - 14, y + 14);
        body.closePath();
        g2d.fill(body);

        g2d.setColor(new Color(210, 50, 55));
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawLine(x - 2, y + 10, x + 4, y + 19);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x + 8, y - 18, 28, 26);

        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x + 5, y - 22, 32, 18);
        g2d.fillOval(x + 2, y - 16, 12, 14);
        g2d.fillOval(x + 14, y - 25, 12, 12);

        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x + 18, y - 10, 4, 4);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawArc(x + 19, y - 7, 6, 4, 200, 140);

        g2d.setColor(new Color(245, 160, 160, 130));
        g2d.fillOval(x + 23, y - 7, 5, 3);

        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x + 3, y - 4, 16, 7, 4, 4);
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

    // ==========================================
    // โมเดลสุนัขจิ้งจอก (3D Figure Style)
    // ==========================================
    private void drawFoxSitting(Graphics2D g2d, int x, int y) {
        // 1. เท้าหน้าสีน้ำตาลเข้ม
        g2d.setColor(new Color(110, 60, 30));
        g2d.fillOval(x - 9, y + 22, 7, 5);
        g2d.fillOval(x + 2, y + 22, 7, 5);

        // 2. ลำตัวปุ๊กปิ๊กสีส้มพาสเทล
        g2d.setColor(new Color(235, 165, 85));
        g2d.fillOval(x - 12, y + 6, 24, 20);

        // ท้องสีขาว
        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(x - 7, y + 8, 14, 16);

        // แขนสั้นๆ
        g2d.setColor(new Color(210, 130, 50));
        g2d.fillOval(x - 12, y + 8, 5, 9);
        g2d.fillOval(x + 7, y + 8, 5, 9);

        // 3. หูสามเหลี่ยม ปลายสีน้ำตาลเข้ม
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

        // 4. หัวทรงแก้มป่อง
        g2d.setColor(new Color(235, 165, 85));
        GeneralPath head = new GeneralPath();
        head.moveTo(x, y - 18);
        head.curveTo(x + 18, y - 12, x + 22, y + 2, x, y + 8);
        head.curveTo(x - 22, y + 2, x - 18, y - 12, x, y - 18);
        head.closePath();
        g2d.fill(head);

        // ปากกระบอกสีขาวกลมโต
        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(x - 8, y - 5, 16, 11);

        // 5. ตาจุดกลมดำ + จมูกดำเล็กๆ
        g2d.setColor(new Color(40, 35, 35));
        g2d.fillOval(x - 11, y - 4, 3, 4);
        g2d.fillOval(x + 8, y - 4, 3, 4);
        g2d.fillOval(x - 2, y - 4, 4, 3);
    }

    private void drawFoxHeld(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(235, 165, 85));
        g2d.fillOval(x - 10, y - 8, 20, 16);
        
        g2d.setColor(new Color(250, 245, 235));
        g2d.fillOval(x - 6, y - 3, 12, 9);

        g2d.setColor(new Color(100, 50, 25));
        g2d.fillOval(x - 8, y - 15, 4, 8);
        g2d.fillOval(x + 4, y - 15, 4, 8);

        g2d.setColor(new Color(40, 35, 35));
        g2d.fillOval(x - 6, y - 2, 2, 3);
        g2d.fillOval(x + 4, y - 2, 2, 3);
        g2d.fillOval(x - 1, y - 2, 3, 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Little Prince - Figure Chibi Version");
        LittlePrince anim = new LittlePrince();
        frame.add(anim);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}