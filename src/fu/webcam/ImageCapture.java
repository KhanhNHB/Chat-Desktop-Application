/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fu.webcam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class ImageCapture extends javax.swing.JFrame {

    private Webcam webcam;
    private boolean isRunning = false;

    public ImageCapture() {
        initComponents();
        setTitle("Webcam");
        setLocationRelativeTo(null);
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImageHolder = new javax.swing.JLabel();
        pnlWrapperOption = new javax.swing.JPanel();
        btnTakePicture = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(670, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblImageHolder.setBackground(new java.awt.Color(255, 255, 255));
        lblImageHolder.setMaximumSize(new java.awt.Dimension(640, 480));
        lblImageHolder.setMinimumSize(new java.awt.Dimension(640, 480));
        lblImageHolder.setPreferredSize(new java.awt.Dimension(640, 480));

        pnlWrapperOption.setBackground(new java.awt.Color(255, 255, 255));
        pnlWrapperOption.setLayout(new java.awt.GridLayout(1, 4, 10, 10));

        btnTakePicture.setBackground(new java.awt.Color(255, 255, 255));
        btnTakePicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/webcam/retro-squared-camera.png"))); // NOI18N
        btnTakePicture.setText("Take Picture");
        btnTakePicture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTakePictureActionPerformed(evt);
            }
        });
        pnlWrapperOption.add(btnTakePicture);

        btnStart.setBackground(new java.awt.Color(255, 255, 255));
        btnStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/webcam/rocket.png"))); // NOI18N
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        pnlWrapperOption.add(btnStart);

        btnPause.setBackground(new java.awt.Color(255, 255, 255));
        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/webcam/next-track.png"))); // NOI18N
        btnPause.setText("Pause");
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });
        pnlWrapperOption.add(btnPause);

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/webcam/icon.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        pnlWrapperOption.add(btnExit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImageHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlWrapperOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblImageHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlWrapperOption, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTakePictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTakePictureActionPerformed
        try {
            ImageIO.write(webcam.getImage(), "PNG", new File("firstCapture.png"));
        } catch (IOException ex) {
            Logger.getLogger(Webcam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTakePictureActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        isRunning = false;
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        if (!isRunning) {
            isRunning = true;
            new VideoFeedTaker().start();
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        exitApplication();
    }//GEN-LAST:event_btnExitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitApplication();
    }//GEN-LAST:event_formWindowClosing

    class VideoFeedTaker extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Image image = webcam.getImage();
                    lblImageHolder.setIcon(new ImageIcon(image));
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Webcam.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to close Webcam?", "Exit", JOptionPane.OK_CANCEL_OPTION);
        if (choice == 0) {
            isRunning = false;
            webcam.close();
//            this.dispose();
            setVisible(false);
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Webcam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Webcam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Webcam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Webcam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImageCapture().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnTakePicture;
    private javax.swing.JLabel lblImageHolder;
    private javax.swing.JPanel pnlWrapperOption;
    // End of variables declaration//GEN-END:variables
}
