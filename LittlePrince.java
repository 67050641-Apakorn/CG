import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class LittlePrince extends JPanel implements ActionListener {
    private Timer timer;
    private double globalTime = 0;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    public LittlePrince() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(245, 243, 238));
        timer = new Timer(16, this); 
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        globalTime += 0.025; // ความเร็วฉากละ ~2.5 วินาที
        if (globalTime > 12.5) { // รวม 5 ฉาก ~12.5 วินาที
            globalTime = 0; // วนลูปการทำงาน
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ตัวสลับฉากตามช่วงเวลา
        if (globalTime < 2.5) {
            // SCENE 1: เปิดเรื่อง(เปิดหนังสือ)
            drawBookOpening(g2d, globalTime / 2.5);
        } else if (globalTime < 5.0) {
            // SCENE 2: จิ้งจอกเดินมาหาเจ้าชายน้อย
            double t = (globalTime - 2.5) / 2.5;
            drawFlowerMeadowScene(g2d, t);
        } else if (globalTime < 7.5) {
            // SCENE 3: ล่องเรือกระดาษในแม่น้ำ
            double t = (globalTime - 5.0) / 2.5;
            drawRiverScene(g2d, t);
        } else if (globalTime < 10.0) {
            // SCENE 4: บินไปกับฝูงนก
            double t = (globalTime - 7.5) / 2.5;
            drawFlyingScene(g2d, t);
        } else {
            // SCENE 5: เจอดอกกุหลาบ และปิดหนังสือ
            double t = (globalTime - 10.0) / 2.5;
            drawRoseAndClosingBook(g2d, t);
        }

        //ละอองดาวเรืองแสงลอยทั้งหน้าจอ
        drawFloatingGlowParticles(g2d);
    }

    // เอฟเฟกต์ส่วนกลาง
    private void drawFloatingGlowParticles(Graphics2D g2d) {
        for (int i = 0; i < 20; i++) {
            double px = (Math.sin(globalTime * 0.8 + i * 1.3) * 0.5 + 0.5) * 600;
            double py = (Math.cos(globalTime * 0.5 + i * 0.7) * 0.5 + 0.5) * 600;
            int size = (int) (Math.sin(globalTime * 2 + i) * 3 + 4);
            g2d.setColor(new Color(255, 245, 200, 120));
            g2d.fillOval((int) px, (int) py, size, size);
        }
    }

    // SCENE 1: เปิดเรื่อง (ฉากหลังจักรวาล ดวงดาว และดาวเคราะห์)
    private void drawBookOpening(Graphics2D g2d, double t) {
        // 1. พื้นหลังไล่เฉดสีจักรวาลห้วงอวกาศลึก
        Point2D spaceStart = new Point2D.Float(0, 0);
        Point2D spaceEnd = new Point2D.Float(0, HEIGHT);
        LinearGradientPaint spaceGrad = new LinearGradientPaint(spaceStart, spaceEnd,
                new float[]{0.0f, 0.5f, 1.0f},
                new Color[]{new Color(10, 14, 32), new Color(20, 26, 58), new Color(12, 16, 38)});
        g2d.setPaint(spaceGrad);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // วาดฉากหลังจักรวาล
        drawUniverseBackground(g2d);

        int cx = 300;
        int cy = 340;

        double openProgress = Math.sin(t * Math.PI / 2);
        int maxW = (int) (210 * openProgress);
        int maxH = 240;

        if (maxW < 10) return;

        // ปกหนังสือด้านนอก
        g2d.setColor(new Color(55, 33, 22));
        g2d.fillRoundRect(cx - maxW - 12, cy - maxH / 2 - 6, (maxW + 12) * 2, maxH + 30, 20, 20);

        // สันขอบหนังสือ
        g2d.setColor(new Color(210, 185, 140));
        g2d.fillRoundRect(cx - maxW - 6, cy - maxH / 2 + 10, maxW * 2 + 12, maxH, 8, 8);
        g2d.setColor(new Color(140, 115, 80));
        g2d.drawRoundRect(cx - maxW - 6, cy - maxH / 2 + 10, maxW * 2 + 12, maxH, 8, 8);

        // หน้าหนังสือฝั่งซ้าย
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

        // หน้าหนังสือฝั่งขวา
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

        // เงากลางหนังสือ
        g2d.setColor(new Color(90, 65, 45, 140));
        g2d.fillRect(cx - 5, cy - maxH / 2 - 10, 10, maxH + 15);

        // ตัวหนังสือและข้อความชื่อเรื่อง
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

    // สร้างบรรยากาศฉากหลังจักรวาล 
    private void drawUniverseBackground(Graphics2D g2d) {
        // เรืองแสงฟุ้งจางๆ ในอวกาศ
        g2d.setColor(new Color(120, 80, 180, 35));
        g2d.fillOval(40, 60, 280, 180);
        g2d.setColor(new Color(60, 130, 200, 30));
        g2d.fillOval(320, 320, 260, 160);

        //ดาวเคราะห์ขนาดต่างๆ ประดับตามมุมอวกาศ
        // ดาวเคราะห์วงแหวน (มุมบนซ้าย)
        drawRingedPlanet(g2d, 100, 110, 26, new Color(245, 190, 140), new Color(210, 110, 130));
        
        // ดาวเคราะห์พาสเทลม่วงอมฟ้า (มุมบนขวา)
        drawSmallBgPlanet(g2d, 500, 120, 22, new Color(150, 170, 235));
        
        // ดาวเคราะห์สีทองดวงเล็ก (มุมล่างซ้าย)
        drawSmallBgPlanet(g2d, 80, 480, 14, new Color(245, 215, 130));

        // ดาวเคราะห์พาสเทลชมพูจิ๋ว (มุมล่างขวา)
        drawSmallBgPlanet(g2d, 520, 460, 16, new Color(235, 160, 190));

        // ดาววิบวับพริ้วไหว
        for (int i = 0; i < 55; i++) {
            int seed = i * 43 + 17;
            int sx = Math.abs(seed * 19) % WIDTH;
            int sy = Math.abs(seed * 31) % HEIGHT;

            // คำนวณการกะพริบของดาว
            int alpha = 80 + (int) (Math.sin(globalTime * 3.0 + i) * 120);
            alpha = Math.max(20, Math.min(255, alpha));

            if (i % 7 == 0) {
                // ประกายดาว 4 แฉก (Cross Star)
                g2d.setColor(new Color(255, 245, 210, alpha));
                int len = 3 + (i % 3) * 2;
                g2d.drawLine(sx - len, sy, sx + len, sy);
                g2d.drawLine(sx, sy - len, sx, sy + len);
            } else {
                // จุดดาวเล็กๆ
                g2d.setColor(new Color(240, 240, 255, alpha));
                int size = 1 + (i % 3);
                g2d.fillOval(sx, sy, size, size);
            }
        }
    }

    // SCENE 2: ดาวสีเหลืองกลางอวกาศ (จิ้งจอกเดินมาหาเจ้าชายน้อย)
    private void drawFlowerMeadowScene(Graphics2D g2d, double t) {
        // ท้องฟ้าอวกาศสีDeep Night Sky
        Point2D skyStart = new Point2D.Float(0, 0);
        Point2D skyEnd = new Point2D.Float(0, HEIGHT);
        LinearGradientPaint skyGrad = new LinearGradientPaint(skyStart, skyEnd, 
                new float[]{0.0f, 0.5f, 1.0f}, 
                new Color[]{new Color(8, 15, 38), new Color(15, 35, 75), new Color(10, 50, 100)});
        g2d.setPaint(skyGrad);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // ทางช้างเผือก
        g2d.setColor(new Color(130, 200, 255, 30));
        GeneralPath milkyWay = new GeneralPath();
        milkyWay.moveTo(50, 0);
        milkyWay.curveTo(200, 150, 350, 200, 550, 0);
        milkyWay.lineTo(600, 0);
        milkyWay.curveTo(400, 250, 200, 200, 100, 0);
        milkyWay.closePath();
        g2d.fill(milkyWay);

        // ประกายดาวเล็กๆ
        for (int i = 0; i < 70; i++) {
            int sx = (i * 37 + 13) % WIDTH;
            int sy = (i * 23 + 7) % 350;
            int sSize = (i % 3 == 0) ? 3 : 2;
            int alpha = 120 + (int)(Math.sin(globalTime * 3 + i) * 100);
            g2d.setColor(new Color(255, 255, 230, Math.max(20, Math.min(255, alpha))));
            g2d.fillOval(sx, sy, sSize, sSize);
        }

        // ประกายดาวดวงใหญ่ 4 แฉก
        drawStarGlow(g2d, 80, 80, 10);
        drawStarGlow(g2d, 500, 120, 12);
        drawStarGlow(g2d, 420, 220, 8);

        //ดาวเคราะห์สีเหลือง
        int planetX = 300;
        int planetY = 660;
        int planetR = 380;

        // Gradient พื้นฐานของดาว
        Point2D pStart = new Point2D.Float(planetX, planetY - planetR);
        Point2D pEnd = new Point2D.Float(planetX, planetY + planetR);
        LinearGradientPaint planetGrad = new LinearGradientPaint(pStart, pEnd,
                new float[]{0.0f, 0.4f, 0.8f, 1.0f},
                new Color[]{new Color(255, 225, 70), new Color(240, 185, 35), new Color(195, 135, 20), new Color(120, 75, 15)});
        g2d.setPaint(planetGrad);
        g2d.fillOval(planetX - planetR, planetY - planetR, planetR * 2, planetR * 2);

        // ลาย Texture แปรงระบายสีบนดาวสีเหลือง (Painted Brush Strokes)
        g2d.setColor(new Color(255, 240, 130, 90));
        g2d.fillOval(planetX - 180, planetY - planetR + 20, 360, 180);
        g2d.setColor(new Color(215, 150, 20, 70));
        g2d.fillOval(planetX - 250, planetY - planetR + 120, 500, 200);
        g2d.setColor(new Color(160, 100, 15, 80));
        g2d.fillOval(planetX - 200, planetY - planetR + 220, 400, 180);

        //วาดตัวละครนั่งคู่กันบนยอดดาวสีเหลือง
           // เจ้าชายน้อยนั่งอยู่ทางซ้ายของยอดดาว
        drawPrinceSitting(g2d, planetX - 25, planetY - planetR + 12);

           // จิ้งจอก ค่อยๆ เดินเข้ามานั่งข้างๆ ทางขวา
        double foxMove = Math.min(1.0, t * 1.5);
        int foxX = (int) (planetX + 100 - foxMove * 70);
        drawFoxSitting(g2d, foxX, planetY - planetR + 18);
    }

    // SCENE 3: ล่องเรือกระดาษในแม่น้ำ
    private void drawRiverScene(Graphics2D g2d, double t) {

        // Background & Water Gradient
        Point2D wStart = new Point2D.Float(0, 0);
        Point2D wEnd = new Point2D.Float(0, HEIGHT);
        LinearGradientPaint waterGrad = new LinearGradientPaint(wStart, wEnd,
                new float[]{0.0f, 0.5f, 1.0f},
                new Color[]{new Color(135, 175, 215), new Color(85, 135, 185), new Color(45, 90, 145)});
        g2d.setPaint(waterGrad);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // พุ่มดอกไม้ริมฝั่งแม่น้ำ (ฝั่งซ้ายและขวา)
        drawRiverBankFlowers(g2d);

        // ประกายดาวเรืองแสงบนผิวน้ำ
        for (int i = 0; i < 35; i++) {
            int sx = (i * 67 + (int)(globalTime * 15)) % (WIDTH + 100) - 50;
            int sy = (i * 37 + 20) % HEIGHT;
            
            // ดาวเรืองแสง 4 แฉกบนผิวน้ำ
            if (i % 3 == 0) {
                int starAlpha = 140 + (int)(Math.sin(globalTime * 4 + i) * 100);
                g2d.setColor(new Color(255, 255, 230, Math.max(30, Math.min(255, starAlpha))));
                int sSize = 6 + (i % 4) * 2;
                g2d.drawLine(sx - sSize, sy, sx + sSize, sy);
                g2d.drawLine(sx, sy - sSize, sx, sy + sSize);
                g2d.fillOval(sx - 1, sy - 1, 3, 3);
            } else {
                // ละอองแสงลอยตามน้ำ
                int glowAlpha = 100 + (int)(Math.cos(globalTime * 3 + i) * 80);
                g2d.setColor(new Color(255, 240, 180, Math.max(20, Math.min(255, glowAlpha))));
                g2d.fillOval(sx, sy, 3, 3);
            }
        }

        // เส้นแสงสะท้อนระลอกน้ำทองคำ
        for (int i = 0; i < 18; i++) {
            int yy = 80 + (i * 31) % 460;
            int xx = (int) ((i * 77 + globalTime * 20) % 700) - 80;
            int len = 25 + (i % 5) * 15;

            GeneralPath glowLine = new GeneralPath();
            glowLine.moveTo(xx, yy);
            glowLine.curveTo(xx + len * 0.3, yy - 3, xx + len * 0.7, yy + 3, xx + len, yy);

            g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(255, 245, 200, 110));
            g2d.draw(glowLine);
        }

        // Position & Dynamics
        double move = Math.min(1.0, t);
        int bx = (int) (80 + move * 430); // พิกัด X ของเรือ
        int by = 340 + (int) (Math.sin(globalTime * 3.0) * 6); // ลอยตามน้ำขึ้นลงเบาๆ

        // เงาเรือกระดาษผิวน้ำ
        g2d.setColor(new Color(20, 50, 80, 90));
        g2d.fillOval(bx - 120, by + 18, 240, 25);

        //ส่วนยอดใบเรือตรงกลางด้านหลัง
        GeneralPath centerPeak = new GeneralPath();
        centerPeak.moveTo(bx, by - 70);       
        centerPeak.lineTo(bx - 45, by - 10);  
        centerPeak.lineTo(bx + 45, by - 10);  
        centerPeak.closePath();

        GradientPaint peakFill = new GradientPaint(
                bx, by - 70, new Color(255, 255, 250),
                bx, by - 10, new Color(225, 220, 210)
        );
        g2d.setPaint(peakFill);
        g2d.fill(centerPeak);

        g2d.setStroke(new BasicStroke(1.2f));
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawLine(bx, by - 70, bx, by - 10);

        // ตัวละครนั่งอยู่ในลำเรือ
        drawFoxSitting(g2d, bx - 30, by - 22);
        drawPrinceSitting(g2d, bx + 20, by - 28);

        // ตัวเรือกระดาษด้านหน้า
        GeneralPath leftHull = new GeneralPath();
        leftHull.moveTo(bx - 130, by);      
        leftHull.lineTo(bx - 45, by - 10);  
        leftHull.lineTo(bx, by + 22);       
        leftHull.closePath();

        g2d.setColor(new Color(240, 238, 232));
        g2d.fill(leftHull);

        GeneralPath rightHull = new GeneralPath();
        rightHull.moveTo(bx + 130, by);     
        rightHull.lineTo(bx + 45, by - 10); 
        rightHull.lineTo(bx, by + 22);      
        rightHull.closePath();

        g2d.setColor(new Color(250, 248, 242));
        g2d.fill(rightHull);

        GeneralPath frontFold = new GeneralPath();
        frontFold.moveTo(bx - 130, by);
        frontFold.lineTo(bx, by + 22);
        frontFold.lineTo(bx + 130, by);
        frontFold.lineTo(bx, by + 32);      
        frontFold.closePath();

        GradientPaint frontFill = new GradientPaint(
                bx, by, new Color(230, 225, 215),
                bx, by + 32, new Color(195, 190, 180)
        );
        g2d.setPaint(frontFill);
        g2d.fill(frontFold);

        // Outlines และ Fold Lines
        g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(150, 145, 140));

        g2d.draw(leftHull);
        g2d.draw(rightHull);
        g2d.draw(frontFold);

        // คลื่นน้ำรอบเรือ
        for (int i = 0; i < 3; i++) {
            int d = i * 8;
            GeneralPath ripple = new GeneralPath();
            ripple.moveTo(bx - 135 - d, by + 10 + d);
            ripple.curveTo(bx - 45, by + 38 + d, bx + 45, by + 40 + d, bx + 135 + d, by + 10 + d);

            g2d.setStroke(new BasicStroke(1.5f - i * 0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(255, 245, 210, 140 - i * 40));
            g2d.draw(ripple);
        }
    }

    // วาดพุ่มดอกไม้ตลิ่งริมแม่น้ำ
    private void drawRiverBankFlowers(Graphics2D g2d) {
        // มุมซ้ายบน
        drawSoftFlowerCluster(g2d, -60, -80, 260, 220, new Color(245, 220, 230));

        // มุมขวาบน
        drawSoftFlowerCluster(g2d, 360, -90, 320, 260, new Color(230, 225, 250));

        // มุมซ้ายล่าง 
        drawSoftFlowerCluster(g2d, -80, 380, 350, 300, new Color(250, 215, 225));

        // มุมขวาล่าง
        drawSoftFlowerCluster(g2d, 380, 400, 300, 260, new Color(225, 235, 255));
    }

    // วาดพุ่มดอกไม้ฟุ้งๆ
    private void drawSoftFlowerCluster(Graphics2D g2d, int x, int y, int w, int h, Color baseColor) {
        // บันทึกค่า RenderHints เพื่อความนุ่มฟุ้งของเส้น
        Object oldAntialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int r = baseColor.getRed();
        int g = baseColor.getGreen();
        int b = baseColor.getBlue();

        // Layer 1
        g2d.setColor(new Color(r, g, b, 35));
        g2d.fillOval(x - 20, y - 20, w + 40, h + 40);

        // Layer 2
        g2d.setColor(new Color(r, g, b, 70));
        g2d.fillOval(x, y, w, h);

        // Layer 3
        int[][] subPuffs = {
            {x + (int)(w * 0.15), y + (int)(h * 0.15), (int)(w * 0.6), (int)(h * 0.6)},
            {x + (int)(w * 0.35), y + (int)(h * 0.1),  (int)(w * 0.55), (int)(h * 0.55)},
            {x + (int)(w * 0.1),  y + (int)(h * 0.35), (int)(w * 0.65), (int)(h * 0.65)},
            {x + (int)(w * 0.3),  y + (int)(h * 0.3),  (int)(w * 0.6), (int)(h * 0.6)}
        };

        for (int[] p : subPuffs) {
            g2d.setColor(new Color(r, g, b, 90));
            g2d.fillOval(p[0], p[1], p[2], p[3]);

            // Layer 4
            g2d.setColor(new Color(255, 255, 255, 110));
            g2d.fillOval(p[0] + p[2] / 4, p[1] + p[3] / 4, p[2] / 2, p[3] / 2);

            // Layer 5 จุดละอองเกสรเรืองแสงสีทองกระจายเบาๆ
            g2d.setColor(new Color(255, 235, 170, 140));
            g2d.fillOval(p[0] + p[2] / 3, p[1] + p[3] / 3, 6, 6);
            g2d.fillOval(p[0] + (int)(p[2] * 0.6), p[1] + (int)(p[3] * 0.4), 4, 4);
        }

        // Layer 6: ดอกไม้และกลีบดอกไม้
        int seed = x * 31 + y * 17; // ใช้ Seed คงที่เพื่อไม่ให้ดอกไม้ขยับสั่นตอน Render
        for (int i = 0; i < 16; i++) {
            int fx = x + (Math.abs(seed * (i + 1) * 13) % (int)(w * 0.8)) + (int)(w * 0.1);
            int fy = y + (Math.abs(seed * (i + 1) * 29) % (int)(h * 0.8)) + (int)(h * 0.1);
            int petalSize = 5 + (i % 4) * 2;

            if (i % 2 == 0) {
                // ดอกไม้ 5 กลีบ
                g2d.setColor(new Color(255, 245, 250, 200));
                for (int angle = 0; angle < 360; angle += 72) {
                    double rad = Math.toRadians(angle);
                    int px = fx + (int) (Math.cos(rad) * (petalSize * 0.7));
                    int py = fy + (int) (Math.sin(rad) * (petalSize * 0.7));
                    g2d.fillOval(px - petalSize / 2, py - petalSize / 2, petalSize, petalSize);
                }
                // เกสรกลางดอก
                g2d.setColor(new Color(255, 215, 110, 230));
                g2d.fillOval(fx - 2, fy - 2, 4, 4);
            } else {
                // กลีบดอกไม้เดี่ยวลอยคละทิศทาง
                g2d.setColor(new Color(255, 230, 240, 180));
                g2d.fillOval(fx, fy, petalSize + 2, petalSize - 1);
            }
        }

        // Layer 7: ประกายดาวระยิบระยับบนพื้นพุ่ม
        for (int i = 0; i < 7; i++) {
            int starX = x + (Math.abs(seed * (i + 5) * 41) % (int)(w * 0.85)) + (int)(w * 0.08);
            int starY = y + (Math.abs(seed * (i + 3) * 53) % (int)(h * 0.85)) + (int)(h * 0.08);
            int starLen = 3 + (i % 3) * 2;

            // ดาว 4 แฉกเรืองแสง
            g2d.setColor(new Color(255, 255, 220, 210));
            g2d.drawLine(starX - starLen, starY, starX + starLen, starY);
            g2d.drawLine(starX, starY - starLen, starX, starY + starLen);
            g2d.setColor(new Color(255, 255, 255, 240));
            g2d.fillOval(starX - 1, starY - 1, 3, 3);
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
    }

    // SCENE 4: การบินท้องฟ้ากับฝูงนกในอวกาศ
    private void drawFlyingScene(Graphics2D g2d, double t) {
        // พื้นหลังอวกาศแบบDeep Space Gradient
        Point2D skyStart = new Point2D.Float(0, 0);
        Point2D skyEnd = new Point2D.Float(0, HEIGHT);
        LinearGradientPaint skyGrad = new LinearGradientPaint(skyStart, skyEnd,
                new float[]{0.0f, 0.4f, 0.8f, 1.0f},
                new Color[]{new Color(5, 10, 30), new Color(15, 30, 70), new Color(25, 55, 115), new Color(15, 35, 80)});
        g2d.setPaint(skyGrad);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // กระแสทางช้างเผือกเรืองแสง 
        g2d.setColor(new Color(100, 160, 240, 25));
        GeneralPath nebula = new GeneralPath();
        nebula.moveTo(0, 50);
        nebula.curveTo(200, 150, 400, 100, 600, 300);
        nebula.lineTo(600, 450);
        nebula.curveTo(350, 250, 150, 300, 0, 200);
        nebula.closePath();
        g2d.fill(nebula);

        //ดาวเคราะห์และดาวบริวารในอวกาศ
        // ดาวเสาร์ (ฝั่งซ้าย)
        drawRingedPlanet(g2d, 100, 260, 32);
        
        // ดาวเคราะห์ดวงเล็ก (ฝั่งขวา)
        drawGlowingPlanet(g2d, 480, 320, 22, new Color(245, 210, 130));
        drawGlowingPlanet(g2d, 460, 510, 14, new Color(230, 215, 170));

        // ประกายดาวกระพริบและดาว 5 แฉกกระจายทั่วท้องฟ้า
        for (int i = 0; i < 80; i++) {
            int sx = (i * 43 + 17) % WIDTH;
            int sy = (i * 29 + 11) % HEIGHT;
            int sSize = (i % 4 == 0) ? 3 : 2;
            int alpha = 100 + (int)(Math.sin(globalTime * 3 + i) * 120);
            g2d.setColor(new Color(255, 255, 220, Math.max(30, Math.min(255, alpha))));
            g2d.fillOval(sx, sy, sSize, sSize);

            // สุ่มวาดดาวดวงใหญ่/ดาว 5 แฉกประปราย
            if (i % 12 == 0) {
                drawMiniStar(g2d, sx, sy, 5);
            }
        }

        // ประกายดาวเรืองแสงแฉกใหญ่
        drawStarGlow(g2d, 120, 70, 12);
        drawStarGlow(g2d, 480, 100, 14);
        drawStarGlow(g2d, 230, 220, 10);
        drawStarGlow(g2d, 520, 240, 8);

        // ฝูงนกและการบินของเจ้าชายน้อย
        int flyX = (int) (180 + t * 220);
        int flyY = (int) (400 - Math.sin(t * Math.PI) * 100);

        int holdX = flyX + 16;
        int holdY = flyY - 26;

        // วาดฝูงนกพร้อมเชือกโยง
        int numBirds = 6;
        for (int i = 0; i < numBirds; i++) {
            int birdX = flyX - 120 + i * 45;
            int birdY = flyY - 240 + (int) (Math.sin(globalTime * 2 + i) * 12);

            // เชือกโยงสีขาวเรืองแสง
            g2d.setColor(new Color(225, 240, 255, 190));
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawLine(birdX, birdY, holdX, holdY);

            double wingFlap = Math.sin(globalTime * 3.5 + i * 0.6);
            drawProportionalFlappingBird(g2d, birdX, birdY, wingFlap);
        }

        // วาดเจ้าชายน้อยท่าบิน
        drawPrinceStandingFlight(g2d, flyX, flyY, holdX, holdY);
    }
    // วาดดาวเคราะห์ในอวกาศเพิ่ม
    private void drawRingedPlanet(Graphics2D g2d, int cx, int cy, int radius) {
        // เงาวงแหวนด้านหลังดาว
        g2d.setColor(new Color(180, 160, 130, 140));
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawOval(cx - radius - 18, cy - 8, (radius + 18) * 2, 16);

        // ตัวดาวเคราะห์
        Point2D p1 = new Point2D.Float(cx - radius, cy - radius);
        Point2D p2 = new Point2D.Float(cx + radius, cy + radius);
        LinearGradientPaint pGrad = new LinearGradientPaint(p1, p2,
                new float[]{0.0f, 0.5f, 1.0f},
                new Color[]{new Color(240, 200, 140), new Color(190, 140, 80), new Color(110, 70, 35)});
        g2d.setPaint(pGrad);
        g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // วงแหวนด้านหน้าดาว
        g2d.setColor(new Color(220, 195, 155, 210));
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawArc(cx - radius - 18, cy - 8, (radius + 18) * 2, 16, 180, 180);
    }

    private void drawGlowingPlanet(Graphics2D g2d, int cx, int cy, int radius, Color color) {
        // ออร่าเรืองแสงรอบดาว
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        g2d.fillOval(cx - radius - 4, cy - radius - 4, (radius + 4) * 2, (radius + 4) * 2);

        // ตัวดาว
        g2d.setColor(color);
        g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
    }


    // SCENE 5: เจอดอกกุหลาบ และปิดหนังสือ
    private void drawRoseAndClosingBook(Graphics2D g2d, double t) {
        if (t < 0.6) {
            // พื้นหลังอวกาศไล่เฉดสีDeep Space Gradient
            Point2D spaceStart = new Point2D.Float(0, 0);
            Point2D spaceEnd = new Point2D.Float(0, HEIGHT);
            LinearGradientPaint spaceGrad = new LinearGradientPaint(spaceStart, spaceEnd,
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{new Color(10, 14, 35), new Color(20, 25, 55), new Color(12, 16, 40)});
            g2d.setPaint(spaceGrad);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            // ดาวเคราะห์เรืองแสง
            g2d.setColor(new Color(130, 90, 190, 35));
            GeneralPath nebula = new GeneralPath();
            nebula.moveTo(0, 120);
            nebula.curveTo(200, 60, 420, 220, 600, 100);
            nebula.lineTo(600, 320);
            nebula.curveTo(380, 200, 180, 340, 0, 260);
            nebula.closePath();
            g2d.fill(nebula);

            // ประกายดาวกระพริบระยิบระยับ
            for (int i = 0; i < 85; i++) {
                int sx = (i * 47 + 13) % WIDTH;
                int sy = (i * 31 + 7) % 420; // ให้ดาวกระจายเฉพาะครึ่งบนท้องฟ้า
                int sSize = (i % 6 == 0) ? 3 : 2;

                int alpha = 100 + (int) (Math.sin(globalTime * 3.5 + i) * 125);
                g2d.setColor(new Color(255, 255, 230, Math.max(20, Math.min(255, alpha))));
                g2d.fillOval(sx, sy, sSize, sSize);

                // สุ่มวาดดาวประกายแฉกเรืองแสง
                if (i % 11 == 0) {
                    drawStarGlow(g2d, sx, sy, 6);
                }
            }

            // เติมดาวเคราะห์ประดับฉากหลัง
            // ดาวเคราะห์พร้อมวงแหวนมุมซ้ายบน (มีออร่าเรืองแสง)
            drawRingedPlanet(g2d, 100, 110, 28, new Color(245, 190, 120), new Color(215, 110, 140));
            // ดาวเคราะห์พาสเทลดวงเล็กมุมขวาบน
            drawSmallBgPlanet(g2d, 490, 90, 18, new Color(110, 200, 230, 220));

            // ดาวเคราะห์สีม่วงพาสเทลหลัก พร้อมหลุมอุกกาบาตมีมิติ
            drawGradientCraterPlanet(g2d, 300, 650, 360, 220);

            // วาดองค์ประกอบหลักตามภาพ (กุหลาบแก้ว, เจ้าชายน้อย, จิ้งจอก)
            int rx = 300;
            int ry = 360;

            drawDetailedRoseWithDome(g2d, rx, ry);
            drawPrinceSitting(g2d, rx - 100, ry + 75);
            drawFoxSitting(g2d, rx + 80, ry + 70);

        } else {
            // ช่วงปิดหนังสือเมื่อหมดซีน
            double closeT = (t - 0.6) / 0.4;
            g2d.setColor(new Color(25, 35, 60));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            drawBookOpening(g2d, 1.0 - closeT);
        }
    }

    // Background Planets
    
    // วาดดาวเคราะห์พร้อมวงแหวน
    private void drawRingedPlanet(Graphics2D g2d, int cx, int cy, int radius, Color c1, Color c2) {
        // ออร่าเรืองแสงรอบตัวดาว
        g2d.setColor(new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), 40));
        g2d.fillOval(cx - radius - 6, cy - radius - 6, (radius + 6) * 2, (radius + 6) * 2);

        // วงแหวนครึ่งหลัง (แบ็คกราวด์)
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.setColor(new Color(230, 200, 160, 160));
        g2d.drawArc(cx - radius - 18, cy - 8, (radius + 18) * 2, 16, 0, 180);

        // ตัวดาวเคราะห์ไล่เฉดสี
        Point2D p1 = new Point2D.Float(cx - radius, cy - radius);
        Point2D p2 = new Point2D.Float(cx + radius, cy + radius);
        LinearGradientPaint planetGrad = new LinearGradientPaint(p1, p2,
                new float[]{0.0f, 1.0f}, new Color[]{c1, c2});
        g2d.setPaint(planetGrad);
        g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // วงแหวนครึ่งหน้า (ทับหน้าดาว)
        g2d.setColor(new Color(255, 235, 190, 230));
        g2d.drawArc(cx - radius - 18, cy - 8, (radius + 18) * 2, 16, 180, 180);
    }

    // วาดดาวเคราะห์เล็กจางๆ
    private void drawSmallBgPlanet(Graphics2D g2d, int cx, int cy, int radius, Color baseColor) {
        g2d.setColor(baseColor);
        g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
        
        // เติมไฮไลต์เงาสว่างด้านบนดาว
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillOval(cx - radius + 3, cy - radius + 2, radius, radius - 2);
    }
    // วาดดาวเคราะห์และหลุมอุกกาบาต
    // วาดดาวเคราะห์และหลุมอุกกาบาต (อัปเดตเพิ่มทุ่งหญ้าและประกายดาวบนดาวเคราะห์)
    private void drawGradientCraterPlanet(Graphics2D g2d, int cx, int cy, int rx, int ry) {
        Point2D start = new Point2D.Float(cx - rx * 0.4f, cy - ry * 0.6f);
        Point2D end = new Point2D.Float(cx + rx * 0.6f, cy + ry * 0.6f);
        float[] fractions = {0.0f, 0.45f, 0.85f, 1.0f};
        Color[] colors = {
            new Color(255, 205, 230), 
            new Color(210, 140, 235), 
            new Color(130, 95, 220),  
            new Color(75, 55, 150)    
        };
        LinearGradientPaint planetGradient = new LinearGradientPaint(start, end, fractions, colors);

        g2d.setPaint(planetGradient);
        g2d.fillOval(cx - rx, cy - ry, rx * 2, ry * 2);

        // หลุมอุกกาบาต
        drawCrater(g2d, cx - 120, cy - 30, 45, 30, new Color(180, 115, 215), new Color(110, 75, 180));
        drawCrater(g2d, cx + 40, cy + 20, 65, 45, new Color(160, 100, 210), new Color(90, 60, 160));
        drawCrater(g2d, cx - 40, cy + 60, 50, 32, new Color(150, 90, 200), new Color(85, 55, 150));
        drawCrater(g2d, cx + 130, cy - 40, 35, 25, new Color(190, 125, 225), new Color(120, 80, 190));
        drawCrater(g2d, cx - 180, cy + 30, 28, 18, new Color(170, 105, 210), new Color(100, 65, 165));
        drawCrater(g2d, cx + 170, cy + 50, 40, 26, new Color(130, 80, 185), new Color(75, 45, 135));

        // 1. ตกแต่งทุ่งหญ้าพาสเทลเล็กๆ บริเวณผิวโค้งขอบดาว
        drawPinkPlanetGrass(g2d, cx, cy, rx, ry);

        // 2. เติมประกายดาวเล็กๆ ละอองเวทมนตร์วิบวับรอบๆ พื้นผิวดาว
        drawPlanetSurfaceSparkles(g2d, cx, cy, rx, ry);
    }

    // ฟังก์ชันวาดทุ่งหญ้าเล็กๆ โทนชมพู-ม่วงบนขอบผิวดาว
    private void drawPinkPlanetGrass(Graphics2D g2d, int cx, int cy, int rx, int ry) {
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // สุ่มกระจายกอหญ้าตามขอบโค้งส่วนบนของดาวเคราะห์
        for (int i = 0; i < 45; i++) {
            // คำนวณมุมตามขอบโค้งดาว (ตั้งแต่มุมซ้ายไปขวา)
            double angle = Math.toRadians(-165 + i * 3.4);
            
            // พิกัดโคนหญ้าบนเส้นรอบวงดาว
            int gx = cx + (int) (Math.cos(angle) * (rx - 2));
            int gy = cy + (int) (Math.sin(angle) * (ry - 2));

            // เฉดสีชมพูพาสเทลกลมกลืนกับพื้นผิว
            Color grassColor = (i % 2 == 0) 
                    ? new Color(255, 215, 238, 220) 
                    : new Color(230, 165, 235, 200);
            g2d.setColor(grassColor);

            // วาดใบหญ้า 2-3 ใบต่อหนึ่งกอ
            int hHeight = 6 + (i % 4) * 2;
            int tilt = (i % 3 - 1) * 3;

            g2d.drawLine(gx, gy, gx + tilt - 2, gy - hHeight);
            g2d.drawLine(gx, gy, gx + tilt + 2, gy - hHeight + 2);
        }
    }

    // ฟังก์ชันวาดประกายดาวและละอองแสงรอบๆ พื้นผิวดาวเคราะห์
    private void drawPlanetSurfaceSparkles(Graphics2D g2d, int cx, int cy, int rx, int ry) {
        for (int i = 0; i < 28; i++) {
            int seed = i * 37 + 19;
            // กระจายจุดประกายดาวให้อยู่บริเวณส่วนบนและรอบๆ ผิวดาว
            int sx = cx - rx + (Math.abs(seed * 13) % (rx * 2));
            int sy = cy - ry + (Math.abs(seed * 29) % (int)(ry * 1.2));

            // ตรวจสอบให้อยู่ใกล้เคียงกับขอบดาวเคราะห์
            double dist = Math.pow((double)(sx - cx) / rx, 2) + Math.pow((double)(sy - cy) / ry, 2);
            if (dist >= 0.6 && dist <= 1.25) {
                int sparkleAlpha = 120 + (int)(Math.sin(globalTime * 4.0 + i) * 110);
                sparkleAlpha = Math.max(30, Math.min(255, sparkleAlpha));

                if (i % 3 == 0) {
                    // ประกายดาว 4 แฉกสีขาวทอง
                    g2d.setColor(new Color(255, 245, 220, sparkleAlpha));
                    int size = 3 + (i % 3) * 2;
                    g2d.drawLine(sx - size, sy, sx + size, sy);
                    g2d.drawLine(sx, sy - size, sx, sy + size);
                } else {
                    // ละอองดาวจุดเล็กๆ
                    g2d.setColor(new Color(255, 225, 245, sparkleAlpha));
                    int dotSize = 2 + (i % 2);
                    g2d.fillOval(sx, sy, dotSize, dotSize);
                }
            }
        }
    }

    private void drawCrater(Graphics2D g2d, int x, int y, int w, int h, Color lightInner, Color darkInner) {
        g2d.setColor(new Color(255, 230, 245, 180));
        g2d.fillOval(x - 2, y - 2, w + 4, h + 4);

        Point2D p1 = new Point2D.Float(x, y);
        Point2D p2 = new Point2D.Float(x + w, y + h);
        LinearGradientPaint craterGrad = new LinearGradientPaint(p1, p2, new float[]{0.0f, 1.0f}, new Color[]{darkInner, lightInner});
        g2d.setPaint(craterGrad);
        g2d.fillOval(x, y, w, h);

        g2d.setColor(new Color( darkInner.getRed() - 20 < 0 ? 0 : darkInner.getRed() - 20, 
                                 darkInner.getGreen() - 20 < 0 ? 0 : darkInner.getGreen() - 20, 
                                 darkInner.getBlue() - 20 < 0 ? 0 : darkInner.getBlue() - 20, 160));
        g2d.fillOval(x + w / 6, y + h / 6, (int)(w * 0.65), (int)(h * 0.65));
    }
    
    // เมธอดช่วยวาดองค์ประกอบอื่น ๆ 
    private void drawStarGlow(Graphics2D g2d, int cx, int cy, int size) {
        g2d.setColor(new Color(255, 255, 220, 200));
        g2d.drawLine(cx - size, cy, cx + size, cy);
        g2d.drawLine(cx, cy - size, cx, cy + size);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(cx - 2, cy - 2, 4, 4);
    }

    private void drawProportionalFlappingBird(Graphics2D g2d, int x, int y, double flap) {
        g2d.setColor(Color.WHITE);

        GeneralPath headAndBeak = new GeneralPath();
        headAndBeak.moveTo(x - 6, y - 2);
        headAndBeak.curveTo(x - 9, y - 5, x - 12, y - 3, x - 13, y - 1);
        headAndBeak.lineTo(x - 16, y);
        headAndBeak.lineTo(x - 13, y + 2);
        headAndBeak.curveTo(x - 10, y + 4, x - 6, y + 4, x - 4, y + 2);
        headAndBeak.closePath();
        g2d.fill(headAndBeak);

        GeneralPath bodyAndTail = new GeneralPath();
        bodyAndTail.moveTo(x - 6, y - 2);
        bodyAndTail.curveTo(x, y - 3, x + 6, y - 1, x + 10, y + 1);
        bodyAndTail.lineTo(x + 18, y + 4);
        bodyAndTail.lineTo(x + 16, y + 7);
        bodyAndTail.lineTo(x + 8, y + 4);
        bodyAndTail.curveTo(x + 2, y + 5, x - 4, y + 3, x - 6, y + 1);
        bodyAndTail.closePath();
        g2d.fill(bodyAndTail);

        double wingOffsetY = flap * 10.0; 

        GeneralPath wingBack = new GeneralPath();
        wingBack.moveTo(x - 2, y - 2);
        wingBack.curveTo(x - 4, y - 8 + wingOffsetY * 0.5, x - 8, y - 13 + wingOffsetY, x - 11, y - 15 + wingOffsetY);
        wingBack.curveTo(x - 7, y - 10 + wingOffsetY, x - 2, y - 5 + wingOffsetY * 0.5, x + 3, y - 1);
        wingBack.closePath();
        g2d.fill(wingBack);

        GeneralPath wingFront = new GeneralPath();
        wingFront.moveTo(x - 3, y - 1);
        wingFront.curveTo(x - 6, y - 10 + wingOffsetY * 0.6, x - 10, y - 17 + wingOffsetY, x - 14, y - 19 + wingOffsetY);
        wingFront.curveTo(x - 9, y - 12 + wingOffsetY, x - 3, y - 6 + wingOffsetY * 0.5, x + 4, y);
        wingFront.closePath();
        g2d.fill(wingFront);
    }

    private void drawDetailedRoseWithDome(Graphics2D g2d, int cx, int cy) {
        int baseY = cy + 65;

        // วาดฐานของครอบแก้ว
        g2d.setColor(new Color(100, 65, 40));
        g2d.fillOval(cx - 55, baseY + 4, 110, 22);
        g2d.setColor(new Color(140, 95, 55));
        g2d.fillOval(cx - 50, baseY, 100, 18);
        g2d.setColor(new Color(80, 50, 30));
        g2d.drawOval(cx - 50, baseY, 100, 18);

        // ก้านกุหลาบและใบ
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

        // กลีบเลี้ยงและดอกกุหลาบ
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

        // ตัวครอบแก้ว (Glass Dome)
        int domeW = 86;
        int domeH = 110;
        int domeX = cx - domeW / 2;
        int domeY = baseY - domeH + 5;

        g2d.setColor(new Color(220, 240, 255, 45));
        g2d.fillRoundRect(domeX, domeY, domeW, domeH, domeW, domeW);

        g2d.setColor(new Color(160, 190, 215, 180));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(domeX, domeY, domeW, domeH, domeW, domeW);

        // แสงสะท้อนบนแก้ว
        g2d.setColor(new Color(255, 255, 255, 140));
        g2d.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(domeX + 8, domeY + 10, 25, 70, 100, 75);
        g2d.drawArc(domeX + 14, domeY + 16, 15, 30, 110, 60);
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

    // วาดโมเดลตัวละคร 
    
    // วาดเจ้าชายน้อยท่านั่ง
    private void drawPrinceSitting(Graphics2D g2d, int x, int y) {
        // รองเท้า
        g2d.setColor(new Color(110, 70, 45));
        g2d.fillOval(x - 15, y + 30, 14, 9);
        g2d.fillOval(x + 1, y + 30, 14, 9);

        // ตัว/ชุดสีเขียว
        g2d.setColor(new Color(160, 205, 95));
        g2d.fillRoundRect(x - 16, y + 6, 32, 28, 18, 18);

        // เข็มขัดสีแดง
        g2d.setColor(new Color(210, 50, 55));
        g2d.fillRect(x - 16, y + 12, 32, 5);

        // กระดุมชุด
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 2, y + 1, 5, 5);
        g2d.fillOval(x - 2, y + 6, 5, 5);

        // คอ
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 7, y - 7, 14, 12);

        // ศีรษะ
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 20, y - 34, 40, 35);

        // ผมทรงเจ้าชาย
        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x - 22, y - 40, 44, 27);
        g2d.fillOval(x - 17, y - 44, 25, 20);
        g2d.fillOval(x - 2, y - 43, 22, 17);
        g2d.fillOval(x - 23, y - 30, 12, 18);
        g2d.fillOval(x + 11, y - 30, 12, 18);

        // หู
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 22, y - 21, 6, 8);
        g2d.fillOval(x + 16, y - 21, 6, 8);

        // ตาและรอยยิ้ม
        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x - 10, y - 18, 5, 5);
        g2d.fillOval(x + 5, y - 18, 5, 5);

        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x - 5, y - 14, 10, 6, 200, 140);

        // แก้มอมชมพู
        g2d.setColor(new Color(245, 160, 160, 140));
        g2d.fillOval(x - 16, y - 14, 7, 5);
        g2d.fillOval(x + 9, y - 14, 7, 5);

        // ปกคอเสื้อ
        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x - 17, y - 6, 34, 11, 10, 10);

        // ผ้าพันคอ
        double scarfWave1 = Math.sin(globalTime * 3.5) * 8;
        double scarfWave2 = Math.cos(globalTime * 3.5) * 12;

        GeneralPath scarfTail = new GeneralPath();
        scarfTail.moveTo(x - 12, y - 2);
        scarfTail.curveTo(x - 30, y - 2 + scarfWave1, x - 55, y + scarfWave2, x - 78, y - 5 + scarfWave1);
        scarfTail.lineTo(x - 75, y + 5 + scarfWave1);
        scarfTail.curveTo(x - 55, y + 10 + scarfWave2, x - 30, y + 8 + scarfWave1, x - 12, y + 8);
        scarfTail.closePath();
        g2d.fill(scarfTail);

        g2d.setColor(new Color(245, 200, 50));
        drawMiniStar(g2d, (int) (x - 70), (int) (y - 1 + scarfWave1), 4);
    }

    // วาดเจ้าชายน้อยจับเชือกบิน
    private void drawPrinceStandingFlight(Graphics2D g2d, int x, int y, int holdX, int holdY) {
        // รองเท้า
        g2d.setColor(new Color(110, 70, 45));
        g2d.fillOval(x - 7, y + 31, 13, 8);
        g2d.fillOval(x + 2, y + 31, 13, 8);

        // ขาและลำตัว
        g2d.setColor(new Color(160, 205, 95));
        g2d.fillRoundRect(x - 6, y + 19, 8, 15, 7, 7);
        g2d.fillRoundRect(x + 3, y + 19, 8, 15, 7, 7);

        g2d.fillRoundRect(x - 10, y + 2, 26, 22, 16, 16);

        // เข็มขัด
        g2d.setColor(new Color(210, 50, 55));
        g2d.fillRect(x - 10, y + 8, 26, 4);

        // แขนเอื้อมจับเชือก
        g2d.setColor(new Color(160, 205, 95));
        g2d.setStroke(new BasicStroke(7.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x + 6, y + 4, holdX, holdY);

        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(holdX - 4, holdY - 4, 8, 8);

        // คอและศีรษะ
        g2d.setColor(new Color(255, 228, 208));
        g2d.fillOval(x - 2, y - 7, 12, 12);
        g2d.fillOval(x - 15, y - 31, 34, 29);

        // ผม
        g2d.setColor(new Color(245, 215, 95));
        g2d.fillOval(x - 17, y - 37, 38, 24);
        g2d.fillOval(x - 15, y - 41, 22, 17);
        g2d.fillOval(x + 2, y - 38, 17, 14);
        g2d.fillOval(x + 12, y - 26, 10, 14);

        // หน้าตา
        g2d.setColor(new Color(45, 40, 40));
        g2d.fillOval(x + 9, y - 17, 5, 5);
        g2d.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x + 8, y - 13, 7, 5, 200, 140);

        g2d.setColor(new Color(245, 160, 160, 140));
        g2d.fillOval(x + 12, y - 13, 6, 5);

        // ปกคอเสื้อ
        g2d.setColor(new Color(215, 45, 50));
        g2d.fillRoundRect(x - 12, y - 5, 28, 9, 7, 7);

        // ผ้าพันคอ
        double wave1 = Math.sin(globalTime * 4) * 6;
        double wave2 = Math.cos(globalTime * 4) * 8;

        GeneralPath scarf = new GeneralPath();
        scarf.moveTo(x - 10, y - 2);
        scarf.curveTo(x - 40, y - 2 + wave1, x - 80, y - 10 + wave2, x - 120, y - 6 + wave1);
        scarf.lineTo(x - 120, y + 3 + wave1);
        scarf.curveTo(x - 80, y + 3 + wave2, x - 40, y + 6 + wave1, x - 10, y + 5);
        scarf.closePath();
        g2d.fill(scarf);

        g2d.setColor(new Color(245, 200, 50));
        drawMiniStar(g2d, (int) (x - 112), (int) (y - 2 + wave1), 4);

        // จิ้งจอกบนคอ/ไหล่เจ้าชาย (ขยายขนาดใหญ่ขึ้น + หางฟูสะบัดตามลมไปทางซ้าย)
        int foxX = x - 12;
        int foxY = y - 24;

        // คำนวณการสะบัดของหางไปทางซ้ายตามแรงลม
        double windSway = Math.sin(globalTime * 3.5) * 8;
        double windSwayY = Math.cos(globalTime * 3.5) * 4;

        // 1. หางฟูสะบัดไปทางซ้าย (ทรงเดียวกับท่านั่ง)
        GeneralPath foxRidingTail = new GeneralPath();
        foxRidingTail.moveTo(foxX + 6, foxY + 6);
        foxRidingTail.curveTo(foxX - 10, foxY + 12, foxX - 35 + windSway, foxY + windSwayY, foxX - 42 + windSway, foxY - 14 + windSwayY);
        foxRidingTail.curveTo(foxX - 28 + windSway, foxY - 24, foxX - 14, foxY - 10, foxX, foxY - 2);
        foxRidingTail.closePath();
        g2d.setColor(new Color(225, 125, 50));
        g2d.fill(foxRidingTail);

        // ปลายหางสีขาว
        GeneralPath foxRidingTailTip = new GeneralPath();
        foxRidingTailTip.moveTo(foxX - 42 + windSway, foxY - 14 + windSwayY);
        foxRidingTailTip.curveTo(foxX - 46 + windSway, foxY - 4 + windSwayY, foxX - 32 + windSway, foxY - 2, foxX - 25 + windSway, foxY - 8);
        foxRidingTailTip.curveTo(foxX - 26 + windSway, foxY - 18, foxX - 35 + windSway, foxY - 24, foxX - 42 + windSway, foxY - 14 + windSwayY);
        foxRidingTailTip.closePath();
        g2d.setColor(new Color(252, 250, 245));
        g2d.fill(foxRidingTailTip);

        // 2. ลำตัวจิ้งจอก (ขยายใหญ่ขึ้น)
        g2d.setColor(new Color(225, 125, 50));
        g2d.fillOval(foxX - 10, foxY - 6, 26, 20);

        // 3. หูจิ้งจอก (ทรงสามเหลี่ยม 2 ข้าง)
        int[] fEarX1 = {foxX - 8, foxX - 4, foxX + 3};
        int[] fEarY1 = {foxY - 6, foxY - 22, foxY - 6};
        g2d.setColor(new Color(40, 30, 30));
        g2d.fillPolygon(fEarX1, fEarY1, 3);
        int[] fInnerEarX1 = {foxX - 6, foxX - 4, foxX + 1};
        int[] fInnerEarY1 = {foxY - 6, foxY - 18, foxY - 6};
        g2d.setColor(new Color(252, 250, 245));
        g2d.fillPolygon(fInnerEarX1, fInnerEarY1, 3);

        int[] fEarX2 = {foxX + 5, foxX + 10, foxX + 16};
        int[] fEarY2 = {foxY - 6, foxY - 22, foxY - 6};
        g2d.setColor(new Color(40, 30, 30));
        g2d.fillPolygon(fEarX2, fEarY2, 3);
        int[] fInnerEarX2 = {foxX + 7, foxX + 10, foxX + 14};
        int[] fInnerEarY2 = {foxY - 6, foxY - 18, foxY - 6};
        g2d.setColor(new Color(252, 250, 245));
        g2d.fillPolygon(fInnerEarX2, fInnerEarY2, 3);

        // 4. ใบหน้าและปากสีขาว
        g2d.setColor(new Color(252, 250, 245));
        g2d.fillOval(foxX - 6, foxY + 2, 18, 12);

        // 5. ตาหลับพริ้มและจมูก
        g2d.setColor(new Color(40, 30, 30));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(foxX - 3, foxY + 3, 5, 4, 20, 140);
        g2d.drawArc(foxX + 5, foxY + 3, 5, 4, 20, 140);
        g2d.fillOval(foxX + 1, foxY + 8, 4, 3);
    }

    // วาดจิ้งจอกท่านั่ง
    private void drawFoxSitting(Graphics2D g2d, int x, int y) {
        double tailSway = Math.sin(globalTime * 2.5) * 6;
        double tailSway2 = Math.cos(globalTime * 2.5) * 4;

        // หาง
        GeneralPath tailBase = new GeneralPath();
        tailBase.moveTo(x + 8, y + 22);
        tailBase.curveTo(x + 28, y + 25, x + 48 + tailSway, y + 10 + tailSway2, x + 42 + tailSway, y - 18);
        tailBase.curveTo(x + 26 + tailSway, y - 28, x + 15, y + 2, x + 2, y + 16);
        tailBase.closePath();
        g2d.setColor(new Color(225, 125, 50));
        g2d.fill(tailBase);

        GeneralPath tailTip = new GeneralPath();
        tailTip.moveTo(x + 42 + tailSway, y - 18);
        tailTip.curveTo(x + 46 + tailSway, y - 8 + tailSway2, x + 34 + tailSway, y - 2, x + 26 + tailSway, y - 8);
        tailTip.curveTo(x + 25 + tailSway, y - 22, x + 34 + tailSway, y - 30, x + 42 + tailSway, y - 18);
        tailTip.closePath();
        g2d.setColor(new Color(252, 250, 245));
        g2d.fill(tailTip);

        // ขาหลังและลำตัว
        g2d.setColor(new Color(210, 110, 40));
        g2d.fillOval(x + 2, y + 12, 16, 18);

        g2d.setColor(new Color(225, 125, 50));
        g2d.fillOval(x - 12, y + 6, 26, 24);

        g2d.setColor(new Color(252, 250, 245));
        g2d.fillOval(x - 10, y + 4, 16, 22);

        // ขาหน้า
        g2d.setColor(new Color(60, 40, 35));
        g2d.fillRoundRect(x - 8, y + 16, 5, 16, 4, 4);
        g2d.fillRoundRect(x + 1, y + 16, 5, 16, 4, 4);

        // หู
        int[] earX1 = {x - 16, x - 11, x - 2};
        int[] earY1 = {y - 4, y - 32, y - 4};
        g2d.setColor(new Color(40, 30, 30));
        g2d.fillPolygon(earX1, earY1, 3);
        int[] innerEarX1 = {x - 14, x - 11, x - 4};
        int[] innerEarY1 = {y - 4, y - 27, y - 4};
        g2d.setColor(new Color(252, 250, 245));
        g2d.fillPolygon(innerEarX1, innerEarY1, 3);

        int[] earX2 = {x + 2, x + 11, x + 16};
        int[] earY2 = {y - 4, y - 32, y - 4};
        g2d.setColor(new Color(40, 30, 30));
        g2d.fillPolygon(earX2, earY2, 3);
        int[] innerEarX2 = {x + 4, x + 11, x + 14};
        int[] innerEarY2 = {y - 4, y - 27, y - 4};
        g2d.setColor(new Color(252, 250, 245));
        g2d.fillPolygon(innerEarX2, innerEarY2, 3);

        // หัว+หน้า
        g2d.setColor(new Color(225, 125, 50));
        GeneralPath head = new GeneralPath();
        head.moveTo(x - 15, y - 10);
        head.curveTo(x - 18, y - 2, x - 10, y + 6, x, y + 8);
        head.curveTo(x + 10, y + 6, x + 18, y - 2, x + 15, y - 10);
        head.curveTo(x + 8, y - 22, x - 8, y - 22, x - 15, y - 10);
        head.closePath();
        g2d.fill(head);

        g2d.setColor(new Color(252, 250, 245));
        GeneralPath muzzle = new GeneralPath();
        muzzle.moveTo(x - 14, y - 2);
        muzzle.curveTo(x - 8, y + 6, x, y + 8, x + 8, y + 6);
        muzzle.curveTo(x + 14, y - 2, x, y - 2, x - 14, y - 2);
        muzzle.closePath();
        g2d.fill(muzzle);

        // หน้าจิ้งจอก
        g2d.setColor(new Color(40, 30, 30));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x - 10, y - 5, 6, 5, 20, 140);
        g2d.drawArc(x + 4, y - 5, 6, 5, 20, 140);

        g2d.fillOval(x - 3, y + 3, 6, 4);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Little Prince - Smooth Story");
        LittlePrince anim = new LittlePrinceS();
        frame.add(anim);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
