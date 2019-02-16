/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fu.login;

import java.sql.SQLException;
import fu.client.ClientDAO;
import fu.client.ClientDTO;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Register extends javax.swing.JFrame {

    private byte[] person_image;

    public Register() {
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlDemoApp = new javax.swing.JPanel();
        lblImageApp = new javax.swing.JLabel();
        lblInfoApp = new javax.swing.JLabel();
        lblSignUp = new javax.swing.JLabel();
        lblFullname = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblUsername1 = new javax.swing.JLabel();
        txtUsername1 = new javax.swing.JTextField();
        lblAvarta = new javax.swing.JLabel();
        btnAvarta = new javax.swing.JButton();
        btnSignUp = new javax.swing.JButton();
        lblLoginForm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 520));
        setMinimumSize(new java.awt.Dimension(700, 520));
        setPreferredSize(new java.awt.Dimension(700, 520));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(null);

        pnlDemoApp.setBackground(new java.awt.Color(0, 102, 204));

        lblImageApp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/login/greeting-card.png"))); // NOI18N
        lblImageApp.setText("lbl");

        lblInfoApp.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lblInfoApp.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoApp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoApp.setText("Chat Application Login");

        javax.swing.GroupLayout pnlDemoAppLayout = new javax.swing.GroupLayout(pnlDemoApp);
        pnlDemoApp.setLayout(pnlDemoAppLayout);
        pnlDemoAppLayout.setHorizontalGroup(
            pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDemoAppLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblInfoApp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblImageApp, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        pnlDemoAppLayout.setVerticalGroup(
            pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDemoAppLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblImageApp, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(lblInfoApp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        pnlMain.add(pnlDemoApp);
        pnlDemoApp.setBounds(0, 0, 353, 488);

        lblSignUp.setFont(new java.awt.Font("Calibri", 0, 36)); // NOI18N
        lblSignUp.setForeground(new java.awt.Color(102, 102, 102));
        lblSignUp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSignUp.setText("Sign Up");
        pnlMain.add(lblSignUp);
        lblSignUp.setBounds(400, 10, 240, 44);

        lblFullname.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblFullname.setForeground(new java.awt.Color(51, 51, 51));
        lblFullname.setText("Fullname");
        pnlMain.add(lblFullname);
        lblFullname.setBounds(400, 210, 130, 16);

        txtFullName.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtFullName.setForeground(new java.awt.Color(51, 51, 51));
        txtFullName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(txtFullName);
        txtFullName.setBounds(400, 230, 230, 40);

        lblPassword.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(51, 51, 51));
        lblPassword.setText("Password");
        pnlMain.add(lblPassword);
        lblPassword.setBounds(400, 140, 130, 14);

        txtPassword.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(51, 51, 51));
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(txtPassword);
        txtPassword.setBounds(400, 160, 230, 40);

        lblUsername1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblUsername1.setForeground(new java.awt.Color(51, 51, 51));
        lblUsername1.setText("Username");
        pnlMain.add(lblUsername1);
        lblUsername1.setBounds(400, 70, 130, 16);

        txtUsername1.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtUsername1.setForeground(new java.awt.Color(51, 51, 51));
        txtUsername1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(txtUsername1);
        txtUsername1.setBounds(400, 90, 230, 40);

        lblAvarta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(lblAvarta);
        lblAvarta.setBounds(400, 290, 120, 100);

        btnAvarta.setText("Avarta");
        btnAvarta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvartaActionPerformed(evt);
            }
        });
        pnlMain.add(btnAvarta);
        btnAvarta.setBounds(530, 290, 100, 40);

        btnSignUp.setBackground(new java.awt.Color(0, 102, 204));
        btnSignUp.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        btnSignUp.setForeground(new java.awt.Color(255, 255, 255));
        btnSignUp.setText("Sign Up");
        btnSignUp.setBorder(null);
        btnSignUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });
        pnlMain.add(btnSignUp);
        btnSignUp.setBounds(470, 400, 100, 40);

        lblLoginForm.setBackground(new java.awt.Color(255, 255, 255));
        lblLoginForm.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblLoginForm.setForeground(new java.awt.Color(0, 102, 204));
        lblLoginForm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginForm.setText("Already Member Register click here to login");
        lblLoginForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLoginForm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLoginFormMouseClicked(evt);
            }
        });
        pnlMain.add(lblLoginForm);
        lblLoginForm.setBounds(400, 450, 240, 20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAvartaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvartaActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File f = fc.getSelectedFile();
        String fileName = f.getAbsolutePath();

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(fileName).getImage().getScaledInstance(lblAvarta.getWidth(), lblAvarta.getHeight(), Image.SCALE_SMOOTH));
        lblAvarta.setIcon(imageIcon);

        try {
            File image = new File(fileName);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            person_image = bos.toByteArray();

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnAvartaActionPerformed

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        try {
            String username = txtUsername1.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is not empty");
                return;
            }

            String password = txtPassword.getText().trim();

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password is not empty");
                return;
            }

            String fullname = txtFullName.getText().trim();
            if (fullname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fullname is not empty");
                return;
            }

            ClientDAO dao = new ClientDAO();
            ClientDTO dto = new ClientDTO(0, username, password, fullname, false, person_image);

            boolean result = dao.createAccount(dto);
            if (result) {
                JOptionPane.showMessageDialog(this, "Create account success");
                txtUsername1.setText("");
                txtPassword.setText("");
                txtFullName.setText("");
                lblAvarta.setIcon(null);
            } else {
                JOptionPane.showMessageDialog(this, "Create account fail");
                return;
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate key")) {
                JOptionPane.showMessageDialog(this, "Username is exist");
                return;
            }
        }
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void lblLoginFormMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLoginFormMouseClicked
        this.dispose();
        new Login();
    }//GEN-LAST:event_lblLoginFormMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
        new Login();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvarta;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JLabel lblAvarta;
    private javax.swing.JLabel lblFullname;
    private javax.swing.JLabel lblImageApp;
    private javax.swing.JLabel lblInfoApp;
    private javax.swing.JLabel lblLoginForm;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSignUp;
    private javax.swing.JLabel lblUsername1;
    private javax.swing.JPanel pnlDemoApp;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername1;
    // End of variables declaration//GEN-END:variables
}
