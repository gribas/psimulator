package com.automatak.dnp3.tools.controls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OpenDNP3Frame extends JPanel {

    private BufferedImage image = null;

    public OpenDNP3Frame()
    {
        try {
            image = ImageIO.read(this.getClass().getResource("/images/opendnp3.png"));
            Dimension d = new Dimension(image.getWidth(), image.getHeight());
            this.setMinimumSize(d);
            this.setMaximumSize(d);
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }

}
